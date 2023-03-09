package com.eyas.framework.exception;

import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;

/**
 * @author Created by yixuan on 2019/6/20.
 */
public class EyasFrameworkRuntimeException extends EyasRuntimeException{

    private ErrorFrameworkCodeEnum errorFrameworkCodeEnum;

    public EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum errCode) {
        super(errCode.getErrCode(), errCode.getErrMsg());
        this.errorFrameworkCodeEnum = errCode;
    }

    public EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum errCode, String msg) {
        super(errCode.getErrCode(), msg);
        this.errorFrameworkCodeEnum = errCode;
    }

    public EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum errCode, Throwable e) {
        super(errCode.getErrMsg(), errCode.getErrCode(), e);
        this.errorFrameworkCodeEnum = errCode;
    }

    public EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum errCode, String msg, Throwable e) {
        super(msg, errCode.getErrCode(), e);
        this.errorFrameworkCodeEnum = errCode;
    }

    public ErrorFrameworkCodeEnum getErrCodeEnum() {
        return errorFrameworkCodeEnum;
    }
}
