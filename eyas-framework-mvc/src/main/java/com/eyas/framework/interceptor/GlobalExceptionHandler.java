package com.eyas.framework.interceptor;

import com.eyas.framework.data.EyasFrameworkResult;
import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;
import com.eyas.framework.exception.EyasFrameworkRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Created by yixuan on 2019/7/11.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        String msg = StringUtils.defaultIfEmpty(e.getMessage(), "服务器出错");
        log.error(ErrorFrameworkCodeEnum.SYSTEM_ERROR.getErrCode() + "-" + msg, e);
        return EyasFrameworkResult.fail(ErrorFrameworkCodeEnum.SYSTEM_ERROR.getErrCode(), msg);
    }

    @ExceptionHandler(EyasFrameworkRuntimeException.class)
    public Object handleException1(EyasFrameworkRuntimeException e) {
        String msg = StringUtils.defaultIfEmpty(e.getMsg(), "服务器出错");
        log.warn(e.getCode() + "-" + msg);
        return EyasFrameworkResult.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleIllegalParamException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String tips = "参数不合法";
        if (!errors.isEmpty()) {
            tips = errors.stream().map(error -> {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    return fieldError.getField() + ":" + fieldError.getDefaultMessage();
                }
                return error.getDefaultMessage();
            }).collect(Collectors.joining(","));
        }
        log.warn(tips, e);
        return EyasFrameworkResult.fail(ErrorFrameworkCodeEnum.NULL_PARAM_ERROR.getErrCode(), tips);
    }
}
