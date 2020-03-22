package com.test.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="es.properties")
public class ElasticsearchProperties {

    private String username;

    private String password;

    private String clusterName;

    private String host;

    private Integer tcpPort;

    private Integer httpPort;

    private String schema;

    private String pkcsTransportFilePath;

    private String pkcsClientFilePath;

    private String pemFilePath;

    private String certificatesType;

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

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(Integer tcpPort) {
        this.tcpPort = tcpPort;
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getPkcsTransportFilePath() {
        return pkcsTransportFilePath;
    }

    public void setPkcsTransportFilePath(String pkcsTransportFilePath) {
        this.pkcsTransportFilePath = pkcsTransportFilePath;
    }

    public String getPkcsClientFilePath() {
        return pkcsClientFilePath;
    }

    public void setPkcsClientFilePath(String pkcsClientFilePath) {
        this.pkcsClientFilePath = pkcsClientFilePath;
    }

    public String getPemFilePath() {
        return pemFilePath;
    }

    public void setPemFilePath(String pemFilePath) {
        this.pemFilePath = pemFilePath;
    }

    public String getCertificatesType() {
        return certificatesType;
    }

    public void setCertificatesType(String certificatesType) {
        this.certificatesType = certificatesType;
    }
}
