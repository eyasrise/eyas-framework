package com.eyas.framework.interceptor;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.eyas.framework.JwtUtils;
import com.eyas.framework.annotation.WithOutToken;
import com.eyas.framework.constant.SystemConstant;
import com.eyas.framework.data.EyasFrameworkTokenInfo;
import com.eyas.framework.data.EyasFrameworkUserInfo;
import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;
import com.eyas.framework.exception.EyasFrameworkRuntimeException;
import com.eyas.framework.intf.RedisService;
import com.eyas.framework.provider.RedisUserProvider;
import com.eyas.framework.provider.UserProvider;
import com.eyas.framework.utils.TenantThreadLocal;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author Created by yixuan on 2019/7/11.
 */
@Configuration
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private UserProvider userProvider;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisService.class)
    public UserProvider userProvider(RedisService redisService) {
        return new RedisUserProvider(redisService);
    }


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(WithOutToken.class)) {
            WithOutToken withOutToken = method.getAnnotation(WithOutToken.class);
            if (withOutToken.required()) {
                return true;
            }
        } else {
            //检查有没有需要用户权限的注解
            // if (method.isAnnotationPresent(UserLoginToken.class)) {
            //     UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            //     if (userLoginToken.required()) {
            // 执行认证
            if (token == null) {
                throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.LOGIN_ERROR, "登录失败!无token");
            }
            // 获取 token 中的 user id
            Claims claims;
            try {
                claims = JwtUtils.parseJWT(token);
            } catch (JWTDecodeException j) {
                throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.LOGIN_ERROR, "用户不存在，请重新登录");
            }
            // 获取用户id
            String userId = claims.getId();
            Long tenantCode = Long.parseLong(claims.getSubject());
            EyasFrameworkTokenInfo eyasFrameworkTokenInfo = EyasFrameworkTokenInfo.builder().tenantCode(tenantCode).build();
            EyasFrameworkUserInfo eyasFrameworkUserInfo = userProvider.getUserInfo(userId, eyasFrameworkTokenInfo);

            if (eyasFrameworkUserInfo == null) {
                throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.LOGIN_ERROR, "用户不存在，请重新登录");
            }
            //对token的过期时间进行判断，续期
            Date expiration = claims.getExpiration();
            if (expiration.getTime() - System.currentTimeMillis() < 1800_000*6) {
                String newToken = JwtUtils.createJWT(userId, tenantCode.toString(), SystemConstant.JWT_TTL);
                httpServletResponse.setHeader("token", newToken);
            }
            TenantThreadLocal.setSystemUser(eyasFrameworkUserInfo.getSystemUser());
            return true;
            // }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) {
    }
}
