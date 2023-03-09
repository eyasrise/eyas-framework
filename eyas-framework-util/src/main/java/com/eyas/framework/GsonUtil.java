package com.eyas.framework;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    private static Gson gson = new Gson();

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
        // 类型默认值，设置null
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new StringConverter(null), Long.class);
        ConvertUtils.register(new ShortConverter(null), Short.class);
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new DoubleConverter(null), Double.class);
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        // 忽略没有的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //过滤为null的属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    }

    public static <T> T fromJson(String json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);

    }

    public static <T> List<T> listFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * json字符串转map
     *
     * @param jsonStr
     * @return
     */
    public static <T> T jsonToObject(String jsonStr, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }

        T t = null;
        try {
            t = objectMapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    /**
     * 对象转json字符串
     *
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj) {
        String jsonStr = null;
        if (obj == null) {
            return jsonStr;
        }

        try {
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonStr;
    }

    /**
     * json转map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMap(String json) {
        return JSON.parseObject(json, Map.class);
    }

    /**
     * 转换map
     *
     * @param jsonStr
     * @return
     * @throws IOException
     */
    public static Map<String, Object> convertToMap(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }

        try {
            return objectMapper.readValue(jsonStr, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("convertToMap error!", e);
        }
    }

    /**
     * 转换map列表
     *
     * @param jsonStr
     * @return
     * @throws IOException
     */
    public static List<Map<String, Object>> convertToMapList(String jsonStr) throws IOException {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }

        return objectMapper.readValue(jsonStr, List.class);
    }
}
