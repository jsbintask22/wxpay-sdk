package cn.jsbintask.wxpay.config;

import lombok.Builder;
import lombok.Getter;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/17 16:15
 */
@Builder
@Getter
public class HttpConfig {
    private long readTimeout;
    private long connectTimeout;
    private long callTimeout;

    private static HttpConfig defaultConfig = HttpConfig.builder()
            .callTimeout(4000L)
            .connectTimeout(10000L)
            .readTimeout(30000L)
            .build();

    public static HttpConfig defaultConfig() {
        return defaultConfig;
    }
}
