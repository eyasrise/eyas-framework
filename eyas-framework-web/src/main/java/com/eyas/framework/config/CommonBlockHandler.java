package com.eyas.framework.config;

import com.alibaba.csp.sentinel.slots.block.BlockException;

import java.util.Map;

/**
 * @author Fox
 */

public class CommonBlockHandler {

    /**
     * 注意： 必须为 static 函数   多个方法之间方法名不能一样
     * @param exception
     * @return
     */
    public static R handleException(Map<String, Object> params, BlockException exception){
        return R.error(-1,"===被限流啦==="+exception);
    }

    public static R handleException2(Integer id, BlockException exception){
        return R.error(-1,"===被限流啦==="+exception);
    }

    public static String handleException3(BlockException exception){
        return "===被限流啦==="+exception;
    }
}
