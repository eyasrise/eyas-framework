package com.eyas.framework.constant;

/**
 * @author Created by yixuan on 2019/6/20.
 */
public class SystemConstant {

    /**
     * 业务领域
     */
    public static final String DOMAIN = "EYAS-FRAMEWORK";

    public static final String JWT_SECRET = "ZXlhcy1mcmFtZXdvcmstYmFzZQ==";

    public static final Long JWT_TTL = 3600_000L*24;

    /**
     * cpu核心数
     */
    public final static int PROCESSORS = Runtime.getRuntime().availableProcessors();

}
