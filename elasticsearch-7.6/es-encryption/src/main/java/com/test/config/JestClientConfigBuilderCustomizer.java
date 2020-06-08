package com.test.config;

import io.searchbox.client.config.HttpClientConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.autoconfigure.elasticsearch.jest.HttpClientConfigBuilderCustomizer;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
public class JestClientConfigBuilderCustomizer implements HttpClientConfigBuilderCustomizer {

    @Resource
    private ElasticsearchProperties elasticsearchProperties;

    @Override
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
