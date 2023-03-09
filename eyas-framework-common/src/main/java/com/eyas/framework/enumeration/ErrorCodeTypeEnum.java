package com.eyas.framework.enumeration;

/**
 * @author Created by yixuan on 2018/12/27.
 */
public enum ErrorCodeTypeEnum {

    /**
     * 系统异常
     */
    SYSTEM("SYS", "系统"),

    /**
     * 业务异常
     */
    BIZ("BUSINESS", "业务"),

    /**
     * 参数异常
     */
    PARAMETER("PARM", "参数"),

    /**
     * 其他异常
     */
    OTHER("O", "其他"),
    ;

    private String code;

    private String desc;


    ErrorCodeTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
