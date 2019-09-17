package cn.jsbintask.wxpay;

import cn.jsbintask.wxpay.config.WxPayEnv;
import cn.jsbintask.wxpay.http.OkHttpManager;
import cn.jsbintask.wxpay.request.*;
import cn.jsbintask.wxpay.response.*;
import cn.jsbintask.wxpay.utils.WxPayUtils;
import cn.jsbintask.wxpay.utils.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/17
 */
@Slf4j
public class DefaultWxPayClient extends AbstractWxPayClient {
    private OkHttpManager okHttpManager;

    public DefaultWxPayClient(WxPayEnv wxPayEnv) {
        super(wxPayEnv);
        okHttpManager = new OkHttpManager(wxPayEnv);
    }

    /**
     * @param successHandler api调用成功，并且签名检验成功处理回调
     * @param errorHandler   api调用失败，业务处理失败回调， 如 参数不全等，验签失败
     */
    public void preOrder(WxPayPreOrderRequest payPreOrderRequest,
                         Consumer<WxPayPreOrderResponse> successHandler,
                         Consumer<WxPayCommonError> errorHandler) {
        execute(payPreOrderRequest,
                successHandler,
                errorHandler);
    }

    /**
     * @param systemOrderNo 系统内部订单号 systemOrderNo
     * @apiNote 其他同上
     */
    public void queryOrder(String systemOrderNo,
                           Consumer<WxPayOrderQueryResponse> success,
                           Consumer<WxPayCommonError> failHandler) {
        WxPayOrderQueryRequest request = new WxPayOrderQueryRequest();
        request.setOutTradeNo(systemOrderNo);
        execute(request,
                success,
                failHandler);
    }

    /**
     * 微信支付 关闭订单
     * 以下情况需要调用关单接口：商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
     * 系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     *
     * @apiNote 注意：订单生成后不能马上调用关单接口，最短调用时间间隔为5分钟。
     */
    public void closeOrder(String systemOrderNo,
                           Consumer<WxPayResponse> success,
                           Consumer<WxPayCommonError> failHandler) {
        WxPayCloseOrderRequest request = new WxPayCloseOrderRequest();
        request.setOutTradeNo(systemOrderNo);

        execute(request,
                success,
                failHandler);
    }


    /**
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，微信支付将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * 注意：
     * 1、交易时间超过一年的订单无法提交退款
     * 2、微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。申请退款总金额不能超过订单金额。 一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号
     * 3、请求频率限制：150qps，即每秒钟正常的申请退款请求次数不超过150次
     * 错误或无效请求频率限制：6qps，即每秒钟异常或错误的退款申请请求不超过6次
     * 4、每个支付订单的部分退款次数不能超过50次
     */
    public void refundOrder(WxPayRefundRequest refundRequest,
                            Consumer<WxPayRefundResponse> success,
                            Consumer<WxPayCommonError> failHandler) {
        execute(refundRequest,
                success,
                failHandler);
    }

    /**
     * 应用场景
     * 提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新查询退款状态。
     * 注意：如果单个支付订单部分退款次数超过20次请使用退款单号查询
     * 分页查询
     * 当一个订单部分退款超过10笔后，商户用微信订单号或商户订单号调退款查询API查询退款时，
     * 默认返回前10笔和total_refund_count（订单总退款次数）。商户需要查询同一订单下超过10笔的退款单时，可传入订单号及offset来查询，微信支付会返回offset及后面的10笔，以此类推。
     * 当商户传入的offset超过total_refund_count，则系统会返回报错PARAM_ERROR。
     * 举例：
     * 一笔订单下的退款单有36笔，当商户想查询第25笔时，可传入订单号及offset=24，微信支付平台会返回第25笔到第35笔的退款单信息，或商户可直接传入退款单号查询退款
     */
    public void queryRefund(WxPayQueryRefundRequest refundRequest,
                            Consumer<WxPayQueryRefundResponse> success,
                            Consumer<WxPayCommonError> failHandler) {
        execute(refundRequest,
                success,
                failHandler);
    }


    /**
     * 应用场景
     * 商户可以通过该接口下载历史交易清单。比如掉单、系统错误等导致商户侧和微信侧数据不一致，通过对账单核对后可校正支付状态。
     * 注意：
     * 1、微信侧未成功下单的交易不会出现在对账单中。支付成功后撤销的交易会出现在对账单中，跟原支付单订单号一致；
     * 2、微信在次日9点启动生成前一天的对账单，建议商户10点后再获取；
     * 3、对账单中涉及金额的字段单位为“元”。
     * 4、对账单接口只能下载三个月以内的账单。
     * 5、对账单是以商户号纬度来生成的，如一个商户号与多个appid有绑定关系，
     * 则使用其中任何一个appid都可以请求下载对账单。对账单中的appid取自交易时候提交的appid，与请求下载对账单时使用的appid无关。
     */
    public void downloadBill(WxPayDownloadBillRequest request,
                             BiConsumer<Object, File> success,
                             Consumer<WxPayCommonError> failHandler) {
        // 默认返回文本数据
        request.setTarType(null);
        execute(request,
                wxPayByteResponse -> {
                    Object rawData = wxPayByteResponse.getRawData();
                    File temp = null;
                    if (request.genFile()) {
                        temp = WxPayUtils.writeTextFile(rawData);
                    }
                    success.accept(rawData, temp);
                },
                failHandler);
    }

    /**
     * 商户可以通过该接口下载自2017年6月1日起 的历史资金流水账单。
     * 说明：
     * 1、资金账单中的数据反映的是商户微信账户资金变动情况；
     * 2、当日账单在次日上午9点开始生成，建议商户在上午10点以后获取；
     * 3、资金账单中涉及金额的字段单位为“元”。
     */
    public void downloadFundflow(WxPayDownloadBillFundflowRequest request,
                                 BiConsumer<String, File> success,
                                 Consumer<WxPayCommonError> failHandler) {
        request.setTarType(null);
        execute(request,
                wxPayByteResponse -> {
                    Object rawData = wxPayByteResponse.getRawData();
                    File temp = null;
                    if (request.genFile()) {
                        temp = WxPayUtils.writeTextFile(rawData);
                    }
                    success.accept((String) wxPayByteResponse.getRawData(), temp);
                },
                failHandler);
    }

    /**
     * 商户可以通过该接口拉取用户在微信支付交易记录中针对你的支付记录进行的评价内容。
     * 商户可结合商户系统逻辑对该内容数据进行存储、分析、展示、客服回访以及其他使用。
     * 如商户业务对评价内容有依赖，可主动引导用户进入微信支付交易记录进行评价。
     */
    public void downloadComment(WxPayQueryCommentRequest request,
                                BiConsumer<File, String> consumer,
                                Consumer<WxPayCommonError> failHandler) {
        execute(request,
                wxPayByteResponse -> {
                    Object rawData = wxPayByteResponse.getRawData();
                    File temp = null;
                    if (request.genFile()) {
                        temp = WxPayUtils.writeTextFile(rawData);
                    }
                    consumer.accept(temp, (String) rawData);
                },
                failHandler);
    }

    /**
     * 该接口主要用于Native支付模式一中的二维码链接转成短链接(weixin://wxpay/s/XXXXXX)，减小二维码数据量，提升扫描速度和精确度。
     */
    public void shortUrl(String longUrl,
                         Consumer<WxPayShortUrlResponse> success,
                         Consumer<WxPayCommonError> failHandler) {
        WxPayShortUrlRequest request = new WxPayShortUrlRequest();
        request.setLongUrl(longUrl);

        execute(request,
                success,
                failHandler);
    }

    /**
     * 用于获取沙箱环境 sign key  仅在沙箱环境可用( WxPayEnv.isSandEnv)
     * 线上环境从商家控制台查看
     */
    public void sandboxSignkey(Consumer<String> success, Consumer<WxPayCommonError> failHandler) {
        WxPaySandBoxSignKeyRequest request = new WxPaySandBoxSignKeyRequest();
        execute(request,
                wxPaySandBoxSignKeyResponse -> {
                    success.accept(wxPaySandBoxSignKeyResponse.getSandboxSignkey());
                },
                failHandler);
    }

    /**
     * 交易保障
     */
    public void report(WxPayReportRequest reportRequest,
                       Consumer<WxPayReportRequest.WxPayReportResponse> success,
                       Consumer<WxPayCommonError> failHandler) {
        execute(reportRequest,
                success,
                failHandler);
    }


    @SuppressWarnings("all")
    @Override
    <T extends WxPayResponse> Object rawExecute(String domain, AbstractWxPayRequest<T> wxPayRequest, boolean debugRequest) throws WxPayException {
        T tr = null;
        long startTime = System.currentTimeMillis();
        String requestBody = signRequest(wxPayRequest, wxPayEnv);

        String url = "https://" + domain + wxPayEnv.envUri() + wxPayRequest.apiSuffix();
        Request okHttpRequest = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", WxPayConstants.USER_AGENT)
                .addHeader("Content-Type", "text/xml;charset=utf-8")
                .post(RequestBody.create(okhttp3.MediaType.get("text/xml"), requestBody))
                .build();

        if (debugRequest) {
            log.debug("request body: \n{}", requestBody);
        }

        try (Response okResponse = okHttpManager.select(wxPayRequest.ssl()).newCall(okHttpRequest).execute()) {
            Response priorResponse = okResponse.priorResponse();
            if (priorResponse == null && okResponse.isSuccessful()) {
                String contentType = okResponse.header("content-type");
                log.debug("response content-type: {}", contentType);

                if (StringUtils.containsAny(contentType, "json", "text", "html", "xml")) {
                    Reader reader = okResponse.body().charStream();
                    StringBuilder xmlResponse = new StringBuilder();
                    char[] buff = new char[512];
                    int len = -1;
                    while (((len = reader.read(buff)) != -1)) {
                        xmlResponse.append(buff, 0, len);
                    }

                    if (debugRequest) {
                        log.debug("response body: \n{}", xmlResponse.toString());
                    }
                    return xmlResponse.toString();
                } else {
                    return okResponse.body().bytes();
                }
            } else if (priorResponse != null) {
                throw new WxPayException(String.format("wxpay api %s. returned status: %d, desc: %s", url, priorResponse.code(), priorResponse.message()));
            } else {
                throw new WxPayException(String.format("wxpay api %s. returned status: %d, desc: %s", url, okResponse.code(), okResponse.message()));
            }
        } catch (Exception e) {
            throw new WxPayException(e);
        } finally {
            log.debug("execute api {} takes {}ms", url, System.currentTimeMillis() - startTime);
        }
    }


    @SuppressWarnings("all")
    private <T extends WxPayResponse> String signRequest(AbstractWxPayRequest<T> wxPayRequest, WxPayEnv wxPayEnv) throws WxPayException {
        Objects.requireNonNull(wxPayRequest, "request can not be empty.");

        wxPayRequest.setNonceStr(WxPayUtils.nonceStr());
        wxPayRequest.setMchId(wxPayEnv.getMchId());
        // remove already sign.
        wxPayRequest.setSign(null);

        if (!(wxPayRequest instanceof WxPaySandBoxSignKeyRequest)) {
            wxPayRequest.setAppId(wxPayEnv.getAppId());
            wxPayRequest.setSignType(wxPayEnv.signType());
            try {
                Field notifyUrl = wxPayRequest.getClass().getDeclaredField("notifyUrl");
                notifyUrl.setAccessible(true);
                notifyUrl.set(wxPayRequest, wxPayEnv.getNotifyUrl());
            } catch (Exception ignored) {
            }
        }

        HashMap<String, String> map = XmlUtils.xml2Obj(XmlUtils.obj2Xml(wxPayRequest), HashMap.class);
        List<String> signContent = new ArrayList<>(map.keySet().size());
        map.forEach((key, value) -> {
            signContent.add(key + "=" + value);
        });
        Collections.sort(signContent);
        String collect = signContent.stream().collect(Collectors.joining("&"));

        if (!(wxPayRequest instanceof WxPaySandBoxSignKeyRequest)) {
            collect += "&key=" + wxPayEnv.getApiKey();
        }
        try {
            wxPayRequest.setSign(doSign(wxPayEnv.signType(), wxPayEnv.getApiKey(), collect));
            return XmlUtils.obj2Xml(wxPayRequest);
        } catch (Exception e) {
            log.error("sign content {} with {} error {}", collect, wxPayEnv.signType(), e.getMessage());
            throw new WxPayException(e);
        }
    }

    private String doSign(String signType, String key, String data) throws Exception {
        if (signType.equals(WxPayConstants.MD5)) {
            return WxPayUtils.MD5(data);
        } else if (WxPayConstants.HMACSHA256.equals(signType)) {
            return WxPayUtils.HMACSHA256(data, key);
        } else {
            throw new WxPayException("unsupport sign_type: " + signType);
        }
    }
}
