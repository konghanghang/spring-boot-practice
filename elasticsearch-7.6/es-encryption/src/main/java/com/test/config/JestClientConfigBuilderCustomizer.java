package com.test.config;

import io.searchbox.client.config.HttpClientConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.annotation.Resource;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

//import org.springframework.boot.autoconfigure.elasticsearch.jest.HttpClientConfigBuilderCustomizer;

/**
 * 由于springboot从2.0.2升级到2.3.5，导致HttpClientConfigBuilderCustomizer已经不存在，所以这里暂时先注释掉，
 * 以后找时间解决
 * todo HttpClientConfigBuilderCustomizer
 */
// @Configuration
// public class JestClientConfigBuilderCustomizer implements HttpClientConfigBuilderCustomizer {
public class JestClientConfigBuilderCustomizer {

    @Resource
    private ElasticsearchProperties elasticsearchProperties;

    // @Override
    public void customize(HttpClientConfig.Builder builder) {
        if (elasticsearchProperties.getCertificatesType().equalsIgnoreCase("pem"))
            return;
        try {
            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(elasticsearchProperties.getPkcsClientFilePath())) {
                keyStore.load(is, "".toCharArray());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            SSLContextBuilder sslContextBuilder = SSLContexts.custom().loadTrustMaterial(keyStore, (x509Certificates, s) -> true);
            final SSLContext sslContext = sslContextBuilder.build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            builder.sslSocketFactory(sslConnectionSocketFactory);
        } catch (Exception e){

        }

    }
}
