package com.eyas.framework.config;

import com.eyas.framework.data.EyasFrameworkUserInfo;
import com.eyas.framework.interceptor.AuthenticationInterceptor;
import com.eyas.framework.provider.UserProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private AuthenticationInterceptor authenticationInterceptor;

    @Bean
    public UserProvider userProvider(){

        return (userId, tokenInfo) -> {
//            EyasFrameworkUserInfo aa = EyasFrameworkUserInfo.builder().userId("XS1212").userCode("1212").tenantCode(100L).systemUser(null).build();
//            EmptyUtil.dealEmptyDataReturn(tokenInfo.getUserName(), "数据有误");
            EyasFrameworkUserInfo bb = new EyasFrameworkUserInfo();
            bb.setTenantCode(tokenInfo.getTenantCode());
            bb.setUserName(tokenInfo.getUserName());
            bb.setUserId(userId);
            return EyasFrameworkUserInfo.builder()
                    .tenantCode(tokenInfo.getTenantCode())
                    .userId(userId)
                    .userName(tokenInfo.getUserName())
                    .systemUser(bb)
                    .build();
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor);
    }

}
