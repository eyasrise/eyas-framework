package com.eyas.framework.provider;

import com.eyas.framework.data.EyasFrameworkTokenInfo;
import com.eyas.framework.data.EyasFrameworkUserInfo;
import com.eyas.framework.intf.RedisService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedisUserProvider implements UserProvider {

    private final RedisService redisService;

    @Override
    public EyasFrameworkUserInfo getUserInfo(String userId, EyasFrameworkTokenInfo eyasFrameworkTokenInfo) {
        Object value = redisService.get(userId + eyasFrameworkTokenInfo.getTenantCode());
        if (value == null) {
            return null;
        }
        return EyasFrameworkUserInfo.builder().userId(userId).userCode(userId).tenantCode(eyasFrameworkTokenInfo.getTenantCode()).systemUser(value).build();
    }
}
