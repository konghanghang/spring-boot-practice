package com.test.zk.api;

import java.util.List;
import javax.annotation.Resource;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private ZkComponent zkComponent;

    @Test
    public void createPath(){
        zkComponent.createPath("/my/zk/test/node", "node1", CreateMode.PERSISTENT, CreateMode.PERSISTENT_SEQUENTIAL);
    }

    @Test
    public void getChildren(){
        String path = "/my/zk/test";
        List<String> children = zkComponent.getChildren(path);
        children.stream().forEach(child -> System.out.println(zkComponent.getData(path + "/" + child)));
    }

    @Test
    public void setData() {
        boolean oneTwoThree = zkComponent.setData("/my/zk/test/node0000000001", "oneTwoThree");
    }

}
