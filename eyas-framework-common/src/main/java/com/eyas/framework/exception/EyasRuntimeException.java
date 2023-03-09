package com.eyas.framework.exception;

/**
 * @author Created by yixuan on 2018/12/27.
 */
public class EyasRuntimeException extends RuntimeException {

    private String msg;
    private String code;


    public EyasRuntimeException() {
        super();
    }


    public EyasRuntimeException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public EyasRuntimeException(String code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public EyasRuntimeException(String msg, String code, Throwable e) {
        super(msg, e);
        this.code = code;
        this.msg = msg;
    }

    public EyasRuntimeException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public EyasRuntimeException(Throwable e) {
        super(e);
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }
}
