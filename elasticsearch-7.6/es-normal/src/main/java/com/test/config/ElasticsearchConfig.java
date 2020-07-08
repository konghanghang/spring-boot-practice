package com.test.config;

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
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.net.InetAddress;

@Configuration
public class ElasticsearchConfig {

    private final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @Resource
    private ElasticsearchProperties elasticsearchProperties;

    @Bean(name = "transportClient")
    public TransportClient transportClient() {
        logger.info("Elasticsearch transportClient init begin");
        TransportClient transportClient = null;
        try {
            // 配置信息
            Settings esSetting = Settings.builder()
                    //集群名字
                    .put("cluster.name", elasticsearchProperties.getClusterName())
                    //增加嗅探机制，找到ES集群
                    .put("client.transport.sniff", true)
                    //增加线程池个数，暂时设为5
                    .put("thread_pool.search.size", 5)
                    .build();
            //配置信息Settings自定义
            transportClient = new PreBuiltTransportClient(esSetting);

            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(elasticsearchProperties.getHost()),
                    9300);
            transportClient.addTransportAddresses(transportAddress);
            logger.info("Elasticsearch transportClient init end");
        } catch (Exception e) {
            logger.error("Elasticsearch TransportClient create error!!", e);
        }
        return transportClient;
    }

    @Bean
    public RestClient getRestClient() {
        logger.info("init RestClient");
        RestClientBuilder builder = RestClient.builder(new HttpHost(elasticsearchProperties.getHost(), elasticsearchProperties.getHttpPort(), elasticsearchProperties.getSchema()));
        RestClient restClient = builder.build();
        return restClient;
    }

    @Bean
    @ConditionalOnBean(RestClient.class)
    public RestHighLevelClient restHighLevelClient() {
        logger.info("init RestHighLevelClient");
        RestClientBuilder builder = RestClient.builder(new HttpHost(elasticsearchProperties.getHost(), elasticsearchProperties.getHttpPort(), elasticsearchProperties.getSchema()));
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        return restHighLevelClient;
    }

}
