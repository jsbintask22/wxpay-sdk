package cn.jsbintask.wxpay.request;

import cn.jsbintask.wxpay.WxPayConstants;
import cn.jsbintask.wxpay.response.WxPayPreOrderResponse;
import cn.jsbintask.wxpay.response.WxPayResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/5 10:21
 * 微信统一下单 pojo
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
public class WxPayPreOrderRequest extends AbstractWxPayRequest<WxPayPreOrderResponse> {
    /**
     * 商品描述 浏览器打开的网站主页title名 -商品概述	腾讯充值中心-QQ会员充值
     * max: 128
     */
    private String body;

    /**
     * 非必传
     * 商品详情
     * max: 6000
     */
    @JacksonXmlCData
    private String detail;

    /**
     * 非必传
     * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
     * max: 127
     */
    @JacksonXmlCData
    private String attach;

    /**
     * 系统订单号，请使用 订单号生成类生成
     */
    private String outTradeNo;

    /**
     * 非必传
     * 自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
     */
    private String deviceInfo;

    /**
     * 订单总金额  单位 元
     * <b>请务必 保留到小数点后两位 如： 1.00  .088 </b>
     */
    private Integer totalFee;

    /**
     * 终端IP 客户扫码时候的 ip地址， 请使用 HttpUtils.getIp 获取
     * 支持IPV4和IPV6两种格式的IP地址。用户的客户端IP
     */
    private String spbillCreateIp;

    /**
     * 非必传
     * 订单优惠标记，使用代金券或立减优惠功能时需要的参数，说明详见代金券或立减优惠
     */
    private String goodsTag;

    /**
     * 非必传
     * 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
     * 同支付宝地址一样
     */
    private String notifyUrl;

    /**
     * 当前请传 固定 NATIVE
     * JSAPI -JSAPI支付
     * NATIVE -Native支付
     * APP -APP支付
     */
    private String tradeType;

    /**
     * trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义
     * 对应 product 的 product_code
     */
    private String productId;

    /**
     * 非必传
     * 限制支付方式
     * 上传此参数no_credit--可限制用户不能使用信用卡支付
     */
    private String limitPay;

    /**
     * 交易起始时间	time_start	否	String(14)	20091225091010
     * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
     */
    private String timeStart;

    /**
     * 交易结束时间	time_expire	否	String(14)	20091227091010
     * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
     * 订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，
     * 所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。
     *
     * 建议：最短失效时间间隔大于1分钟
     */
    private String timeExpire;

    @Override
    @JsonIgnore
    public Class<? extends WxPayResponse> responseType() {
        return WxPayPreOrderResponse.class;
    }

    @Override
    public boolean ssl() {
        return false;
    }

    @Override
    public String apiSuffix() {
        return WxPayConstants.UNIFIEDORDER_URL_SUFFIX;
    }
}
