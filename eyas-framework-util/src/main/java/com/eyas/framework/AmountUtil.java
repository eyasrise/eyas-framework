package com.eyas.framework;

import java.math.BigDecimal;

/**
 * @author Created by yixuan on 2019/7/26.
 */
public class AmountUtil {

    // 金额默认小数点后两位计算
    // 方式选择四舍五入


    public static BigDecimal amountAdd(Object numA, Object numB){
        return BigDecimalUtil.bigDecimalAdd(numA, numB).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal amountAdd(Object... num){
        return BigDecimalUtil.bigDecimalAdd(num).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal amountSubtract(Object numA, Object numB){
        return BigDecimalUtil.bigDecimalSubtract(numA, numB).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal amountSubtract(Object... num){
        return BigDecimalUtil.bigDecimalSubtract(num).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal amountMultiply(Object numA, Object numB){
        return BigDecimalUtil.bigDecimalMultiply(numA, numB).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal amountDivide(Object numA, Object numB){
        return BigDecimalUtil.bigDecimalDivide(numA, numB, 2).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
