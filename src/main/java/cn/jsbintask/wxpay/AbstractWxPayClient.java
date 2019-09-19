package cn.jsbintask.wxpay;

import cn.jsbintask.wxpay.config.WxPayEnv;
import cn.jsbintask.wxpay.request.AbstractWxPayRawResponseRequest;
import cn.jsbintask.wxpay.request.AbstractWxPayRequest;
import cn.jsbintask.wxpay.request.WxPaySandBoxSignKeyRequest;
import cn.jsbintask.wxpay.response.WxPayCommonError;
import cn.jsbintask.wxpay.response.WxPayResponse;
import cn.jsbintask.wxpay.utils.WxPayUtils;
import cn.jsbintask.wxpay.utils.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/17 14:44
 */
@Slf4j
public abstract class AbstractWxPayClient implements WxPayClient {
    private WxPayEnv wxPayEnv;

    public AbstractWxPayClient(WxPayEnv wxPayEnv) {
        this.wxPayEnv = wxPayEnv;
    }

    @Override
    @SuppressWarnings("all")
    public <T extends WxPayResponse> T execute(AbstractWxPayRequest<T> wxPayRequest) throws WxPayException {
        String retryDomain = WxPayConstants.DOMAIN_API;
        int exeCount = 0;
        Object rawData = null;
        try {
            // retry.
            for (; ; ) {
                try {
                    exeCount++;
                    rawData = rawExecute(retryDomain, wxPayRequest, wxPayEnv.debugRequestBody());
                    break;
                } catch (WxPayException e) {
                    log.info("domain: {}, api: '{}', count: {} failed. msg:\n{}", retryDomain, wxPayRequest.apiSuffix(), exeCount, e.getMessage());
                    retryDomain = WxPayConstants.DOMAIN_API2;
                    if (exeCount == 2) {
                        throw e;
                    }
                }
            }
        } catch (WxPayException e) {
            log.error("invoke api {} failed, msg: '{}', please check wxpay config.", wxPayRequest.apiSuffix(), e.getMessage());
            throw e;
        }

        // report
        log.debug("successful domain: {}, api: {}, count: {}", retryDomain, wxPayRequest.apiSuffix(), exeCount);

        if (WxPayUtils.isWxPayXml(rawData)) {
            T response = (T) XmlUtils.xml2Obj(((String) rawData), wxPayRequest.responseType());
            // check sign.
            HashMap responseMap = XmlUtils.xml2Obj(((String) rawData), HashMap.class);
            if (!response.needCheckSign() || doCheckResponseSign(wxPayEnv.getApiKey(), wxPayEnv.signType(), responseMap)) {

                // set @WrapperPrefix value
                WxPayUtils.setWrapPrefixValue(response, responseMap);

                if (wxPayRequest instanceof AbstractWxPayRawResponseRequest) {
                    response.setRawData(rawData);
                }

                return response;
            }

            throw new WxPayException("wxpay.sign.check.fail", "签名检验失败，数据不可取.");
        } else if (wxPayRequest instanceof AbstractWxPayRawResponseRequest) {
            Class<? extends WxPayResponse> aClass = wxPayRequest.responseType();
            T byteResponse = null;
            try {
                byteResponse = (T) aClass.newInstance();
            } catch (Exception igored) {
            }

            // default success.
            byteResponse.setReturnCode(WxPayConstants.SUCCESS);
            byteResponse.setResultCode(WxPayConstants.SUCCESS);
            byteResponse.setRawData(rawData);
            return byteResponse;
        } else {
            // redirect to 404.  need retry
            log.error("request redirected 404. content {}", rawData);
            throw new WxPayException("original.request.redirected", "request已被重定向，请检查环境域名是否正确");
        }
    }

    @Override
    @SuppressWarnings("all")
    public <T extends WxPayResponse> void execute(AbstractWxPayRequest<T> wxPayRequest, Consumer<T> responseConsumer, Consumer<WxPayCommonError> errorConsumer) throws WxPayException {
        Objects.requireNonNull(responseConsumer, "success handler cant be empty.");
        Objects.requireNonNull(errorConsumer, "failure handler cant be empty.");

        String retryDomain = WxPayConstants.DOMAIN_API;
        int exeCount = 0;
        Object rawData = null;
        try {
            // retry.
            for (; ; ) {
                try {
                    exeCount++;
                    rawData = rawExecute(retryDomain, wxPayRequest, wxPayEnv.debugRequestBody());
                    break;
                } catch (WxPayException e) {
                    log.info("domain: {}, api: '{}', count: {} failed. msg:\n{}", retryDomain, wxPayRequest.apiSuffix(), exeCount, e.getMessage());
                    retryDomain = WxPayConstants.DOMAIN_API2;
                    if (exeCount == 2) {
                        throw e;
                    }
                }
            }
        } catch (WxPayException e) {
            log.error("invoke api {} failed, msg: '{}', please check wxpay config.", wxPayRequest.apiSuffix(), e.getMessage());
            throw e;
        }

        // report
        log.debug("successful domain: {}, api: {}, count: {}", retryDomain, wxPayRequest.apiSuffix(), exeCount);

        if (WxPayUtils.isWxPayXml(rawData)) {
            T response = (T) XmlUtils.xml2Obj(((String) rawData), wxPayRequest.responseType());
            if (response != null && response.success()) {
                // check sign.
                HashMap responseMap = XmlUtils.xml2Obj(((String) rawData), HashMap.class);
                if (!response.needCheckSign() ||
                        doCheckResponseSign(wxPayEnv.getApiKey(), wxPayEnv.signType(), responseMap)) {

                    // set @WrapperPrefix value
                    WxPayUtils.setWrapPrefixValue(response, responseMap);

                    if (wxPayRequest instanceof AbstractWxPayRawResponseRequest) {
                        response.setRawData(rawData);
                    }
                    responseConsumer.accept(response);
                    return;
                }

                log.warn("invoke api {} successfully, response sign check failed.", wxPayRequest.apiSuffix());
                WxPayCommonError error = new WxPayCommonError();
                error.setReturnCode(WxPayConstants.FAIL);
                error.setResultCode(WxPayConstants.FAIL);
                error.setErrCode("wxpay.sign.check.fail");
                error.setErrCodeDes("api调用成功，微信签名验证失败.");
                errorConsumer.accept(error);
            } else {
                WxPayCommonError wxPayCommonError = new WxPayCommonError();
                wxPayCommonError.setReturnCode(response.getReturnCode());
                wxPayCommonError.setReturnCode(response.getReturnMsg());
                wxPayCommonError.setResultCode(response.getResultCode());
                wxPayCommonError.setErrCode(response.getErrCode());
                wxPayCommonError.setErrCodeDes(response.getErrCodeDes());
                errorConsumer.accept(wxPayCommonError);
            }
        } else if (wxPayRequest instanceof AbstractWxPayRawResponseRequest) {
            Class<? extends WxPayResponse> aClass = wxPayRequest.responseType();
            T byteResponse = null;
            try {
                byteResponse = (T) aClass.newInstance();
            } catch (Exception igored) {
            }

            byteResponse.setRawData(rawData);
            responseConsumer.accept(byteResponse);
        } else {
            // redirect to 404.  need retry
            log.error("request redirected 404. content {}", rawData);
            throw new WxPayException("original.request.redirected", "request已被重定向，请检查环境域名是否正确");
        }
    }

    @Override
    public boolean checkResponseSign(Map<String, String> wxPayResponse) {
        return checkAppid(wxPayResponse.get(WxPayConstants.FIELD_APPID), wxPayResponse.get(WxPayConstants.FIELD_MCHID))
                && doCheckResponseSign(wxPayEnv.getApiKey(), wxPayEnv.signType(), wxPayResponse);
    }

    abstract <T extends WxPayResponse> Object rawExecute(String domain, AbstractWxPayRequest<T> wxPayRequest, boolean debugRequest) throws WxPayException;

    @SuppressWarnings("all")
    protected <T extends WxPayResponse> String signRequest(AbstractWxPayRequest<T> wxPayRequest) throws WxPayException {
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

    WxPayEnv getWxPayEnv() {
        return this.wxPayEnv;
    }

    private boolean checkAppid(String appId, String mchId) {
        return wxPayEnv.getAppId().equals(appId) && wxPayEnv.getMchId().equals(mchId);
    }


    private boolean doCheckResponseSign(String signKey, String signType, Map<String, String> wxPayResponse) {
        // response sign do not participate check.
        String sign = wxPayResponse.remove(WxPayConstants.FIELD_SIGN);
        if (StringUtils.isBlank(sign)) {
            return false;
        }

        List<String> signContent = new ArrayList<>(wxPayResponse.keySet().size());
        wxPayResponse.forEach((key, value) -> {
            if (StringUtils.isNoneBlank(key, value)) {
                signContent.add(key + "=" + value);
            }
        });

        Collections.sort(signContent);
        String collect = String.join("&", signContent) + "&key=" + signKey;

        try {
            // return
            wxPayResponse.put(WxPayConstants.FIELD_SIGN, sign);
            return sign.equals(doSign(signType, signKey, collect));
        } catch (Exception e) {
            log.error("check response sign error. {}", e.getMessage());
            return false;
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
