package com.eyas.framework.config;

public interface NacosConfigKeyConstraint {

    /**
     * 需要区分common与环境的配置中心
     *
     * @return
     */
    String nacosKeyValue();
}
