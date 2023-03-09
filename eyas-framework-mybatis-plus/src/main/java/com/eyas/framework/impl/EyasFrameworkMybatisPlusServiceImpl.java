package com.eyas.framework.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eyas.framework.mapper.EyasFrameworkMybatisPlusMapper;
import com.eyas.framework.data.EyasFrameworkQuery;
import com.eyas.framework.intf.EyasFrameworkMybatisPlusService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Service
public class EyasFrameworkMybatisPlusServiceImpl<D,Q> implements EyasFrameworkMybatisPlusService<D,Q> {

    @Resource
    private EyasFrameworkMybatisPlusMapper<D> eyasFrameworkMybatisPlusMapper;

    @Override
    public Integer updateDto(Long id, D d){
        // 查询id
        D DNew = this.eyasFrameworkMybatisPlusMapper.selectById(id);
        BeanUtils.copyProperties(d, DNew, "version", "id");
        return this.eyasFrameworkMybatisPlusMapper.updateById(DNew);
    }

    @Override
    public List<D> queryDto(Q q){
        // 复制对象
        Class<D> entityClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            D d = entityClass.newInstance();
            BeanUtils.copyProperties(q, d);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        EyasFrameworkQuery eyasFrameworkQuery = (EyasFrameworkQuery) q;
        QueryWrapper<D> wrapper = new QueryWrapper<>(); //首先新建一个 QueryWrapper
        IPage<D> page = new Page<>(eyasFrameworkQuery.getCurrentPage(), eyasFrameworkQuery.getPageSize());  // 查询第1页，每页返回5条
        this.eyasFrameworkMybatisPlusMapper.selectPage(page, wrapper);
        eyasFrameworkQuery.setTotalRecord((int) page.getTotal());
        return page.getRecords();
    }
}
