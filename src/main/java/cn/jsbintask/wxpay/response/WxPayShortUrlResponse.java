package cn.jsbintask.wxpay.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/10 10:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
public class WxPayShortUrlResponse extends WxPayResponse {
    private String shortUrl;
}
