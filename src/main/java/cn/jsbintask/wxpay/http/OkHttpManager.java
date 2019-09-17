package cn.jsbintask.wxpay.http;

import cn.jsbintask.wxpay.WxPayException;
import cn.jsbintask.wxpay.config.WxPayEnv;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Arrays;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/17 15:56
 */
@Slf4j
public class OkHttpManager {
    private OkHttpClient client;
    private OkHttpClient sslClient;

    public OkHttpManager(WxPayEnv config) {
        try {
            init(config);
        } catch (Exception e) {
            throw new WxPayException(String.format("初始OkHttpClient失败：[%s]%s.", e.getClass().getName(), e.getMessage()));
        }
    }

    private void init(WxPayEnv config) throws Exception {
        client = new OkHttpClient.Builder()
                .callTimeout(Duration.ofMillis(config.getHttpConfig().getCallTimeout()))
                .connectTimeout(Duration.ofMillis(config.getHttpConfig().getConnectTimeout()))
                .readTimeout(Duration.ofMillis(config.getHttpConfig().getReadTimeout()))
                .build();

        // ssl
        char[] password = config.getMchId().toCharArray();
        InputStream certStream = config.getCer();
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(certStream, password);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);

        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), trustManagers, new SecureRandom());

        sslClient = new OkHttpClient.Builder()
                .callTimeout(Duration.ofMillis(config.getHttpConfig().getCallTimeout()))
                .connectTimeout(Duration.ofMillis(config.getHttpConfig().getConnectTimeout()))
                .readTimeout(Duration.ofMillis(config.getHttpConfig().getReadTimeout()))
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0])
                .build();
    }

    public OkHttpClient select(boolean ssl) {
        return ssl ? sslClient : client;
    }

    public void destory() {
        client.connectionPool().evictAll();
        client.dispatcher().executorService().shutdown();
    }
}
