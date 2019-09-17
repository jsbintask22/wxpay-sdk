package cn.jsbintask.wxpay;

import cn.jsbintask.wxpay.request.AbstractWxPayRequest;
import cn.jsbintask.wxpay.response.WxPayCommonError;
import cn.jsbintask.wxpay.response.WxPayResponse;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/17 14:34
 */
public interface WxPayClient {
    /**
     * 支持域名切换重试
     */
    <T extends WxPayResponse> void execute(AbstractWxPayRequest<T> wxPayRequest,
                                           Consumer<T> responseConsumer,
                                           Consumer<WxPayCommonError> errorConsumer) throws WxPayException;

    /**
     * 支持域名切换重试
     */
    <T extends WxPayResponse> T execute(AbstractWxPayRequest<T> wxPayRequest) throws WxPayException;

    /**
     * 验签
     */
    boolean checkResponseSign(Map<String, String> wxPayResponse);
}
