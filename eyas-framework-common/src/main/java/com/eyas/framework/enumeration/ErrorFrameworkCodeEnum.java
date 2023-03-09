package com.eyas.framework.enumeration;

import com.eyas.framework.constant.SystemConstant;

public enum ErrorFrameworkCodeEnum {
    /**
     * 参数为空
     */
    NULL_PARAM_ERROR(ErrorCodeUtil.getErrorCode(SystemConstant.DOMAIN, ErrorCodeTypeEnum.PARAMETER.getCode(), ErrorCodeSourceEnum.INTERNAL.getCode(), "000000"), "参数为空"),
    /**
     * 数学数字0
     */
    MATH_ZERO(ErrorCodeUtil.getErrorCode(SystemConstant.DOMAIN, ErrorCodeTypeEnum.BIZ.getCode(), ErrorCodeSourceEnum.INTERNAL.getCode(), "000001"), "数字为0"),

    /**
     * 更新失败
     */
    UPDATE_DATA_FAIL(ErrorCodeUtil.getErrorCode(SystemConstant.DOMAIN, ErrorCodeTypeEnum.BIZ.getCode(), ErrorCodeSourceEnum.INTERNAL.getCode(), "000002"), "更新有误"),

    /**
     * 创建对象异常
     */
    NEW_INSTANCE_ERROR(ErrorCodeUtil.getErrorCode(SystemConstant.DOMAIN, ErrorCodeTypeEnum.BIZ.getCode(), ErrorCodeSourceEnum.INTERNAL.getCode(), "000003"), "创建对象异常"),

    /**
     * 时间处理异常
     */
    DATE_DEAL_ERROR(ErrorCodeUtil.getErrorCode(SystemConstant.DOMAIN, ErrorCodeTypeEnum.BIZ.getCode(), ErrorCodeSourceEnum.INTERNAL.getCode(), "000004"), "日期处理异常"),

    /**
     * 配置中心处理异常
     */
    NACOS_CONFIG_ERROR(ErrorCodeUtil.getErrorCode(SystemConstant.DOMAIN, ErrorCodeTypeEnum.BIZ.getCode(), ErrorCodeSourceEnum.INTERNAL.getCode(), "000005"), "配置中心处理异常"),

    /**
     * JWT异常
     */
    JWT_ERRCODE_EXPIRE(ErrorCodeUtil.getErrorCode(SystemConstant.DOMAIN, ErrorCodeTypeEnum.BIZ.getCode(), ErrorCodeSourceEnum.INTERNAL.getCode(), "000006"), "JWT异常"),

    /**
     * 登录异常
     */
    LOGIN_ERROR(ErrorCodeUtil.getErrorCode(SystemConstant.DOMAIN, ErrorCodeTypeEnum.BIZ.getCode(), ErrorCodeSourceEnum.INTERNAL.getCode(), "000007"), "登录异常"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(ErrorCodeUtil.getErrorCode(SystemConstant.DOMAIN, ErrorCodeTypeEnum.BIZ.getCode(), ErrorCodeSourceEnum.INTERNAL.getCode(), "000008"), "系統异常"),

    /**
     * 角色过期
     */
    USER_ROLE_EXPIRE_ERROR(ErrorCodeUtil.getErrorCode(SystemConstant.DOMAIN, ErrorCodeTypeEnum.BIZ.getCode(), ErrorCodeSourceEnum.INTERNAL.getCode(), "000009"), "用户角色已过期，请联系管理员"),

    /**
     * redis值获取异常
     */
    REDIS_VALUE_GET_ERROR(ErrorCodeUtil.getErrorCode(SystemConstant.DOMAIN, ErrorCodeTypeEnum.BIZ.getCode(), ErrorCodeSourceEnum.INTERNAL.getCode(), "000010"), "redis值获取异常"),
    ;

    /**
     * 错误码
     */
    private String errCode;

    /**
     * 错误
     */
    private String errMsg;

    ErrorFrameworkCodeEnum(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }


    public String getErrCode() {
        return errCode;
    }


    public String getErrMsg() {
        return errMsg;
    }
}
