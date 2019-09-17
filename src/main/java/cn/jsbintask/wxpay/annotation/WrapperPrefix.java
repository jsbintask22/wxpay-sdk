package cn.jsbintask.wxpay.annotation;

import java.lang.annotation.*;

/**
 * @author jsbintask@gmail.com
 * @date 2019/9/16 10:58
 * 用于处理不定数量的 变量反序列化
 * e.g:  单个代金券退款金额	coupon_refund_fee_$n_$m	否	Int	100	单个退款代金券支付金额, $n为下标，$m为下标，从0开始编号
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WrapperPrefix {
    /**
     * 变量前缀 如： coupon_refund_fee_
     */
    String value();

    /**
     * 变量类型： 默认为1, 如  $n_$m 则需设置为 2
     */
    int variables() default 1;
}
