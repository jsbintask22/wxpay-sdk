package cn.jsbintask.wxpay.response;

import cn.jsbintask.wxpay.annotation.WrapperPrefix;
import cn.jsbintask.wxpay.request.WxPayOrderQueryRequest;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/6 16:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
public class WxPayOrderQueryResponse extends WxPayResponse<WxPayOrderQueryRequest> {
    private String tradeType;
    private String prepayId;
    private String deviceInfo;

    /**
     * 用户在商户appid下的唯一标识
     */
    private String openid;

    /**
     * 用户是否关注公众账号，Y-关注，N-未关注
     */
    private String isSubscribe;

    /**
     * SUCCESS—支付成功
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销（付款码支付）
     * USERPAYING--用户支付中（付款码支付）
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    private String tradeState;

    private String bankType;

    /**
     * 订单总金额，单位为分
     */
    private Integer totalFee;

    /**
     * 应结订单金额
     * 当订单使用了免充值型优惠券后返回该参数，应结订单金额=订单金额-免充值优惠券金额。
     */
    private Integer settlementTotalFee;

    /**
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    private String feeType;

    /**
     * 现金支付金额订单现金支付金额，详见支付金额
     */
    private Integer cashFee;

    /**
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    private String cashFeeType;

    /**
     * “代金券”金额<=订单金额，订单金额-“代金券”金额=现金支付金额，详见支付金额
     */
    private Integer couponFee;

    /**
     * 代金券使用数量
     */
    private Integer couponCount;

    /**
     * CASH--充值代金券  代金券类型
     * NO_CASH---非充值优惠券
     * 开通免充值券功能，并且订单使用了优惠券后有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_$0
     */
    @WrapperPrefix("coupon_type_")
    private HashMap<String, String> couponTypeMap;

    /**
     * 代金券ID	coupon_id_$n	否	String(20)	10000 	代金券ID, $n为下标，从0开始编号
     */
    @WrapperPrefix("coupon_id_")
    private ArrayList<String> couponIdMap;

    /**
     * 单个代金券支付金额	coupon_fee_$n	否	Int	100	单个代金券支付金额, $n为下标，从0开始编号
     */
    @WrapperPrefix("coupon_fee_")
    private HashMap<String, Integer> couponFeeMap;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     */
    private String outTradeNo;

    /**
     * 附加数据，原样返回
     */
    private String attach;

    /**
     * 订单支付时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
     */
    private String timeEnd;

    /**
     * 对当前查询订单状态的描述和下一步操作的指引
     */
    private String tradeStateDesc;
}
