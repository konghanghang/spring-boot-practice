package com.tools.jdbc.tool;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tools.jdbc.constant.DatasourceTypeEnum;
import com.tools.jdbc.model.DatasourceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class JdbcUtils {

    public static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
    private static Cache<String, Object> cacheMap;

    static {
        cacheMap = CacheBuilder.newBuilder().removalListener((removalNotification) -> {
            DruidDataSource dataSource = (DruidDataSource)removalNotification.getValue();
            dataSource.close();
            logger.info("JdbcUtils remove datasource, key:{}, value:{} ", removalNotification.getKey(), removalNotification.getValue());
        }).initialCapacity(10).concurrencyLevel(4).expireAfterWrite(10L, TimeUnit.MINUTES).build();
    }

    private JdbcUtils() {
    }

    public static String getDriverClass(Integer databaseType) {
        String driverClass = null;
        if (databaseType == 1) {
            driverClass = "com.mysql.jdbc.Driver";
        } else if (databaseType == 2) {
            driverClass = "oracle.jdbc.driver.OracleDriver";
        } else if (databaseType == 3) {
            driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        } else if (databaseType == 4) {
            driverClass = "com.ibm.db2.jcc.DB2Driver";
        } else if (databaseType == 5) {
            driverClass = "com.sybase.jdbc4.jdbc.SybDriver";
        } else if (databaseType == 6) {
            driverClass = "dm.jdbc.driver.DmDriver";
        }

        return driverClass;
    }

    public static String getJdbcUrl(Integer databaseType, String ip, Integer port, String databaseName, Integer sidType) {
        String jdbcUrl = null;
        if (databaseType == 1) {
            jdbcUrl = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
        } else if (databaseType == 2) {
            // 实例
            if (sidType == 0) {
                jdbcUrl = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP) (HOST=" + ip + ") (PORT=" + port + "))) (CONNECT_DATA=(SID=" + databaseName + ")))";
            } else if (sidType == 1) {
                // 服务
                jdbcUrl = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP) (HOST=" + ip + ") (PORT=" + port + "))) (CONNECT_DATA=(SERVICE_NAME=" + databaseName + ")))";
            } else {
                jdbcUrl = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP) (HOST=" + ip + ") (PORT=" + port + "))) (CONNECT_DATA=(SID=" + databaseName + ")))";
            }
        } else if (databaseType == 3) {
            jdbcUrl = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + databaseName;
        } else if (databaseType == 4) {
            jdbcUrl = "jdbc:db2://" + ip + ":" + port + "/" + databaseName;
        } else if (databaseType == 5) {
            jdbcUrl = "jdbc:sybase:Tds:" + ip + ":" + port + "/" + databaseName;
        } else if (databaseType == 6) {
            jdbcUrl = "jdbc:dm://" + ip + ":" + port;
        }

        return jdbcUrl;
    }

    public static boolean isCanConnection(String dbUrl, String dbUsername, String dbPassword) {
        boolean putFlag = false;
        DruidDataSource dataSource = (DruidDataSource)cacheMap.getIfPresent(dbUrl);
        if (dataSource == null) {
            putFlag = true;
            dataSource = createDataSource(dbUrl, dbUsername, dbPassword);
        }

        boolean result = false;
        DruidPooledConnection connection = null;

        try {
            connection = dataSource.getConnection();
            result = true;
            if (putFlag) {
                cacheMap.put(dbUrl, dataSource);
            }
        } catch (SQLException e) {
            logger.error("获取sql连接失败", e);
        } finally {
            close(connection, null, null);
        }

        return result;
    }

    public static List<Map<String, Object>> getTableInfo(DatasourceInfo datasourceInfo) {
        DruidDataSource dataSource = getDatasource(datasourceInfo);
        Connection conn = null;
        List<Map<String, Object>> tableInfos = new ArrayList();

        try {
            conn = dataSource.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = null;
            if (datasourceInfo.getType().equals(DatasourceTypeEnum.ORACLE.getType())) {
                tables = metaData.getTables(null, datasourceInfo.getUsername().toUpperCase(), "%", new String[]{"TABLE"});
            } else {
                tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            }

            while(tables.next()) {
                Map<String, Object> tableInfo = new HashMap<>();
                String tableName = tables.getString("TABLE_NAME");

                ResultSet columns = metaData.getColumns(null, "%", tableName, "%");
                List<Map<String, String>> fields = new ArrayList();

                while(columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String typeName = columns.getString("TYPE_NAME");
                    Map<String, String> field = new HashMap();
                    field.put("name", columnName);
                    field.put("type", typeName);
                    fields.add(field);
                }

                if (columns != null) {
                    columns.close();
                }
                tableInfo.put("tableName", tableName);
                tableInfo.put("fields", fields);
            }

            if (tables != null) {
                tables.close();
            }
        } catch (SQLException e) {
            logger.error("获取sql连接失败", e);
        } finally {
            close(conn, null, null);
        }

        return tableInfos;
    }

    public static DruidDataSource getDatasource(DatasourceInfo datasourceInfo) {
        String jdbcUrl = getJdbcUrl(datasourceInfo.getType(), datasourceInfo.getIp(), datasourceInfo.getPort(), datasourceInfo.getDatabaseName(), datasourceInfo.getSidType());
        isCanConnection(jdbcUrl, datasourceInfo.getUsername(), datasourceInfo.getPassword());
        DruidDataSource dataSource = (DruidDataSource)cacheMap.getIfPresent(jdbcUrl);
        return dataSource;
    }

    private Map<String, Object> executeSqlGetData(DruidDataSource datasource, String sql) {
        Map<String, Object> sqlResult = new HashMap<>();
        Connection connection = null;
        ResultSet resultSet = null;
        Statement statement = null;
        List<Map<String, Object>> arrayNode = new ArrayList();
        List<String> column = new ArrayList();
        List<Map<String, Object>> headerObjs = new ArrayList();

        try {
            connection = datasource.getConnection();
            statement = connection.createStatement();
            statement.setQueryTimeout(60);
            resultSet = statement.executeQuery(sql);
            if (Objects.nonNull(resultSet)) {
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnCount = rsmd.getColumnCount();
                int row = 1;

                while(resultSet.next()) {
                    Map<String, Object> objectNode = new LinkedHashMap();

                    for(int i = 1; i <= columnCount; ++i) {
                        String columnLabel = rsmd.getColumnLabel(i);
                        String columnTypeName = rsmd.getColumnTypeName(i);
                        if (row == 1) {
                            Map<String, Object> headerObj = new HashMap();
                            headerObj.put("name", columnLabel);
                            headerObj.put("type", columnTypeName);
                            column.add(columnLabel);
                            headerObjs.add(headerObj);
                        }

                        Object value = resultSet.getObject(i);
                        objectNode.put(columnLabel, value);
                    }

                    ++row;
                    arrayNode.add(objectNode);
                }
            }
        } catch (SQLException e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            JdbcUtils.close(connection, statement, resultSet);
        }

        sqlResult.put("column", column);
        sqlResult.put("data", arrayNode);
        sqlResult.put("headerObjs", headerObjs);
        return sqlResult;
    }

    private static DruidDataSource createDataSource(String url, String username, String password) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setMaxActive(50);
        druidDataSource.setInitialSize(5);
        druidDataSource.setMaxWait(60000L);
        druidDataSource.setMinIdle(1);
        druidDataSource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=1;druid.stat.logSlowSql=true");
        return druidDataSource;
    }

    private static void close(Connection conn, Statement st, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            logger.error("jdbcUtils close fail rs. {}", e);
        }

        try {
            if (st != null) {
                st.close();
            }
        } catch (Exception e) {
            logger.error("jdbcUtils close fail st. {}", e);
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            logger.error("jdbcUtils close fail conn. {}", e);
        }

    }

}
