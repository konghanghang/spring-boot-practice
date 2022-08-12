package com.tools.jdbc;

import com.tools.jdbc.constant.DatasourceTypeEnum;
import com.tools.jdbc.model.DatasourceInfo;
import com.tools.jdbc.tool.JdbcUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JdbcTest {

    @Test
    public void sqlTest(){
        DatasourceInfo datasourceInfo = new DatasourceInfo();
        datasourceInfo.setIp("");
        datasourceInfo.setPort(3306);
        datasourceInfo.setType(DatasourceTypeEnum.MYSQL.getType());
        datasourceInfo.setDatabaseName("");
        datasourceInfo.setUsername("root");
        datasourceInfo.setPassword("");
        String jdbcUrl = JdbcUtils.getJdbcUrl(datasourceInfo.getType(), datasourceInfo.getIp(), datasourceInfo.getPort(), datasourceInfo.getDatabaseName(), datasourceInfo.getSidType());
        boolean canConnection = JdbcUtils.isCanConnection(jdbcUrl, datasourceInfo.getUsername(), datasourceInfo.getPassword());
        Assertions.assertEquals(true, canConnection);
    }

}
