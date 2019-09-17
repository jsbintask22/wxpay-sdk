## 可扩展，易使用的微信支付工具 WxpaySDK

![authentication](https://raw.githubusercontent.com/jsbintask22/static/master/wxpay/wxpay.png)

使用：
```xml
<dependency>
    <groupId>com.github.jsbintask22</groupId>
    <artifactId>wxpay-sdk</artifactId>
    <version>1.1.0</version>
</dependency>
```

支持多种不同方式调用：
```java
WxPayEnv wxPayEnv = new WxPayEnv("你的appid",
                "你的商户id",
                "你的 api key( 沙箱环境请调用 DefaultWxPayClient.getSignkey()或者postman手动获取， 线上环境登录微信商户平台获取",
                "你的 ssl证书，登录商户平台生成，将  xxx.p12文件放到 resources目录下",
                true,  /* 是否使用沙箱环境 */
                "http://yourdomain/notify_url",  /* 支付成功后的异步通知地址 */
                true,  /* 是否日志打印 request，response， 沙箱环境建议开启 */
                httpConfig);

        DefaultWxPayClient wxPayClient = new DefaultWxPayClient(wxPayEnv);

        // 1. lambda response  自动验签 切换域名重试
        wxPayClient.preOrder(preOrderRequest(), wxPayPreOrderResponse -> {
            System.out.println("微信下单成功：");
            System.out.println(wxPayPreOrderResponse);
            // todo: 生成二维码展示扫码
        }, wxPayCommonError -> {
            System.out.println("微信下单失败，请检查：" + wxPayCommonError);
        });

        // 2. pojo response  自动验签 切换域名重试
        WxPayPreOrderResponse preOrderResponse = wxPayClient.execute(preOrderRequest());
        if (preOrderResponse.success()) {
            // 下单成功
            System.out.println("微信下单成功：");
            System.out.println(preOrderResponse);
        } else {
            // 下单失败
            System.out.println("微信下单失败，请检查：" + preOrderResponse.getErrCode() + " : " + preOrderResponse.getErrCodeDes());
        }

        // 3. raw request  不做序列化，不验签，不支持重试
        Object rawResponse = wxPayClient.rawExecute(WxPayConstants.DOMAIN_API, preOrderRequest(), wxPayEnv.debugRequestBody());
        System.out.println("执行成功：" + rawResponse);
```


*Features:*
1. 默认支持lambda调用
2. 自动加加签，验签
3. 可配置调用项目
4. 支持不同方式调用
5. 所有request，response均使用普通pojo支持，不再使用map
6. 支持域名切换重试

**TODO**
1. 支持自动向微信报告api result
2. 支持自动切换主备域名
