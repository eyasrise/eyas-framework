package com.eyas.framework.config;

import com.eyas.framework.data.EyasFrameworkTokenInfo;
import com.eyas.framework.data.EyasFrameworkUserInfo;
import com.eyas.framework.intf.DatabaseService;
import com.eyas.framework.provider.UserProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DemoProvicer implements UserProvider {

    private DatabaseService databaseService;

    @Override
    public EyasFrameworkUserInfo getUserInfo(String userId, EyasFrameworkTokenInfo eyasFrameworkTokenInfo) {
        return databaseService.getInfo();
    }
}
