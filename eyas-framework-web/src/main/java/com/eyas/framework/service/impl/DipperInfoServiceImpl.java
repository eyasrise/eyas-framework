package com.eyas.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eyas.framework.entity.DipperInfo;
import com.eyas.framework.entity.DipperInfoQuery;
import com.eyas.framework.intf.EyasFrameworkMybatisPlusService;
import com.eyas.framework.mapper.DipperInfoMapper;
import com.eyas.framework.service.intf.DipperInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DipperInfoServiceImpl implements DipperInfoService {


    @Resource
    private DipperInfoMapper dipperInfoMapper;

    @Resource
    private EyasFrameworkMybatisPlusService<DipperInfo, DipperInfoQuery> eyasFrameworkMybatisPlusService;


    @Override
    public void insertDto(DipperInfo dipperInfo){
        this.dipperInfoMapper.insert(dipperInfo);
    }

    @Override
    public Integer updateDto(Long id, DipperInfo dipperInfo){
//        DipperInfo DNew = this.dipperInfoMapper.selectById(id);
//        BeanUtils.copyProperties(dipperInfo, DNew, "version", "id");
        return this.eyasFrameworkMybatisPlusService.updateDto(id, dipperInfo);
    }

    @Override
    public Integer deleteDto(String id){
        return this.dipperInfoMapper.deleteById(id);
    }

    @Override
    public List<DipperInfo> queryDto(DipperInfoQuery dipperInfoQuery){
        // 复制对象
        DipperInfo dipperInfo = new DipperInfo();
        BeanUtils.copyProperties(dipperInfoQuery, dipperInfo);
        QueryWrapper<DipperInfo> wrapper = new QueryWrapper<>(dipperInfo); //首先新建一个 QueryWrapper
        wrapper.orderByDesc("id");
        IPage<DipperInfo> page = new Page<>(dipperInfoQuery.getCurrentPage(), dipperInfoQuery.getPageSize());  // 查询第1页，每页返回5条
        this.dipperInfoMapper.selectPage(page, wrapper);
        System.out.println(page.getRecords());
        System.out.println(page.getTotal());
        return page.getRecords();
    }

}
