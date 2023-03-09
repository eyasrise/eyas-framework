package com.eyas.framework.controller;

import com.eyas.framework.DateUtil;
import com.eyas.framework.SnowflakeIdWorker;
import com.eyas.framework.annotation.WithOutToken;
import com.eyas.framework.entity.DipperInfo;
import com.eyas.framework.entity.DipperInfoQuery;
import com.eyas.framework.intf.EyasFrameworkMybatisPlusService;
//import com.eyas.framework.service.intf.DipperInfoService;
import com.eyas.framework.service.intf.DipperInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testMp")
public class MybatisPlusTestController {

    @Autowired
    private DipperInfoService dipperInfoService;

    @Autowired
    private EyasFrameworkMybatisPlusService<DipperInfo, DipperInfoQuery> eyasFrameworkMybatisPlusService;

//    @PostMapping("/insertDto")
//    @WithOutToken
//    public void insertDto(){
//        DipperInfo dipperInfo = new DipperInfo();
//        dipperInfo.setCode("DP" + SnowflakeIdWorker.generateId().toString());
//        dipperInfo.setOperator("王瑞测试mybatis-plus");
//        dipperInfo.setCreateTime(DateUtil.stringToDate(DateUtil.getCurrentDateTime(), DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));
//        dipperInfo.setUpdateTime(DateUtil.stringToDate(DateUtil.getCurrentDateTime(), DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));
//
//        this.dipperInfoService.insertDto(dipperInfo);
//    }

    @GetMapping("/queryDto")
    @WithOutToken
    public List<DipperInfo> queryDto(DipperInfoQuery dipperInfoQuery){
        return this.eyasFrameworkMybatisPlusService.queryDto(dipperInfoQuery);
    }

    @PostMapping("/updateDto")
    @WithOutToken
    public Integer updateDto(@RequestBody DipperInfo dipperInfo){
        return this.dipperInfoService.updateDto(dipperInfo.getId(), dipperInfo);
    }
}
