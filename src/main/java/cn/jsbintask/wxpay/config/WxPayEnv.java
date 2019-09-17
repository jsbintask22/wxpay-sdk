package cn.jsbintask.wxpay.config;

import cn.jsbintask.wxpay.WxPayConstants;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/5 9:54
 */
@Data
@AllArgsConstructor
public class WxPayEnv {
    private String appId;
    private String mchId;
    private String apiKey;
    private String cerPath;
    private boolean isSandEnv;
    private String notifyUrl;
    private boolean debugRequestBody;
    private HttpConfig httpConfig;

    public boolean debugRequestBody() {
        return this.debugRequestBody;
    }

    public InputStream getCer() {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(cerPath);
        if (stream == null) {
            stream = this.getClass().getResourceAsStream(cerPath);
        }
        if (stream == null) {
            throw new IllegalStateException("wxpay ssl cert is not found.");
        }
        return stream;
    }

    public String envUri() {
        return isSandEnv ? WxPayConstants.SANDBOX_SUFFIX : "";
    }

    public String signType() {
        return isSandEnv ? WxPayConstants.MD5 : WxPayConstants.HMACSHA256;
    }
}
