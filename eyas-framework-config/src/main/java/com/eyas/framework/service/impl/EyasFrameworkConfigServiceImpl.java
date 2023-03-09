package com.eyas.framework.service.impl;

import com.eyas.framework.EmptyUtil;
import com.eyas.framework.config.NacosConfigKeyConstraint;
import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;
import com.eyas.framework.exception.EyasFrameworkRuntimeException;
import com.eyas.framework.service.intf.EyasFrameworkConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


/**
 * @author Created by yixuan on 2019/7/3.
 */
@Service
@Slf4j
public class EyasFrameworkConfigServiceImpl implements EyasFrameworkConfigService {

    private final Environment environment;

    public EyasFrameworkConfigServiceImpl(Environment environment) {
        this.environment = environment;
    }


    @Override
    public String getEnvNacosConfigValue(NacosConfigKeyConstraint nacosConfigKeyConstraint) {
        return this.getEnvNacosConfigValue(nacosConfigKeyConstraint.nacosKeyValue());
    }

    private String getEnvNacosConfigValue(String key){
        try {
            EmptyUtil.dealEmptyDataReturn(key, "获取nacos-configKey为空!");
            return environment.getProperty(key);
        }catch (Exception e) {
            log.error(ErrorFrameworkCodeEnum.NACOS_CONFIG_ERROR.getErrMsg(), e);
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.NACOS_CONFIG_ERROR, "配置中心服务异常");
        }
    }


}
