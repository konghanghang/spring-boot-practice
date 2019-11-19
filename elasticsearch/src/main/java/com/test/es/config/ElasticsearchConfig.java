package com.test.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * @author konghang
 */
@Configuration
public class ElasticsearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.clusterName}")
    private String clusterName;

    @Value("${elasticsearch.pool}")
    private String pool;

    @Value("${elasticsearch.rest.host}")
    private String restHost;

    @Bean(name = "transportClient")
    public TransportClient transportClient() {
        logger.info("Elasticsearch transportClient init begin");
        TransportClient transportClient = null;
        try {
            // 配置信息
            Settings esSetting = Settings.builder()
                    //集群名字
                    .put("cluster.name", clusterName)
                    //增加嗅探机制，找到ES集群
                    .put("client.transport.sniff", true)
                    //增加线程池个数，暂时设为5
                    .put("thread_pool.search.size", Integer.parseInt(pool))
                    .build();
            //配置信息Settings自定义
            transportClient = new PreBuiltTransportClient(esSetting);

            String[] hosts = host.split(",");
            for (String s : hosts) {
                String[] esIpPort = s.split(":");
                TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(esIpPort[0]),
                        Integer.valueOf(esIpPort[1]));
                transportClient.addTransportAddresses(transportAddress);
                logger.info("add elasticsearch ip:{}, port:{}", esIpPort[0], esIpPort[1]);
            }
            logger.info("Elasticsearch transportClient init end");
        } catch (Exception e) {
            logger.error("Elasticsearch TransportClient create error!!", e);
        }
        return transportClient;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        logger.info("Elasticsearch init high level client begin");
        HttpHost[] httpHosts = getHosts();
        //相比于Transport Client，此处不需要配置cluster.name、client.transport.sniff
        RestHighLevelClient highLevelClient = new RestHighLevelClient(
                RestClient.builder(httpHosts));
        logger.info("Elasticsearch init high level client end");
        return highLevelClient;
    }

    @Bean
    public RestClient restLowLevelClient(){
        logger.info("Elasticsearch init low level client begin");
        HttpHost[] httpHosts = getHosts();
        RestClientBuilder builder = RestClient.builder(httpHosts);
        RestClient restClient = builder.build();
        logger.info("Elasticsearch init low level client end");
        return restClient;
    }

    private HttpHost[] getHosts() {
        String[] hosts = host.split(",");
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < hosts.length; i++) {
            String[] ipAndPort = hosts[i].split(":");
            httpHosts[i] = new HttpHost(ipAndPort[0], Integer.valueOf(ipAndPort[1]), "http");
        }
        return httpHosts;
    }
}
