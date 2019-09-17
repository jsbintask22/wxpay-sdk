package cn.jsbintask.wxpay.request;

import cn.jsbintask.wxpay.WxPayConstants;
import cn.jsbintask.wxpay.response.WxPayResponse;
import cn.jsbintask.wxpay.response.WxPaySandBoxSignKeyResponse;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/6 14:41
 */
@JacksonXmlRootElement(localName = "xml")
public class WxPaySandBoxSignKeyRequest extends AbstractWxPayRequest<WxPaySandBoxSignKeyResponse> {
    @Override
    public Class<? extends WxPayResponse> responseType() {
        return WxPaySandBoxSignKeyResponse.class;
    }

    @Override
    public boolean ssl() {
        return false;
    }

    @Override
    public String apiSuffix() {
        return WxPayConstants.GET_SIGNKEY_URL_SUFFIX;
    }
}
