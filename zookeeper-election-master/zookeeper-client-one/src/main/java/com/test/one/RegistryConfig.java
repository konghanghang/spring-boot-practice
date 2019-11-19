package com.test.one;

import com.test.INodeRegistry;
import com.test.NodeRegistryImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "registry")
public class RegistryConfig {

    private String servers;

    private String nodeName;

    @Bean
    public INodeRegistry serviceRegistry() {
        return new NodeRegistryImpl(servers, nodeName);
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
