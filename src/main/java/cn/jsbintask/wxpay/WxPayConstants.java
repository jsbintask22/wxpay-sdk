package cn.jsbintask.wxpay;

import okhttp3.OkHttpClient;

/**
 * @author jsbintask@gmail.com
 * @date 2019/7/23 10:50
 */
public interface WxPayConstants {
    String DOMAIN_API = "api.mch.weixin.qq.com";
    String DOMAIN_API2 = "api2.mch.weixin.qq.com";
    String DOMAIN_APIHK = "apihk.mch.weixin.qq.com";
    String DOMAIN_APIUS = "apius.mch.weixin.qq.com";


    String FAIL = "FAIL";
    String SUCCESS = "SUCCESS";
    String HMACSHA256 = "HMAC-SHA256";
    String MD5 = "MD5";

    String FIELD_SIGN = "sign";
    String FIELD_SIGN_TYPE = "sign_type";
    String FIELD_APPID = "appid";
    String FIELD_MCHID = "mch_id";
    String NATIVE = "NATIVE";
    String JSAPI = "JSAPI";

    String USER_AGENT = Version.NAME +
            " (" + System.getProperty("os.arch") + " " + System.getProperty("os.name") + " " + System.getProperty("os.version") +
            ") Java/" + System.getProperty("java.version") + " OkHttpClient/" + OkHttpClient.class.getPackage().getImplementationVersion();

    /**
     * 线上环境api uri
     */
    String MICROPAY_URL_SUFFIX = "/pay/micropay";
    String UNIFIEDORDER_URL_SUFFIX = "/pay/unifiedorder";
    String ORDERQUERY_URL_SUFFIX = "/pay/orderquery";
    String REVERSE_URL_SUFFIX = "/secapi/pay/reverse";
    String CLOSEORDER_URL_SUFFIX = "/pay/closeorder";
    String REFUND_URL_SUFFIX = "/secapi/pay/refund";
    String REFUNDQUERY_URL_SUFFIX = "/pay/refundquery";
    String DOWNLOADBILL_URL_SUFFIX = "/pay/downloadbill";
    String DOWNLOADFUNDFLOW_URL_SUFFIX = "/pay/downloadfundflow";
    String REPORT_URL_SUFFIX = "/payitil/report";
    String SHORTURL_URL_SUFFIX = "/tools/shorturl";
    String AUTHCODETOOPENID_URL_SUFFIX = "/tools/authcodetoopenid";
    String QUERY_COMMENT_URL_SUFFIX = "/billcommentsp/batchquerycomment";

    String GET_SIGNKEY_URL_SUFFIX = "/pay/getsignkey";
    String SANDBOX_SUFFIX = "/sandboxnew";
}
