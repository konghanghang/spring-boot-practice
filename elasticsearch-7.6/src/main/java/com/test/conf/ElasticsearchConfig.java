package com.test.conf;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

@Configuration
public class ElasticsearchConfig {

    private final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @Resource
    private ElasticsearchProperties elasticsearchProperties;

    @Bean
    public TransportClient getTransportClient() throws UnknownHostException, URISyntaxException {
        logger.info("init TransportClient");
        String path = Paths.get(getClass().getClassLoader().getResource(elasticsearchProperties.getPkcsTransportFilePath()).toURI()).toString();
        TransportClient client = new PreBuiltXPackTransportClient(Settings.builder()
                .put("cluster.name", elasticsearchProperties.getClusterName())
                // missing authentication credentials for action
                .put("xpack.security.user", String.format("%s:%s", elasticsearchProperties.getUsername(), elasticsearchProperties.getPassword()))
                .put("xpack.security.transport.ssl.enabled", true)
                .put("xpack.security.transport.ssl.verification_mode", "certificate")
                .put("xpack.security.transport.ssl.keystore.path", path)
                .put("xpack.security.transport.ssl.truststore.path", path)
                .build()).addTransportAddress(new TransportAddress(InetAddress.getByName(elasticsearchProperties.getHost()), elasticsearchProperties.getTcpPort()));
        return client;
    }

    @Bean
    // @ConditionalOnExpression("${es.properties.certificates_type}==1&&${es.properties.certificates_type:true}")
    // @ConditionalOnExpression("elasticsearchProperties.getCertificatesType().equals('pkcs')")
    @ConditionalOnExpression("'${es.properties.certificates_type}'.equals('pkcs')")
    public RestClient getRestClient() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        logger.info("init RestClient, type:pkcs");
        RestClientBuilder builder = initRestClientBuilder("pkcs");
        RestClient restClient = builder.build();
        return restClient;
    }

    @Bean
    @ConditionalOnExpression("'${es.properties.certificates_type}'.equals('pem')")
    public RestClient getRestClientPem() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        logger.info("init RestClient, type:pem");
        RestClientBuilder builder = initRestClientBuilder(elasticsearchProperties.getCertificatesType());
        RestClient restClient = builder.build();
        return restClient;
    }

    @Bean
    @ConditionalOnBean(RestClient.class)
    public RestHighLevelClient restHighLevelClient() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        logger.info("init RestHighLevelClient");
        RestClientBuilder builder = initRestClientBuilder(elasticsearchProperties.getCertificatesType());
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        return restHighLevelClient;
    }

    private RestClientBuilder initRestClientBuilder(String type) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, KeyManagementException {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticsearchProperties.getUsername(), elasticsearchProperties.getPassword()));
        KeyStore keyStore = getKeyStoreByType(type);
        SSLContextBuilder sslContextBuilder = SSLContexts.custom().loadTrustMaterial(keyStore, (x509Certificates, s) -> true);
        final SSLContext sslContext = sslContextBuilder.build();
        RestClientBuilder builder = RestClient.builder(new HttpHost(elasticsearchProperties.getHost(), elasticsearchProperties.getHttpPort(), elasticsearchProperties.getSchema()));
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setSSLContext(sslContext);
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpClientBuilder;
        });
        return builder;
    }

    private KeyStore getKeyStoreByType(String type) throws KeyStoreException, CertificateException, NoSuchAlgorithmException {
        KeyStore keyStore = null;
        if (elasticsearchProperties.getCertificatesType().equalsIgnoreCase(type)){
            keyStore = KeyStore.getInstance("pkcs12");
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(elasticsearchProperties.getPkcsClientFilePath())) {
                keyStore.load(is, "".toCharArray());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            Certificate trustedCa;
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(elasticsearchProperties.getPemFilePath())) {
                trustedCa = factory.generateCertificate(is);
                keyStore = KeyStore.getInstance("pkcs12");
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", trustedCa);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return keyStore;
    }

}
