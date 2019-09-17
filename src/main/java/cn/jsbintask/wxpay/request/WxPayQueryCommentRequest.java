package cn.jsbintask.wxpay.request;

import cn.jsbintask.wxpay.WxPayConstants;
import cn.jsbintask.wxpay.response.WxPayResponse;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/10 9:17
 * 拉取订单评论数据
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
@ToString(callSuper = true)
public class WxPayQueryCommentRequest extends AbstractWxPayRawResponseRequest<WxPayResponse> {
    /**
     * 按用户评论时间批量拉取的起始时间，格式为yyyyMMddHHmmss
     */
    private String beginTime;
    private String endTime;
    /**
     * 指定从某条记录的下一条开始返回记录。接口调用成功时，会返回本次查询最后一条数据的offset。商户需要翻页时，应该把本次调用返回的offset 作为下次调用的入参。注意offset是评论数据在微信支付后台保存的索引，未必是连续的
     */
    private Integer offset;
    /**
     * 一次拉取的条数, 最大值是200，默认是200
     */
    private Integer limit;

    @Override
    public Class<? extends WxPayResponse> responseType() {
        return WxPayResponse.class;
    }

    @Override
    public boolean ssl() {
        return true;
    }

    @Override
    public String apiSuffix() {
        return WxPayConstants.QUERY_COMMENT_URL_SUFFIX;
    }
}
