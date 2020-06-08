package com.test.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ElasticsearchConfig {

    private final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @Resource
    private ElasticsearchProperties elasticsearchProperties;

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
