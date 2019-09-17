package cn.jsbintask.wxpay.request;

import cn.jsbintask.wxpay.WxPayConstants;
import cn.jsbintask.wxpay.response.WxPayResponse;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/9 16:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
@ToString(callSuper = true)
public class WxPayDownloadBillFundflowRequest extends AbstractWxPayRawResponseRequest<WxPayResponse> {
    private String billDate;
    private String accountType;
    private String tarType;

    @Override
    public Class<? extends WxPayResponse> responseType() {
        return WxPayResponse.class;
    }

    @Override
    public boolean ssl() {
        return true;
    }

    @Override
    public String apiSuffix() {
        return WxPayConstants.DOWNLOADFUNDFLOW_URL_SUFFIX;
    }
}
