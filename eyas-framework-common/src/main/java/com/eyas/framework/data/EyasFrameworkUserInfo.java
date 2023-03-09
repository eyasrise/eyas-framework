package com.eyas.framework.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * eyas 统一用户包装体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EyasFrameworkUserInfo extends EyasFrameworkDto{

    /**
     * 暂同userCode
     */
    private String userId;
    /**
     * 暂同userId
     */
    private String userCode;

    private String userName;

    private Long tenantCode;

    /**
     * 兼容项目中SystemUser
     * TODO 最好这边拿不到具体的类问题改掉
     */
    private Object systemUser;

}
