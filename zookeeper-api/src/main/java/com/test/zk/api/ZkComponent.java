package com.test.zk.api;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * api 测试
 * https://blog.csdn.net/gosaint/article/details/83750237
 */
@Component
public class ZkComponent {

    private Logger logger = LoggerFactory.getLogger(ZkComponent.class);

    @Value("${zk.servers}")
    private String servers;

    private ZooKeeper zooKeeper = null;

    public ZkComponent(){
        logger.info("zkComponent init.....");
    }

    @PostConstruct
    public void initZk(){
        logger.info("zk init....");
        try {
            zooKeeper=new ZooKeeper(servers, 5000, event -> {
                /** 判断是否和服务器之间取得了连接*/
                if(event.getState()== Watcher.Event.KeeperState.SyncConnected){
                    logger.info("已经触发了{}事件！", event.getType());
                }
            });
        } catch (IOException e) {
            logger.error("连接zk失败");
        }
        logger.info("zk init end...");
    }

    public void createNode(){
        String path = "/api_test";
        String nodePath = null;
        try {
            nodePath = zooKeeper.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(nodePath);
    }

}
