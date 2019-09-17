package cn.jsbintask.wxpay.response;

import lombok.Data;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/6 11:31
 * 不同的 错误逻辑码请作不同处理！
 * see <b></b><a>https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1</a></b>
 */
@Data
public class WxPayCommonError {
    private String returnCode;
    private String returnMsg;
    /**
     * 业务码逻辑
     */
    private String resultCode;
    private String errCode;
    private String errCodeDes;

}
