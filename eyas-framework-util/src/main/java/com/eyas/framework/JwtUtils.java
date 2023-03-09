package com.eyas.framework;

import com.eyas.framework.constant.SystemConstant;
import com.eyas.framework.data.EyasFrameworkResult;
import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;
import com.eyas.framework.exception.EyasFrameworkRuntimeException;
import io.jsonwebtoken.*;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * @author Created by yixuan on 2019/7/10.
 */
public class JwtUtils {
        /**
         * 签发JWT
         * @param id
         * @param subject 可以是JSON数据 尽可能少
         * @param ttlMillis
         * @return  String
         *
         */
        public static String createJWT(String id, String subject, long ttlMillis) {
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            SecretKey secretKey = generalKey();
            JwtBuilder builder = Jwts.builder()
                    .setId(id)
                    // 主题
                    .setSubject(subject)
                    // 签发者
                    .setIssuer("user")
                    // 签发时间
                    .setIssuedAt(now)
                    // 签名算法以及密匙
                    .signWith(signatureAlgorithm, secretKey);
            if (ttlMillis >= 0) {
                long expMillis = nowMillis + ttlMillis;
                Date expDate = new Date(expMillis);
                // 过期时间
                builder.setExpiration(expDate);
            }
            return builder.compact();
        }
        /**
         * 验证JWT
         * @param jwtStr
         * @return
         */
        public static EyasFrameworkResult validateJWT(String jwtStr) {
            Claims claims = null;
            try {
                claims = parseJWT(jwtStr);
                return EyasFrameworkResult.ok(claims);
            } catch (Exception e) {
                throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.LOGIN_ERROR, "登录过期，请重新登录");
            }
        }


        public static SecretKey generalKey() {
            byte[] encodedKey = Base64.decode(SystemConstant.JWT_SECRET);
            SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
            return key;
        }

        /**
         *
         * 解析JWT字符串
         * @param jwt
         * @return
         * @throws Exception
         */
        public static Claims parseJWT(String jwt) throws EyasFrameworkRuntimeException {
            SecretKey secretKey = generalKey();
            Claims claim = null;
            try {
                claim = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(jwt)
                        .getBody();
            }catch (Exception e){
                throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.LOGIN_ERROR, e);
            }

            return claim;
        }
}
