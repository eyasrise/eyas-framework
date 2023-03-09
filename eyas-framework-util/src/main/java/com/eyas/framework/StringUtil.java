package com.eyas.framework;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Created by yixuan on 2019/6/20.
 */
@Slf4j
public class StringUtil {
    /**
     * 定义需要转义字符的数组
     */
    private static String[] ruleRange = {"|", ".", "$", "(", ")", "[", "{", "^", "?", "*", "+", "\\"};

    private static List<String> ruleRangeList = new ArrayList<>();

    private static void init() {
        Collections.addAll(ruleRangeList, ruleRange);
    }

    static {
        init();
    }

    public static List<String> stringSplit(String str, String splitRole) {
        return stringSplit(str, splitRole, true);
    }

    public static List<String> stringSplit(String str, String splitRole, Boolean isRemoveEmpty) {
        // 定义截取返回数据
        List<String> resultList = new ArrayList<>();
        // 校验入参数据
        String checkMessage = "对不起，输入参数不能为空!";
        if (!EmptyUtil.checkEmpty(str, checkMessage) && !EmptyUtil.checkEmpty(splitRole, checkMessage) && !EmptyUtil.checkEmpty(isRemoveEmpty, checkMessage)) {
            if (ruleRangeList.contains(splitRole)) {
                splitRole = "\\".trim() + splitRole;
            }
            String[] splitStr;
            //字符串截取
            if (isRemoveEmpty) {
                splitStr = str.split(splitRole);
            } else {
                splitStr = str.split(splitRole, -1);
            }
            Arrays.asList(splitStr).forEach(s -> resultList.add(s.trim()));
        }
        return resultList;
    }

    /**
     * 字符串截取从0开始
     *
     * @param subStringStr    截取字符串
     * @param subStringLength 截取长度
     * @return 截取结果
     */
    public static String subString(String subStringStr, int subStringLength) {
        String resultStr = "";
        if (EmptyUtil.isNotEmpty(subStringStr)) {
            resultStr = subStringStr.substring(0, subStringLength);
        } else {
            log.error("截取字符为空!");
        }
        return resultStr;
    }

}
