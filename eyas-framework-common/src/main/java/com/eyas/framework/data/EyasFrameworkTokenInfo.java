package com.eyas.framework.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * token存放的各种信息
 * 用于用户的各种扩展
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EyasFrameworkTokenInfo extends EyasFrameworkDto{

    private String userName;

    private Long tenantCode;

}
