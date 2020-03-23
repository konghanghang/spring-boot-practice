package com.sql.model;

public class DatasourceInfo {

    private String name;

    private Integer type;

    private String ip;

    private Integer port;

    private String username;

    private String password;

    private String databaseName;

    // oracle专属，1：实例，2：服务
    private Integer sidType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Integer getSidType() {
        return sidType;
    }

    public void setSidType(Integer sidType) {
        this.sidType = sidType;
    }
}
