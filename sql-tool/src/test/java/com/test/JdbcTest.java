package com.test;

import com.sql.constant.DatasourceTypeEnum;
import com.sql.model.DatasourceInfo;
import com.sql.tool.JdbcUtils;
import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertEquals(true, canConnection);
    }

}
