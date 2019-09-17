package cn.jsbintask.wxpay.request;

import cn.jsbintask.wxpay.WxPayConstants;
import cn.jsbintask.wxpay.response.WxPayResponse;
import cn.jsbintask.wxpay.response.WxPayShortUrlResponse;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/10 10:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
public class WxPayShortUrlRequest extends AbstractWxPayRequest<WxPayShortUrlResponse> {
    private String longUrl;

    @Override
    public Class<? extends WxPayResponse> responseType() {
        return WxPayShortUrlResponse.class;
    }

    @Override
    public boolean ssl() {
        return false;
    }

    @Override
    public String apiSuffix() {
        return WxPayConstants.SHORTURL_URL_SUFFIX;
    }
}
