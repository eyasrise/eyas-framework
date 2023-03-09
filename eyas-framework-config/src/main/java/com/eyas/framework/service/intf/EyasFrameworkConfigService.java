package com.eyas.framework.service.intf;

import com.eyas.framework.config.NacosConfigKeyConstraint;

/**
 * @author Created by yixuan on 2019/7/3.
 */
public interface EyasFrameworkConfigService {

    String getEnvNacosConfigValue(NacosConfigKeyConstraint nacosConfigKeyConstraint);
}
