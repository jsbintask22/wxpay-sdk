package cn.jsbintask.wxpay;

import cn.jsbintask.wxpay.response.WxPayResponse;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/6 10:42
 */
public class WxPayException extends RuntimeException {
    private static final long serialVersionUID = -238091758285157331L;

    private String returnCode;
    private String returnMsg;
    private String resultCode;
    private String errCode;
    private String errCodeDes;

    public WxPayException() {
        super();
    }

    public WxPayException(String message, Throwable cause) {
        super(message, cause);
    }

    public WxPayException(String message) {
        super(message);
    }

    public WxPayException(Throwable cause) {
        super(cause);
    }

    public WxPayException(String returnCode, String returnMsg) {
        super(returnCode + ":" + returnMsg);
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
    }

    public WxPayException(WxPayResponse response) {


        this.returnCode = response.getReturnMsg();
        this.returnMsg = response.getReturnMsg();
        this.resultCode = response.getResultCode();
        this.errCode = response.getErrCode();
        this.errCodeDes = response.getErrCodeDes();
    }

    public String getReturnCode() {
        return this.returnCode;
    }

    public String getReturnMsg() {
        return this.returnMsg;
    }
}
