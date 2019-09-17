package cn.jsbintask.wxpay.response;

import cn.jsbintask.wxpay.request.WxPayPreOrderRequest;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/6 9:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
public class WxPayPreOrderResponse extends WxPayResponse<WxPayPreOrderRequest> {
    private String tradeType;
    private String prepayId;
    private String codeUrl;
    private String deviceInfo;
}
