package com.eyas.framework.enums;

import com.eyas.framework.config.NacosConfigKeyConstraint;

/**
 * @author eyas
 */
public enum EyasFrameworkNacosKeyEnum implements NacosConfigKeyConstraint {

    // common域
    COMMON_NAME("common.user.name"),
    COMMON_AGE("common.user.age"),

    // 自己域
    NAME("user.name"),
    AGE("user.age");

    EyasFrameworkNacosKeyEnum(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }

    @Override
    public String nacosKeyValue() {
        return getName();
    }
}
