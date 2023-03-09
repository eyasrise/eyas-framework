package com.eyas.framework.entity;

import com.eyas.framework.data.EyasFrameworkBaseQuery;
import com.eyas.framework.data.EyasFrameworkQuery;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Created by yixuan on 2019/9/11.
 */
@Data
public class UserEntityQuery extends EyasFrameworkBaseQuery {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话
     */
    private String phone;

    /**
     * email
     */
    private String email;

    /**
     *
     */
    private BigDecimal payAmount;
}
