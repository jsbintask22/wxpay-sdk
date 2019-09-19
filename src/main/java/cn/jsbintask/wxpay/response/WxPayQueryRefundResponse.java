package cn.jsbintask.wxpay.response;

import cn.jsbintask.wxpay.annotation.WrapperPrefix;
import cn.jsbintask.wxpay.request.WxPayQueryRefundRequest;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/10 10:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
@ToString(callSuper = true)
public class WxPayQueryRefundResponse extends WxPayResponse<WxPayQueryRefundRequest> {

    /**
     * 订单总退款次数	total_refund_count	否	Int	35
     * 订单总共已发生的部分退款次数，当请求参数传入offset后有返回
     */
    private Integer totalRefundCount;

    /**
     * 微信订单号
     */
    private String transactionId;

    /**
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一
     */
    private String outTradeNo;

    /**
     * 订单总金额，单位为分，只能为整数，详见支付金额
     */
    private Integer totalFee;

    /**
     * 应结订单金额	settlement_total_fee	否
     * Int	100
     * 当订单使用了免充值型优惠券后返回该参数，应结订单金额=订单金额-免充值优惠券金额。
     */
    private Integer settlementTotalFee;

    /**
     * 现金支付金额，单位为分，只能为整数，详见支付金额
     */
    private Integer cashFee;

    /**
     * 当前返回退款笔数
     */
    private Integer refundCount;


    /**
     * 商户退款单号	out_refund_no_$n	是	String(64)	1217752501201407033233368018
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     */
    @WrapperPrefix("out_refund_no_")
    private HashMap<String, String> outRefundNoMap;

    /**
     * 微信退款单号	refund_id_$n	是	String(32)	1217752501201407033233368018	微信退款单号
     */
    @WrapperPrefix("refund_id_")
    private HashMap<String, String> refundIdMap;

    /**
     * 退款渠道	refund_channel_$n	否	String(16)	ORIGINAL
     * ORIGINAL—原路退款
     * BALANCE—退回到余额
     * OTHER_BALANCE—原账户异常退到其他余额账户
     * OTHER_BANKCARD—原银行卡异常退到其他银行卡
     */
    @WrapperPrefix("refund_channel_")
    private HashMap<String, String> refundChannelMap;

    /**
     * 申请退款金额	refund_fee_$n	是	Int	100	退款总金额,单位为分,可以做部分退款
     */
    @WrapperPrefix("refund_fee_")
    private HashMap<String, Integer> refundFeeMap;

    /**
     * 退款金额	settlement_refund_fee_$n	否	Int	100	退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     */
    @WrapperPrefix("settlement_refund_fee_")
    private HashMap<String, Integer> settlementRefundFeeMap;

    /**
     * 代金券类型	coupon_type_$n_$m	否	String(8)	CASH
     * CASH--充值代金券
     * NO_CASH---非充值优惠券
     * <p>
     * 开通免充值券功能，并且订单使用了优惠券后有返回（取值：CASH、NO_CASH）。$n为下标,$m为下标,从0开始编号，举例：coupon_type_$0_$1
     */
    @WrapperPrefix(value = "coupon_type_", variables = 2)
    private HashMap<String, String> couponTypeMap;

    /**
     * 总代金券退款金额	coupon_refund_fee_$n	否	Int	100
     * 代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     */
    @WrapperPrefix("coupon_refund_fee_")
    private HashMap<String, Integer> couponRefundFeeMap;

    /**
     * 退款代金券使用数量	coupon_refund_count_$n	否	Int	1	退款代金券使用数量 ,$n为下标,从0开始编号
     */
    @WrapperPrefix("coupon_refund_count_")
    private HashMap<String, Integer> couponRefundCountMap;

    /**
     * 退款代金券ID	coupon_refund_id_$n_$m	否	String(20)	10000 	退款代金券ID, $n为下标，$m为下标，从0开始编号
     */
    @WrapperPrefix(value = "coupon_refund_id_", variables = 2)
    private HashMap<String, String> couponRefundIdMap;


    /**
     * 单个代金券退款金额	coupon_refund_fee_$n_$m	否	Int	100	单个退款代金券支付金额, $n为下标，$m为下标，从0开始编号
     */
    @WrapperPrefix(value = "coupon_refund_fee_", variables = 2)
    private HashMap<String, Integer> couponRefundFeeMapMap;


    /**
     * 退款状态	refund_status_$n	是	String(16)	SUCCESS
     * 退款状态：
     * <p>
     * SUCCESS—退款成功
     * <p>
     * REFUNDCLOSE—退款关闭。
     * <p>
     * PROCESSING—退款处理中
     * <p>
     * CHANGE—退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，
     * 可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款。$n为下标，从0开始编号。
     */
    @WrapperPrefix("refund_status_")
    private HashMap<String, String> refundStatusMap;

    /**
     * 退款资金来源	refund_account_$n	否	String(30)	REFUND_SOURCE_RECHARGE_FUNDS
     * REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款/基本账户
     * <p>
     * REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款
     * <p>
     * $n为下标，从0开始编号。
     */
    @WrapperPrefix("refund_account_")
    private HashMap<String, String> refundAccountMap;

    /**
     * 退款入账账户	refund_recv_accout_$n	是	String(64)	招商银行信用卡0403
     * 取当前退款单的退款入账方
     * 1）退回银行卡：
     * <p>
     * {银行名称}{卡类型}{卡尾号}
     * <p>
     * 2）退回支付用户零钱:
     * <p>
     * 支付用户零钱
     * <p>
     * 3）退还商户:
     * <p>
     * 商户基本账户
     * <p>
     * 商户结算银行账户
     * <p>
     * 4）退回支付用户零钱通:
     * <p>
     * 支付用户零钱通
     */
    @WrapperPrefix("refund_recv_accout_")
    private HashMap<String, String> refundRecvAccountMap;

    /**
     * 退款成功时间	refund_success_time_$n	否	String(20)	2016-07-25 15:26:26
     * 退款成功时间，当退款状态为退款成功时有返回。$n为下标，从0开始编号。
     */
    @WrapperPrefix("refund_success_time_")
    private HashMap<String, String> refundSuccessTimeMap;

}
