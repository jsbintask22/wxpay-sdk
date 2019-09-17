package cn.jsbintask.wxpay.request;

import cn.jsbintask.wxpay.response.WxPayResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/10 10:16
 * 继承该类在生成response时会把 raw response设置进去。
 */
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractWxPayRawResponseRequest<T extends WxPayResponse> extends AbstractWxPayRequest<T> {
    /**
     * 是否将rawData 生成文件（文本文件）
     */
    @JsonIgnore
    @Setter
    private boolean genFile;

    public boolean genFile() {
        return this.genFile;
    }
}
