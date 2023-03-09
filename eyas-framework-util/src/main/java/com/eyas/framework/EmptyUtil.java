package com.eyas.framework;

import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;
import com.eyas.framework.exception.EyasFrameworkRuntimeException;
import com.eyas.framework.wxutils.DemoDto;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Created by yixuan on 2019/6/20.
 */
@Slf4j
public class EmptyUtil {
    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return {@code true}: 为空<br>{@code false}: 不为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String && obj.toString().length() == 0) {
            return true;
        } else if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        } else if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        } else if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        } else if (obj instanceof StringBuffer && ((StringBuffer) obj).length() == 0) {
            return true;
        } else if (obj instanceof StringBuilder && ((StringBuilder) obj).length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断对象是否非空
     *
     * @param obj 对象
     * @return {@code true}: 非空<br>{@code false}: 空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 非空校验并打印错误日志
     *
     * @param o            校验对象
     * @param checkMessage 打印的信息
     * @return @return {@code true}: 为空<br>{@code false}: 不为空
     */
    public static boolean checkEmpty(Object o, String checkMessage) {
        if (EmptyUtil.isEmpty(o)) {
            log.error(checkMessage);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 非空校验数据异常，抛出异常；
     * 校验成功不返回结果；
     *
     * @param o      检验对象
     * @param errMsg 报错日志信息
     */
    private static void dealEmptyData(Object o, String errMsg) {
        if (EmptyUtil.checkEmpty(o, errMsg)) {
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.NULL_PARAM_ERROR, errMsg);
        } else {
        }
    }

    /**
     * 非空校验数据异常，抛出异常；
     * 校验成功返回结果；
     *
     * @param t      检验对象
     * @param errMsg 报错日志信息
     * @return object
     */
    public static <T> T dealEmptyDataReturn(T t, String errMsg) {
        EmptyUtil.dealEmptyData(t, errMsg);
        return t;
    }
    /**
     * 数字非空且不为checkNum;
     *
     */
    public static Boolean dealIntegerNull(Integer cnt,Integer checkNum){
        if (EmptyUtil.isNotEmpty(cnt)){
            return cnt.equals(checkNum);
        }
        return false;
    }

    public static Boolean dealIntegerNullThrow(Integer cnt,Integer checkNum, String errMsg){
        if (EmptyUtil.isNotEmpty(cnt)){
            return cnt.equals(checkNum);
        }else{
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.UPDATE_DATA_FAIL, errMsg);
        }
    }

    /**
     * 对集合进行强制判断空，这个方法一旦校验，集合必定有值.
     *
     * @param tList 集合
     * @param <T> 泛型
     * @return Boolean
     */
    @Deprecated
    public static <T> Boolean dealListForceEmpty(List<T> tList){
        if (EmptyUtil.isNotEmpty(tList) && EmptyUtil.isNotEmpty(tList.get(0))){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 对集合进行强制判断空，这个方法一旦校验，集合必定有值. 并抛出异常
     *
     * @param tList 集合
     * @param errMsg 错误描述
     * @param <T> 泛型
     * @return List<T>
     */
    public static <T> List<T> dealListForceEmptyDataReturn(List<T> tList, String errMsg){
        if (EmptyUtil.dealListForceEmpty(tList)){
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.NULL_PARAM_ERROR, errMsg);
        }
        return tList;
    }

    /**
     * 对集合进行强制判断空，这个方法一旦校验，集合必定有值.
     *
     * @param tList 集合
     * @param <T> 泛型
     * @return Boolean
     */
    public static <T> Boolean dealListForceEmpty2(List<T> tList){
        if (EmptyUtil.isNotEmpty(tList) && tList.size()>0){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 对集合进行强制判断空，这个方法一旦校验，集合必定有值.
     *
     * @param tList 集合
     * @param <T> 泛型
     * @return Boolean
     */
    public static <T> List<T> dealListForceEmpty2(List<T> tList, String errMsg){
        if (EmptyUtil.dealListForceEmpty2(tList)){
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.NULL_PARAM_ERROR, errMsg);
        }
        return tList;
    }

}
