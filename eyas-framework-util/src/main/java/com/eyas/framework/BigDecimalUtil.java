package com.eyas.framework;

import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;
import com.eyas.framework.exception.EyasFrameworkRuntimeException;

import java.math.BigDecimal;

/**
 * @author Created by yixuan on 2019/3/19.
 */
public class BigDecimalUtil {

    /**
     * bigDecimal转换-其他类型自己添加
     *
     * @param num 被转对象-string-integer-bigdecimal-其他自己加
     * @return BigDecimal
     */
    public static BigDecimal bigDecimalTransformation(Object num) {
        EmptyUtil.dealEmptyDataReturn(num, "进行BigDecimal转换数据不能为空!");
        BigDecimal bigDecimal = null;
        if (num instanceof Integer) {
            Integer numNew = (Integer) num;
            bigDecimal = new BigDecimal(numNew);
        } else if (num instanceof String) {
            String numNew = (String) num;
            bigDecimal = new BigDecimal(numNew);
        } else if (num instanceof BigDecimal) {
            bigDecimal = (BigDecimal) num;
        } else if (num instanceof Long){
            Long numNew = (Long) num;
            bigDecimal = new BigDecimal(numNew);
        }
        return bigDecimal;
    }

    /**
     * 加法
     *
     * @param numA 加数
     * @param numB 被加数
     * @return 和值
     */
    public static BigDecimal bigDecimalAdd(Object numA, Object numB) {
        BigDecimal bigNumA = BigDecimalUtil.bigDecimalTransformation(numA);
        BigDecimal bigNumB = BigDecimalUtil.bigDecimalTransformation(numB);
        return bigNumA.add(bigNumB);
    }

    /**
     * 减法
     *
     * @param numA 减数
     * @param numB 被减数
     * @return 差值
     */
    public static BigDecimal bigDecimalSubtract(Object numA, Object numB) {
        BigDecimal bigNumA = BigDecimalUtil.bigDecimalTransformation(numA);
        BigDecimal bigNumB = BigDecimalUtil.bigDecimalTransformation(numB);
        return bigNumA.subtract(bigNumB);
    }

    /**
     * 乘法
     *
     * @param numA 乘数
     * @param numB 被乘数
     * @return 积值
     */
    public static BigDecimal bigDecimalMultiply(Object numA, Object numB) {
        BigDecimal bigNumA = BigDecimalUtil.bigDecimalTransformation(numA);
        BigDecimal bigNumB = BigDecimalUtil.bigDecimalTransformation(numB);
        return bigNumA.multiply(bigNumB);
    }

    /**
     * 除法
     *
     * @param numA 除数
     * @param numB 被除数
     * @return 除值
     */
    public static BigDecimal bigDecimalDivide(Object numA, Object numB, Integer num) {
        BigDecimal bigNumA = BigDecimalUtil.bigDecimalTransformation(numA);
        BigDecimal bigNumB = BigDecimalUtil.bigDecimalTransformation(numB);
        if (BigDecimalUtil.compareZeroEqual(bigNumB)) {
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.MATH_ZERO, "bigDecimal除法运算分母不能为0");
        }
        return bigNumA.divide(bigNumB, BigDecimal.ROUND_CEILING).setScale(num, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 等于0
     *
     * @param num 值
     * @return true
     */
    public static boolean compareZeroEqual(Object num) {
        return BigDecimalUtil.bigDecimalSubtract(num, 0).equals(BigDecimal.ZERO);
    }

    /**
     * 大于0
     *
     * @param num 值
     * @return true
     */
    public static boolean compareZeroGreater(Object num) {
        int r = BigDecimalUtil.bigDecimalTransformation(num).compareTo(BigDecimal.ZERO);
        return 0 < r;
    }

    /**
     * 小于0
     *
     * @param num 值
     * @return true
     */
    public static boolean compareZeroSmaller(Object num) {
        int r = BigDecimalUtil.bigDecimalTransformation(num).compareTo(BigDecimal.ZERO);
        return 0 > r;
    }

    /**
     * 多个加法s
     */
    public static BigDecimal bigDecimalAdd(Object... num) {
        BigDecimal amountTotal = new BigDecimal(0);
        for (Object o : num) {
            BigDecimal amount = BigDecimalUtil.checkEmptyReturnZero(o);
            amountTotal = BigDecimalUtil.bigDecimalAdd(amountTotal, amount);
        }
        amountTotal = amountTotal.setScale(4, BigDecimal.ROUND_HALF_UP);
        return amountTotal;
    }

    /**
     * 多个减法s
     */
    public static BigDecimal bigDecimalSubtract(Object... num) {
        BigDecimal amountTotal = new BigDecimal(0);
        for (int i = 1; i < num.length; i++) {
            BigDecimal amount = BigDecimalUtil.checkEmptyReturnZero(num[i]);
            amountTotal = BigDecimalUtil.bigDecimalAdd(amountTotal, amount);
        }
        amountTotal = BigDecimalUtil.bigDecimalSubtract(num[0], amountTotal);
        amountTotal = amountTotal.setScale(4, BigDecimal.ROUND_HALF_UP);
        return amountTotal;
    }

    /**
     * 为空返回0，不为空返回原值
     *
     * @param num
     * @return
     */
    public static BigDecimal checkEmptyReturnZero(Object num) {
        if (EmptyUtil.isNotEmpty(BigDecimalUtil.bigDecimalTransformation(num))) {
            return BigDecimalUtil.bigDecimalTransformation(num);
        } else {
            return BigDecimalUtil.bigDecimalTransformation(0);
        }
    }
}
