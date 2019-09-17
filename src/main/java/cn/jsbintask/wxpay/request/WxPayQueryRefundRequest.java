package cn.jsbintask.wxpay.request;

import cn.jsbintask.wxpay.WxPayConstants;
import cn.jsbintask.wxpay.response.WxPayQueryRefundResponse;
import cn.jsbintask.wxpay.response.WxPayResponse;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/9 17:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
@ToString(callSuper = true)
public class WxPayQueryRefundRequest extends AbstractWxPayRequest<WxPayQueryRefundResponse> {

    /**
     * 微信订单号查询的优先级是： refund_id > out_refund_no > transaction_id > out_trade_no
     */
    private String transactionId;
    private String outTradeNo;
    /**
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     */
    private String outRefundNo;
    /**
     * 微信生成的退款单号，在申请退款接口有返回
     */
    private String refundId;

    /**
     * 偏移量	offset	否	Int	15
     * 偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录
     */
    private Integer offset;

    @Override
    public Class<? extends WxPayResponse> responseType() {
        return WxPayQueryRefundResponse.class;
    }

    @Override
    public boolean ssl() {
        return false;
    }

    @Override
    public String apiSuffix() {
        return WxPayConstants.REFUNDQUERY_URL_SUFFIX;
    }
}
