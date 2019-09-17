package cn.jsbintask.wxpay;

import cn.jsbintask.wxpay.config.WxPayEnv;
import cn.jsbintask.wxpay.config.HttpConfig;
import cn.jsbintask.wxpay.request.WxPayPreOrderRequest;
import cn.jsbintask.wxpay.response.WxPayPreOrderResponse;
import org.junit.Test;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/17 16:34
 */
public class DefaultWxPayClientTest {
    private final Object RUNNER_KEEPER = new Object();


    @Test
    public void preOrder() throws Exception {
        HttpConfig httpConfig = HttpConfig.builder()
                .connectTimeout(20000)
                .readTimeout(30000)
                .callTimeout(40000)
                .build();

        WxPayEnv wxPayEnv = new WxPayEnv("你的appid",
                "你的商户id",
                "你的 api key( 沙箱环境请调用 DefaultWxPayClient.getSignkey()或者postman手动获取， 线上环境登录微信商户平台获取",
                "你的 ssl证书，登录商户平台生成，将  xxx.p12文件放到 resources目录下",
                true,  /* 是否使用沙箱环境 */
                "http://yourdomain/notify_url",  /* 支付成功后的异步通知地址 */
                true,  /* 是否日志打印 request，response， 沙箱环境建议开启 */
                httpConfig);

        // 建议设置为单例
        DefaultWxPayClient wxPayClient = new DefaultWxPayClient(wxPayEnv);

        // lambda response  自动验签 切换域名重试
        wxPayClient.preOrder(preOrderRequest(), wxPayPreOrderResponse -> {
            System.out.println("微信下单成功：");
            System.out.println(wxPayPreOrderResponse);
            // todo: 生成二维码展示扫码
        }, wxPayCommonError -> {
            System.out.println("微信下单失败，请检查：" + wxPayCommonError);
        });

        // pojo response  自动验签 切换域名重试
        WxPayPreOrderResponse preOrderResponse = wxPayClient.execute(preOrderRequest());
        if (preOrderResponse.success()) {
            // 下单成功
            System.out.println("微信下单成功：");
            System.out.println(preOrderResponse);
        } else {
            // 下单失败
            System.out.println("微信下单失败，请检查：" + preOrderResponse.getErrCode() + " : " + preOrderResponse.getErrCodeDes());
        }

        // raw request  不做序列化，不验签，不支持重试
        Object rawResponse = wxPayClient.rawExecute(WxPayConstants.DOMAIN_API, preOrderRequest(), wxPayEnv.debugRequestBody());
        System.out.println("执行成功：" + rawResponse);

        synchronized (RUNNER_KEEPER) {
            RUNNER_KEEPER.wait();
        }
    }

    public WxPayPreOrderRequest preOrderRequest() throws Exception {
        WxPayPreOrderRequest request = new WxPayPreOrderRequest();
        String unionOrderNo = System.currentTimeMillis() + "";

        int totalFee = 301;

        request.setOutTradeNo(unionOrderNo);
        request.setProductId("商品id");
        String subject = "支付概览_";
        request.setBody(subject);
        request.setDetail("商品详情");
        request.setTradeType(WxPayConstants.NATIVE);
        request.setAttach("附加数据");
        request.setGoodsTag("tag: hot!");
        request.setDeviceInfo("支付设备");
        request.setTotalFee(totalFee);
        // 用户ip
        request.setSpbillCreateIp(InetAddress.getLocalHost().getHostAddress());
        LocalDateTime now = LocalDateTime.now();
        request.setTimeStart(now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        now = now.plusMinutes(5);
        // 订单可支付过期时间  推荐设置
        request.setTimeExpire(now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        return request;
    }
}
