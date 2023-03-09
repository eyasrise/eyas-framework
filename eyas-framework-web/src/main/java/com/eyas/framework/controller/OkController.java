package com.eyas.framework.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.eyas.framework.GsonUtil;
import com.eyas.framework.annotation.WithOutToken;
import com.eyas.framework.config.CommonBlockHandler;
import com.eyas.framework.config.CommonFallback;
import com.eyas.framework.config.R;
import com.eyas.framework.config.UseTask;
import com.eyas.framework.data.EyasFrameworkResult;
import com.eyas.framework.entity.DipperInfo;
import com.eyas.framework.entity.UserEntityQuery;
import com.eyas.framework.intf.RedisService;
//import com.eyas.framework.mapper.DipperInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Created by yixuan on 2019/7/8.
 */
@RestController
@RequestMapping(value = "/hello")
@Slf4j
public class OkController {

    @GetMapping("/ok")
    @WithOutToken
    public String ok(){
        return "ok111111!";
    }

}
