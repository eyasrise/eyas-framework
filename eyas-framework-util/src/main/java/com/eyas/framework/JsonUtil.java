package com.eyas.framework;

import com.google.gson.*;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Json格式转换工具类
 *
 * @author Created by yixuan on 2019/7/10.
 */
public class JsonUtil {

    public static String toJson(Object src){
        Gson gson = new Gson();
        return gson.toJson(src);
    }

    public static String toJson(Object src, Type typeOfSrc) {
        Gson gson = new Gson();
        return gson.toJson(src,typeOfSrc);
    }

    public static void toJson(Object src, Appendable writer) throws JsonIOException {
        Gson gson = new Gson();
        gson.toJson(src,writer);
    }

    public static void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException {
        Gson gson = new Gson();
        gson.toJson(src,typeOfSrc,writer);
    }

    public static String toJson(JsonElement jsonElement) {
        Gson gson = new Gson();
        return gson.toJson(jsonElement);
    }

    public static   <T> T fromJson(String src,Type type){
        Gson gson = new Gson();
        return gson.fromJson(src,type);
    }

    public static  <T> T fromJson(String json, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json,classOfT);
    }

    public static  <T> T fromJson(Reader json, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json,classOfT);
    }

    public static  <T> T fromJson(Reader json, Type typeOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json,typeOfT);
    }

    public static <T> T fromJson(String json, Class<T> classOfT, JsonDeserializer<T> deserializer) {
        Gson gson = new GsonBuilder().registerTypeAdapter(classOfT, deserializer).create();
        return gson.fromJson(json, classOfT);
    }
}
