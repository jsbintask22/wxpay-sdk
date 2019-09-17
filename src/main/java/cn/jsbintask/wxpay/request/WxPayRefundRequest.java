package cn.jsbintask.wxpay.request;

import cn.jsbintask.wxpay.WxPayConstants;
import cn.jsbintask.wxpay.response.WxPayRefundResponse;
import cn.jsbintask.wxpay.response.WxPayResponse;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/9 17:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
public class WxPayRefundRequest extends AbstractWxPayRequest<WxPayRefundResponse> {
    /**
     * 微信生成的订单号，在支付通知中有返回
     */
    private String transactionId;

    /**
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     */
    private String outTradeNo;

    /**
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     */
    private String outRefundNo;

    /**
     * 订单总金额，单位为分，只能为整数，详见支付金额
     */
    private Integer totalFee;

    /**
     * 退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
     */
    private Integer refundFee;

    /**
     * 若商户传入，会在下发给用户的退款消息中体现退款原因
     * 注意：若订单退款金额≤1元，且属于部分退款，则不会在退款消息中体现退款原因
     */
    private String refundDesc;

    /**
     * 异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数
     * 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效。
     */
    private String notifyUrl;

    @Override
    public Class<? extends WxPayResponse> responseType() {
        return WxPayRefundResponse.class;
    }

    @Override
    public boolean ssl() {
        return true;
    }

    @Override
    public String apiSuffix() {
        return WxPayConstants.REFUND_URL_SUFFIX;
    }
}
