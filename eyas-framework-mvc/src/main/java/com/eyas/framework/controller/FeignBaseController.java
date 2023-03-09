package com.eyas.framework.controller;

import com.eyas.framework.JsonUtil;
import com.eyas.framework.data.EyasRpcFeignEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Created by yixuan on 2019/10/17.
 */
public class FeignBaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/rpc/root/*")
    public ModelAndView root(@RequestBody EyasRpcFeignEntity eyasRpcFeignEntity, HttpServletRequest request) {
        log.info("cloudClient start");
        log.info("rootParams" + JsonUtil.toJson(eyasRpcFeignEntity));
        // 重定向
        String path = "/" + eyasRpcFeignEntity.getMethodName();
        log.info("path" + path);
        request.setAttribute("eyasRpcFeignEntity", eyasRpcFeignEntity);
        return new ModelAndView(path);
    }
}
