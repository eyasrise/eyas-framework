package com.eyas.framework;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

@Data
@Slf4j
public class Base64Helper {

    /**
     * 字符集
     */
    private static final String CHARSET = "UTF-8";

    /**
     * <p>
     * 解base64
     * </p>
     * 
     * @param res 解密串
     * @return String
     * @author: lichunhui lichunhui1314@126.com
     * @date: Created on 2014-11-17 上午11:17:06
     */
    public static String encode(String res) {
        try {
            Base64 base = new Base64();
            if (res != null && !"".equals(res)) { // 即将解密串不为null并且不为“”
                return new String(base.encode(res.getBytes(CHARSET)));
            } else {
                log.warn("即将解密串为null或者为“” ");
                return "";
            }
        } catch (Exception e) {
            log.error("未知：" + e);
            return "";
        }
    }

    /**
     * 
     * <p>
     * base64编码
     * </p>
     * 
     * @param res 编码数组
     * @return byte[]
     * @author: lichunhui lichunhui1314@126.com
     * @date: Created on 2014-11-17 上午11:41:19
     */
    public static byte[] encode(byte[] res) {
        try {
            Base64 base = new Base64();
            return base.encode(res);
        } catch (Exception e) {
            log.error("未知：" + e);
            return null;
        }
    }

    /**
     * 
     * <p>
     * 解base64编码
     * </p>
     * 
     * @param str 被解密串
     * @return 解密串
     * @author: lichunhui lichunhui1314@126.com
     * @date: Created on 2014-11-17 上午11:41:41
     */
    public static String decode(String str) {

        try {
            return new String(new Base64().decode(str.getBytes()), CHARSET);
        } catch (Exception e) {
            log.error("未知：" + e);
            return null;
        }
    }

    /**
     * 解密
     * 
     * @param str 被解密数组
     * @return 解密数组
     */
    public static byte[] decodeByte(byte[] str) {
        return new Base64().decode(str);
    }

    /**
     * 
     * <p>
     * 解base64编码
     * </p>
     * 
     * @param str 被解密数组
     * @return 解密数组
     * @throws UnsupportedEncodingException byte[]
     * @author: lichunhui lichunhui1314@126.com
     * @date: Created on 2014-11-17 上午11:41:58
     */
    public static byte[] decode(byte[] str) throws UnsupportedEncodingException {
        return new Base64().decode(str);
    }
}
