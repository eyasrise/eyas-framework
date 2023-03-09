package com.eyas.framework.enumeration;

/**
 * @author Created by yixuan on 2018/12/27.
 */
public enum ErrorCodeSourceEnum {

    /**
     * 内部异常
     */
    INTERNAL("IN", "内部"),
    /**
     * 外部异常
     */
    OUTER("OUT", "外部"),
    /**
     * 其他异常
     */
    OTHER("O", "其他"),
    ;

    private String code;

    private String desc;


    ErrorCodeSourceEnum(String code, String desc) {
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
