package com.test.two;

import com.test.INodeRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class WebListener implements ServletContextListener {

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private INodeRegistry nodeRegistry;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        nodeRegistry.register("node",String.format("%s:%d",serverAddress,serverPort));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
