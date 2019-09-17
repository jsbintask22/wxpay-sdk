package cn.jsbintask.wxpay.request;

import cn.jsbintask.wxpay.WxPayConstants;
import cn.jsbintask.wxpay.response.WxPayResponse;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/16 17:03
 * 商户在调用微信支付提供的相关接口时，会得到微信支付返回的相关信息以及获得整个接口的响应时间。
 * 为提高整体的服务水平，协助商户一起提高服务质量，微信支付提供了相关接口调用耗时和返回信息的主动上报接口，
 * 微信支付可以根据商户侧上报的数据进一步优化网络部署，完善服务监控，和商户更好的协作为用户提供更好的业务体验。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "xml")
public class WxPayReportRequest extends AbstractWxPayRequest<WxPayReportRequest.WxPayReportResponse> {
    private String deviceInfo;
    private String interfaceUrl;
    @JacksonXmlProperty(localName = "execute_time_")
    private Integer executeTime;
    private String returnCode;
    private String returnMsg;
    private String resultCode;
    private String errCode;
    private String errCodeDesc;
    private String outTradeNo;
    private String userIp;
    /**
     * 商户上报时间	time	否	String(14)	20091227091010
     * 系统时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则
     */
    private String time;


    @Override
    public Class<? extends WxPayResponse> responseType() {
        return WxPayReportResponse.class;
    }

    @Override
    public boolean ssl() {
        return false;
    }

    @Override
    public String apiSuffix() {
        return WxPayConstants.REPORT_URL_SUFFIX;
    }

    public static class WxPayReportResponse extends WxPayResponse<WxPayReportRequest> {
        @Override
        public boolean needCheckSign() {
            return false;
        }
    }
}
