package com.test;

public interface INodeRegistry {

    /**
     * 注册节点
     * @param registerPath  注册节点路径
     * @param nodeAddress   注册节点id
     */
    void register(String registerPath, String nodeAddress);

}
