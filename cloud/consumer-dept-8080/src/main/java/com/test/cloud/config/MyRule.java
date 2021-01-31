package com.test.cloud.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 自定义规则，过滤8001端口
 */
@Slf4j
public class MyRule extends AbstractLoadBalancerRule {
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        return this.choose(this.getLoadBalancer(), o);
    }

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        } else {
            Server server = null;

            while(server == null) {
                if (Thread.interrupted()) {
                    return null;
                }

                List<Server> upList = lb.getReachableServers().stream().filter(s -> s.getPort() == 8001).collect(Collectors.toList());
                List<Server> allList = lb.getAllServers().stream().filter(s -> s.getPort() == 8001).collect(Collectors.toList());
                int serverCount = allList.size();
                if (serverCount == 0) {
                    return null;
                }

                int index = ThreadLocalRandom.current().nextInt(serverCount);
                server = upList.get(index);
                if (server != null) {
                    if (server.isAlive()) {
                        log.info("select port:{}", server.getPort());
                        return server;
                    }

                    server = null;
                }
                Thread.yield();
            }
            log.info("select port:{}", server.getPort());
            return server;
        }
    }
}
