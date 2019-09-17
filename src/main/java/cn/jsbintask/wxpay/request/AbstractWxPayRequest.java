package cn.jsbintask.wxpay.request;

import cn.jsbintask.wxpay.response.WxPayResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/5 17:07
 */
@Data
public abstract class AbstractWxPayRequest<T extends WxPayResponse> {
    @JacksonXmlProperty(localName = "appid")
    private String appId;
    @JacksonXmlProperty(localName = "mch_id")
    private String mchId;

    private String sign;
    private String signType;

    /**
     * 随机字符串，不长于32位。推荐随机数生成算法
     */
    private String nonceStr;

    @JsonIgnore
    public abstract Class<? extends WxPayResponse> responseType();

    @JsonIgnore
    public abstract boolean ssl();

    @JsonIgnore
    public abstract String apiSuffix();
}
