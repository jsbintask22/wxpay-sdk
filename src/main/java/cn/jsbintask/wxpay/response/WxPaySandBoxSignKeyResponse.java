package cn.jsbintask.wxpay.response;

import cn.jsbintask.wxpay.request.WxPaySandBoxSignKeyRequest;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/6 14:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
@ToString(callSuper = true)
public class WxPaySandBoxSignKeyResponse extends WxPayResponse<WxPaySandBoxSignKeyRequest> {
    private String sandboxSignkey;

    @Override
    public boolean needCheckSign() {
        return false;
    }
}
