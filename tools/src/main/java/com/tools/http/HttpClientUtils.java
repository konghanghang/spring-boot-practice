package com.tools.http;

import cn.hutool.core.util.StrUtil;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

/**
 * see -http://blog.csdn.net/wangpeng047/article/details/19624529
 * see -http://blog.csdn.net/leeo1010/article/details/41801509
 * see -http://blog.51cto.com/linhongyu/1538672
 * see -http://www.yeetrack.com/?p=773
 * @author yslao@outlook.com
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * 连接超时时间，默认10秒(数据传输过程中数据包之间间隔的最大时间)
     */
    private static final int SOCKET_TIMEOUT = 10000;
    /**
     * 传输超时时间，默认30秒(连接建立时间，三次握手完成时间)
     */
    private static final int CONNECT_TIMEOUT = 30000;
    /**
     * 从连接池获取连接的超时时间，可以想象下数据库连接池
     */
    private static final int CONNECTION_REQUEST_TIMEOUT = 10000;

    /**
     * 构建get请求url
     * @param host 主机
     * @param path 请求路径
     * @param params 参数
     * @return body
     * @throws UnsupportedEncodingException 不支持的编码异常
     */
    private static String buildUrl(String host, String path, Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StrUtil.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != params) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : params.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StrUtil.isBlank(query.getKey()) && !StrUtil.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StrUtil.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StrUtil.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    /**
     * 发送get请求
     * @param url 请求url
     * @return body
     */
    public static String httpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();
        httpGet.setConfig(requestConfig);
        //如果不设置user-agent,用默认值User-Agent: Apache-HttpClient/4.5.2 (Java/1.8.0_144)可能会导致访问不成功。
        httpGet.setHeader(HTTP.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36");
        try(CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return responseHandle(response);
        } catch (IOException e) {
            logger.error("http get error", e);
        }
        return null;
    }

    /**
     * post请求发送,请求参数为formData
     * @param url 请求url
     * @param para 请求参数
     * @return body
     */
    public static String httpPostForm(String url, Map<String, Object> para) {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader(HTTP.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36");
        CloseableHttpResponse response = null;
        //拼接参数
        List<NameValuePair> list = new ArrayList<>();
        for(Map.Entry<String, Object> entry: para.entrySet()){
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            response = httpClient.execute(httpPost);
            return responseHandle(response);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException", e);
        } catch (IOException e){
            logger.error("IOException", e);
        }
        return null;
    }


    /**
     * 发送post请求,请求参数为json字符串
     * @param url 请求url
     * @param postData 请求参数
     * @return body
     */
    public static String httpPostJson(String url, String postData) {
        return httpPost(url, postData, "application/json");
    }

    /**
     * 发送post请求,请求参数为json字符串
     * @param url 请求的url
     * @param postData 请求的数据,json字符串
     * @param contentType 可以取值application/json,text/xml等
     * @return body
     */
    public static String httpPost(String url, String postData, String contentType) {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            StringEntity entity = new StringEntity(postData,"UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType(contentType);
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            return responseHandle(response);
        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
        return null;
    }

    /**
     * 发送post请求,请求参数为json字符串
     * @param client 请求客户端
     * @param url 请求的url
     * @param postData 请求的数据,json字符串
     * @param contentType 可以取值application/json,text/xml等
     * @return body
     */
    public static String httpPost(CloseableHttpClient client, String url, String postData, String contentType) {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            StringEntity entity = new StringEntity(postData,"UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType(contentType);
            httpPost.setEntity(entity);
            response = client.execute(httpPost);
            return responseHandle(response);
        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        } finally {
            try {
                if (Objects.nonNull(client))
                    client.close();
            } catch (IOException e) {
                logger.error("IOException", e);
            }
        }
        return null;
    }

    /**
     * 发送post请求， 获取返回的inputStream
     * @param url 请求的url
     * @param postData 请求参数
     * @return body
     */
    public static byte[] httpPostGetBytes(String url, String postData) {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            StringEntity entity = new StringEntity(postData,"UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType(MediaType.APPLICATION_JSON.getType());
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            try{
                HttpEntity httpEntity = response.getEntity();
                if(httpEntity != null){
                    byte[] bytes = EntityUtils.toByteArray(httpEntity);
                    //消耗掉response
                    EntityUtils.consume(httpEntity);
                    return bytes;
                }
            }finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
        return null;
    }

    /**
     * 创建SSLClient,信任所有证书
     * @return {@link CloseableHttpClient}
     */
    public static CloseableHttpClient createSSLClientTruestAllCert(){
        try {
            try {
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                    //信任所有证书
                    @Override
                    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        return true;
                    }
                }).build();
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
                return HttpClients.custom().setSSLSocketFactory(sslsf).build();
            } catch (KeyManagementException e) {
                logger.error("KeyManagementException", e);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException", e);
        } catch (KeyStoreException e) {
            logger.error("KeyStoreException", e);
        }
        return null;
    }

    /**
     * 创建SSLClient,需要加载证书
     * @param certificatePath 证书路径
     * @param certificatePassword 证书密码
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient createSSLClient(String certificatePath, String certificatePassword){
        return createSSLClient(certificatePath, certificatePassword, KeyStore.getDefaultType());
    }

    /**
     * 创建SSLClient,需要加载证书
     * @param certificatePath 证书存放的路径
     * @param certificatePassword 证书的密码
     * @param keyStoreType keystore的类型(PKCS12等)
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient createSSLClient(String certificatePath, String certificatePassword, String keyStoreType){
        try {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            FileInputStream inputStream = new FileInputStream(new File(certificatePath));
            try {
                //加载证书和密码
                keyStore.load(inputStream, certificatePassword.toCharArray());
            } catch (NoSuchAlgorithmException e) {
                logger.error("NoSuchAlgorithmException", e);
            } catch (CertificateException e) {
                logger.error("CertificateException", e);
            } finally {
                inputStream.close();
            }
            // 相信自己的CA和所有自签名的证书
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, certificatePassword.toCharArray())
                    .build();

            // 只允许使用TLSv1协议
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
                    new String[] { "TLSv1" },
                    null,
                    new DefaultHostnameVerifier());
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyStoreException e) {
            logger.error("KeyStoreException", e);
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException", e);
        } catch (KeyManagementException e) {
            logger.error("KeyManagementException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        } catch (UnrecoverableKeyException e) {
            logger.error("UnrecoverableKeyException", e);
        }
        return null;
    }

    /**
     * 统一处理httpclient请求返回的数据,转为JSONObject
     * @param response 请求响应
     * @return body
     * @throws IOException io异常
     */
    public static String responseHandle(CloseableHttpResponse response) throws IOException {
        String result = null;
        //if(response.getStatusLine().getStatusCode() == 200){
        try{
            HttpEntity httpEntity = response.getEntity();
            if(httpEntity != null){
                result = EntityUtils.toString(httpEntity, "UTF-8");
                //消耗掉response
                EntityUtils.consume(httpEntity);
            }
        }finally {
            response.close();
        }
        return result;
    }

    /**
     * 上传文件
     * MultipartEntityBuilder 默认contentType为 multipart_form_data
     * @param url 上传地址
     * @param path 要上传的文件路径
     * @param token token
     * @return body
     */
    public static String postFile(String url, String path,String token) {
        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httppost = new HttpPost(url);
            File file = new File(path);
            //FileBody bin = new FileBody(file);
            //StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    //.addPart("file", bin)
                    .setCharset(Charset.forName("UTF-8"))
                    //.setContentType(ContentType.MULTIPART_FORM_DATA)
                    .addBinaryBody("file", file)
                    .addTextBody("token", token)
                    .addTextBody("filename", file.getName());
            httppost.setEntity(builder.build());
            CloseableHttpResponse response = httpclient.execute(httppost);
            return responseHandle(response);
        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
        return null;
    }

    /**
     * 上传文件
     * MultipartEntityBuilder 默认contentType为 multipart_form_data
     * @param url 上传地址
     * @param content 要上传内容
     * @param params post参数
     * @return body
     */
    public static String postBytes(String url, byte[] content, Map<String, String> params) {
        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httppost = new HttpPost(url);
            //FileBody bin = new FileBody(file);
            //StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    // .addPart("file", bin)
                    // .setCharset(StandardCharsets.UTF_8)
                    .setContentType(ContentType.create("multipart/form-data", (Charset) null))
                    .addBinaryBody("file", content, ContentType.DEFAULT_BINARY, "xxx.xlsx");
            if (Objects.nonNull(params)) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.create("text/plain", StandardCharsets.UTF_8));
                }
                // builder.addTextBody("token", token).addTextBody("filename", UUID.randomUUID().toString());
            }
            httppost.setEntity(builder.build());
            CloseableHttpResponse response = httpclient.execute(httppost);
            return responseHandle(response);
        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
        return null;
    }

    /**
     * 七牛云 post Base64File
     * @param url 请求url
     * @param base64File base64格式的文件
     * @param token token
     * @return code
     */
    public static String postBase64File(String url, String base64File,String token) {
        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization","UpToken " + token);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/octet-stream");
            StringEntity entity = new StringEntity(base64File,"UTF-8");
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            return responseHandle(response);
        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
        return null;
    }

}
