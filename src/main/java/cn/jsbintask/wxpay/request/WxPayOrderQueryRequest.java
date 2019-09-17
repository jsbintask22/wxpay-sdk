package cn.jsbintask.wxpay.request;

import cn.jsbintask.wxpay.WxPayConstants;
import cn.jsbintask.wxpay.response.WxPayOrderQueryResponse;
import cn.jsbintask.wxpay.response.WxPayResponse;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/6 16:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
public class WxPayOrderQueryRequest extends AbstractWxPayRequest<WxPayOrderQueryResponse> {
    /**
     * 微信的订单号，建议优先使用
     */
    private String transactionId;
    /**
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。 详见商户订单号
     */
    private String outTradeNo;

    @Override
    public Class<? extends WxPayResponse> responseType() {
        return WxPayOrderQueryResponse.class;
    }

    @Override
    public boolean ssl() {
        return false;
    }

    @Override
    public String apiSuffix() {
        return WxPayConstants.ORDERQUERY_URL_SUFFIX;
    }
}
