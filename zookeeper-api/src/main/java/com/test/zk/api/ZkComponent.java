package com.test.zk.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                    logger.info("已经触发了{}事件！", event);
                }
            });
        } catch (IOException e) {
            logger.error("连接zk失败");
        }
        logger.info("zk init end...");
    }

    /**
     * 创建路径和节点
     * @param path      全路径
     * @param value     节点值
     * @param pathMode  创建路径模式
     * @param nodeMode  创建节点模式
     * @return
     */
    public boolean createPath(String path, String value, CreateMode pathMode, CreateMode nodeMode){
        String[] pathArray = path.split("/");
        String tempPath = "/";
        for (int i = 1; i < pathArray.length; i++) {
            if (i == 1){
                tempPath = tempPath + pathArray[i];
            } else {
                tempPath = tempPath + "/" + pathArray[i];
            }
            try {
                if (zooKeeper.exists(tempPath, false) == null){
                    String nodePath = null;
                    if (i == pathArray.length - 1){
                        nodePath = zooKeeper.create(tempPath, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, nodeMode);
                    } else {
                        nodePath = zooKeeper.create(tempPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, pathMode);
                    }
                    logger.info("create path:{} success", nodePath);
                }
            } catch (KeeperException | InterruptedException e) {
                logger.error("create path:{} error, msg:{}", tempPath, e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 获得子节点
     * @param path
     * @return
     */
    public List<String> getChildren(String path){
        List<String> list = new ArrayList<>();
        try {
            list.addAll(zooKeeper.getChildren(path, false));
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取节点数据
     * @param path
     * @return
     */
    public String getData(String path){
        try {
            return new String(zooKeeper.getData(path, false, null));
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置值
     * @param path
     * @param data
     * @return
     */
    public boolean setData(String path, String data) {
        try {
            /**
             * version -1不检测dataVersion, 如果要检测，改值传递查询出来的dataVersion既可
             */
            Stat stat = zooKeeper.setData(path, data.getBytes(), -1);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
