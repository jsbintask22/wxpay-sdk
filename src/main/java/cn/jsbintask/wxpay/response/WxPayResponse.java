package cn.jsbintask.wxpay.response;

import cn.jsbintask.wxpay.WxPayConstants;
import cn.jsbintask.wxpay.request.AbstractWxPayRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.ToString;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/5 17:37
 */
@Data
@JacksonXmlRootElement(localName = "xml")
@ToString(callSuper = true)
public class WxPayResponse<T extends AbstractWxPayRequest> {
    /**
     * 通信逻辑
     */
    private String returnCode;
    private String returnMsg;
    private String sign;

    /**
     * 商户信息
     */
    @JacksonXmlProperty(localName = "appid")
    private String appId;
    private String mchId;
    private String nonceStr;

    /**
     * 业务码逻辑
     */
    private String resultCode;
    private String errCode;
    private String errCodeDes;

    @JsonIgnore
    public boolean success() {
        return needCheckSign() ? WxPayConstants.SUCCESS.equals(returnCode) && WxPayConstants.SUCCESS.equals(resultCode) :
                WxPayConstants.SUCCESS.equals(returnCode) && (resultCode == null || WxPayConstants.SUCCESS.equals(resultCode));
    }

    @JsonIgnore
    private Object rawData;

    @JsonIgnore
    public boolean needCheckSign() {
        return true;
    }
}
