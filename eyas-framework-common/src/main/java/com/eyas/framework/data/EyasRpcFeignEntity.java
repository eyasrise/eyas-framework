package com.eyas.framework.data;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Created by yixuan on 2019/10/17.
 */
@Data
public class EyasRpcFeignEntity<T> implements Serializable {

    private String code;

    private String name;

    private String methodName;

    private Map<String, Object> map;

    private T t;
}
