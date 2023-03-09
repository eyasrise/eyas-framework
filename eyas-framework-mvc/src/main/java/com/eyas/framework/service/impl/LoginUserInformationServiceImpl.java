package com.eyas.framework.service.impl;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.eyas.framework.EmptyUtil;
import com.eyas.framework.JwtUtils;
import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;
import com.eyas.framework.exception.EyasFrameworkRuntimeException;
import com.eyas.framework.intf.RedisService;
import com.eyas.framework.service.intf.LoginUserInformationService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class LoginUserInformationServiceImpl implements LoginUserInformationService {

    @Autowired
    private RedisService redisService;


    @Override
    public Object LoginUserInformation(){
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 校验token是否存在
        EmptyUtil.dealEmptyDataReturn(token, "无法获取登录人的token");
        Claims claims;
        try {
            claims = JwtUtils.parseJWT(token);
        } catch (JWTDecodeException j) {
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.LOGIN_ERROR,"无token，请重新登录");
        }
        // 获取用户id
        String userId = claims.getId();
        return redisService.get(userId);
    }
}
