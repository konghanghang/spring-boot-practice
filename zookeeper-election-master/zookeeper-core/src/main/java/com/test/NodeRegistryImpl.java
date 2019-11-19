package com.test;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class NodeRegistryImpl implements INodeRegistry, Watcher {

    private static Logger logger = LoggerFactory.getLogger(NodeRegistryImpl.class);

    private static CountDownLatch latch = new CountDownLatch(1);

    private ZooKeeper zk;
    private String nodeName;

    private static final int SESSION_TIMEOUT = 5000;

    private static final String REGISTRY_PATH = "/registry";
    private static final String REGISTRY_MASTER_PATH = REGISTRY_PATH + "/master";

    public NodeRegistryImpl() {

    }

    public NodeRegistryImpl(String zkServers, String nodeName) {
        try {
            zk = new ZooKeeper(zkServers, SESSION_TIMEOUT, this);
            latch.await();
            this.nodeName = nodeName;
            logger.info("connected to zookeeper");
        } catch (Exception ex) {
            logger.error("create zookeeper client failure", ex);
        }
    }


    @Override
    public void register(String registerPath, String nodeAddress) {
        try {
            if (zk.exists(REGISTRY_PATH, false) == null) {
                zk.create(REGISTRY_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.debug("create registry node:{}", REGISTRY_PATH);
            }
            //创建服务节点（持久节点）
            String servicePath = REGISTRY_PATH + "/" + registerPath;
            if (zk.exists(servicePath, false) == null) {
                zk.create(servicePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.debug("create service node:{}", servicePath);
            }
            //创建地址节点
            String addressPath = servicePath + "/address-";
            String addressNode = zk.create(addressPath, nodeAddress.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.debug("create address node:{} => {}", addressNode, nodeAddress);

            electionMaster();
        } catch (Exception e) {
            logger.error("create node failure", e);
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected)
            latch.countDown();
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            try {
                zk.getData(REGISTRY_MASTER_PATH, false, null);
            } catch (KeeperException | InterruptedException e) {
                logger.info("master节点下线，重新选举master");
                electionMaster();
            }
        }
    }

    /**
     * 选举master
     */
    private void electionMaster(){
        try {
            // 每个客户端都创建同一个节点，如果创建成功，则该客户端是master
            zk.create(REGISTRY_MASTER_PATH, nodeName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info("[{}]成为master节点", nodeName);
        } catch (InterruptedException | KeeperException e) {
            try {
                // 如果抛出节点存在的异常，则master已经存在，在该节点上添加watcher
                String masterNode = new String(zk.getData(REGISTRY_MASTER_PATH, false, null));
                logger.info("成为master失败, 当前master节点是：{}", masterNode);
                zk.exists(REGISTRY_MASTER_PATH, true);
            } catch (KeeperException | InterruptedException ex) {
                logger.error("检测master节点失败：{}", ex.getMessage());
            }
        }
    }
}
