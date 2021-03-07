package com.kyn.rabbitmq_study.demo1.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @author Kangyanan
 * @Description: 发送HTTP请求封装工具类
 * @date 2021/3/1
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public final static String DEFAULT_ENCODING = "UTF-8";
    private static PoolingHttpClientConnectionManager connManager = null;
    private static CloseableHttpClient httpClient = null;
    private static int timeout = 30000;

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        ConnectionSocketFactory socketFactory = new PlainConnectionSocketFactory();
        registryBuilder.register("http", socketFactory);
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            TrustStrategy trustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            };

            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(keyStore, trustStrategy).build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"},
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            Registry<ConnectionSocketFactory> registry = registryBuilder
                    .register("https", sslSF).build();
            connManager = new PoolingHttpClientConnectionManager(registry);
            SocketConfig socketConfig = SocketConfig.custom().setRcvBufSize(2048).setSndBufSize(2048)
                    .setSoKeepAlive(true).setTcpNoDelay(true).setSoTimeout(2 * 60 * 1000).build();
            connManager.setDefaultSocketConfig(socketConfig);

            // Create message constraints
            MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
                    .setMaxLineLength(2000).build();
            // Create connection configuration
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
                    .setMessageConstraints(messageConstraints).build();
            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setMaxTotal(60000);
            connManager.setDefaultMaxPerRoute(120);

            httpClient = HttpClients.custom().setConnectionManager(connManager).build();
        } catch (KeyStoreException | KeyManagementException | NoSuchAlgorithmException e) {
            logger.error("实例化HttpClient异常:", e);
        }
    }



    /**
     * 发送post请求，content-type为application/json,参数为json数据
     *
     * @param url
     * @param jsonValue
     * @param header
     * @param encoding
     * @return
     */
    public static String postJsonBody(String url, String jsonValue, Map<String, String> header, String encoding) {
        HttpPost post = new HttpPost(url);
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        try {
            post.setHeader("Content-Type", "application/json");
            if (header != null && !header.isEmpty()) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    post.setHeader(entry.getKey(), entry.getValue());
                }
            }
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout).setExpectContinueEnabled(false).build();
            post.setConfig(requestConfig);

            post.setEntity(new StringEntity(jsonValue, encoding));
            logger.debug("[HttpUtils Post] begin invoke url:{} , params:{}", url, jsonValue);
            CloseableHttpResponse response = httpClient.execute(post);
            try {
                HttpEntity entity = response.getEntity();
                try {
                    if (entity != null) {
                        String str = EntityUtils.toString(entity, encoding);
                        logger.debug("[HttpUtils Post]Debug response, url :{} , response string :{}", url, str);
                        return str;
                    }
                } finally {
                    if (entity != null && entity.getContent() != null) {
                        entity.getContent().close();
                    }
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException", e);
        } catch (SocketTimeoutException e) {
            logger.error("[HttpUtils POST] post invoke error timeout, url:{}", url, e);
        } catch (Exception e) {
            logger.error("[HttpUtils POST] post invoke error, url:{}", url, e);
        } finally {
            post.releaseConnection();
        }
        return "";
    }

}
