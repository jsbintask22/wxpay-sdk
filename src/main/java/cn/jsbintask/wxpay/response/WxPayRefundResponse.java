package cn.jsbintask.wxpay.response;

import cn.jsbintask.wxpay.request.WxPayRefundRequest;
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
public class WxPayRefundResponse extends WxPayResponse<WxPayRefundRequest> {
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
     * 微信退款单号
     */
    private String refundId;

    /**
     * 退款金额
     * 退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
     */
    private Integer refundFee;

    /**
     * 应结退款金额
     * 去掉非充值代金券退款金额后的退款金额，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     */
    private Integer settlementRefundFee;

    /**
     * 标价金额
     * 订单总金额，单位为分，只能为整数，详见支付金额
     */
    private Integer totalFee;

    /**
     * 应结订单金额
     * 去掉非充值代金券金额后的订单总金额，应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     */
    private Integer settlementTotalFee;

    /**
     * 现金支付金额，单位为分，只能为整数，详见支付金额
     */
    private Integer cashFee;


}
