package com.eyas.framework.service.impl;

import com.eyas.framework.EmptyUtil;
import com.eyas.framework.ListUtil;
import com.eyas.framework.StringUtil;
import com.eyas.framework.data.EyasFrameworkBaseQuery;
import com.eyas.framework.data.EyasFrameworkDo;
import com.eyas.framework.data.EyasFrameworkQuery;
import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;
import com.eyas.framework.exception.EyasFrameworkRuntimeException;
import com.eyas.framework.middle.EyasFrameworkMiddle;
import com.eyas.framework.service.intf.EyasFrameworkService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Created by yixuan on 2019/1/17.
 */
@Service
public class EyasFrameworkServiceImpl<Dto,D,Q> implements EyasFrameworkService<Dto,Q> {

    private final EyasFrameworkMiddle<D,Q> eyasFrameworkMiddle;

    public EyasFrameworkServiceImpl(EyasFrameworkMiddle<D, Q> eyasFrameworkMiddle) {
        this.eyasFrameworkMiddle = eyasFrameworkMiddle;
    }


    private D dtoToD(Dto dto){
        Class<D> entityClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        D d;
        try {
            d = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.NEW_INSTANCE_ERROR, "泛型创建对象有误!");
        }
        if(EmptyUtil.isNotEmpty(dto)) {
            BeanUtils.copyProperties(dto, d);
        }else{
            return null;
        }
        return d;
    }

    private Dto dToDto(D d){
        Class<Dto> entityClass = (Class<Dto>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Dto dto;
        try {
            dto = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.NEW_INSTANCE_ERROR, "泛型创建对象有误!");
        }
        if(EmptyUtil.isNotEmpty(d)) {
            BeanUtils.copyProperties(d, dto);
        }else{
            return null;
        }
        return dto;
    }

    @Override
    public List<Dto> queryByDifferentConditions(Q q){
        List<D> dList = this.eyasFrameworkMiddle.queryByDifferentConditions(q);
        List<Dto> dtoList = new ArrayList<>();
        dList.forEach(d->{
            Dto dto = this.dToDto(d);
            // dto转换
            if (EmptyUtil.isNotEmpty(dto)) {
                BeanUtils.copyProperties(d, dto);
            }
            dtoList.add(dto);
        });
        return dtoList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer insert(Dto dto){
        D d = this.dtoToD(dto);
        return this.eyasFrameworkMiddle.insert(d);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer batchInsert(List<Dto> dtoList){
        return this.batchInsert(dtoList, 500);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer batchInsert(List<Dto> dtoList, Integer splitNumber){
        List<D> dList = new ArrayList<>();
        dtoList.stream().forEach(dto -> {
            D d = this.dtoToD(dto);
            dList.add(d);
        });
        List<List<D>> ddList = ListUtil.splitList(dList, splitNumber);
        AtomicInteger cnt = new AtomicInteger();
        cnt.set(0);
        ddList.stream().forEach(ds -> {
            cnt.set(cnt.get() + this.eyasFrameworkMiddle.batchInsert(dList));
        });
        return cnt.get();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer update(Dto dto){
        D d = this.dtoToD(dto);
        return this.eyasFrameworkMiddle.updateNoLock(d);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateById(Dto dto){
        D d = this.dtoToD(dto);
        // 强转类型
        EyasFrameworkDo eyasFrameworkDo = (EyasFrameworkDo) d;
        if (EmptyUtil.isEmpty(eyasFrameworkDo)){
            return null;
        }
        Long id = eyasFrameworkDo.getId();
        D d1 = this.eyasFrameworkMiddle.getInfoById(id);
        BeanUtils.copyProperties(dto, d1);
        return this.eyasFrameworkMiddle.update(d1);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteById(Long id) {
        return this.eyasFrameworkMiddle.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer delete(Dto dto){
        D d = this.dtoToD(dto);
        return this.eyasFrameworkMiddle.delete(d);
    }

    @Override
    public List<Dto> query(Q q){
        List<D> dList = this.eyasFrameworkMiddle.query(q);
        List<Dto> dtoList = new ArrayList<>();
        for (D d:dList) {
            Dto dto = this.dToDto(d);
            dtoList.add(dto);
        }
        EyasFrameworkBaseQuery eyasFrameworkBaseQuery = (EyasFrameworkBaseQuery) q;
        eyasFrameworkBaseQuery.setTotalRecord(this.queryCount(q));
        return dtoList;
    }

    @Override
    public List<Dto> queryStatusStr(Q q){
        EyasFrameworkQuery eyasFrameworkQuery = (EyasFrameworkQuery) q;
        String statusStr = eyasFrameworkQuery.getStatusStr();
        if (EmptyUtil.isNotEmpty(statusStr)){
            eyasFrameworkQuery.setStatusInt(StringUtil.stringSplit(statusStr, ","));
        }
        return this.query(q);
    }

    @Override
    public Integer queryCount(Q q) {
        return this.eyasFrameworkMiddle.queryCount(q);
    }

    @Override
    public Dto getInfoById(Long id){
        D d = this.eyasFrameworkMiddle.getInfoById(id);
        Dto dto = this.dToDto(d);
        if (EmptyUtil.isNotEmpty(d)){
            BeanUtils.copyProperties(d, dto);
        }
        return dto;
    }

    @Override
    public Dto getInfoById(Dto dto) {
        D d = this.dtoToD(dto);
        D d1 = this.eyasFrameworkMiddle.getInfoById(d);
        Dto dto1 = this.dToDto(d1);
        if (EmptyUtil.isNotEmpty(d)){
            BeanUtils.copyProperties(d, dto);
        }
        return dto1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer batchUpdate(Dto dto){
        D d = this.dtoToD(dto);
        return this.eyasFrameworkMiddle.batchUpdate(d);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateNoLock(Dto dto){
        D d = this.dtoToD(dto);
        return this.eyasFrameworkMiddle.updateNoLock(d);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateByLock(Dto dto, Long id){
        D d = this.eyasFrameworkMiddle.getInfoById(id);
        if (EmptyUtil.isNotEmpty(d)){
            BeanUtils.copyProperties(dto, d);
        }
        return this.eyasFrameworkMiddle.updateNoLock(d);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer updateByLock(Q q){
        List<D> dList = this.eyasFrameworkMiddle.queryByDifferentConditions(q);
        AtomicReference<Integer> count = new AtomicReference<>(0);
        if (!EmptyUtil.dealListForceEmpty2(dList)){
            // 动态切割数组
            List<List<D>> lists = ListUtil.getListLengthDynamicExpansion(dList);
            lists.stream().forEach(dList1 -> {
                // 强转数据类型--获取批量的id
                List<Long> idList = new ArrayList<>();
                dList1.stream().forEach(d -> {
                    EyasFrameworkDo eyasFrameworkDo = (EyasFrameworkDo) d;
                    idList.add(eyasFrameworkDo.getId());
                });
                EyasFrameworkDo eyasFrameworkDo = new EyasFrameworkDo();
                BeanUtils.copyProperties(q, eyasFrameworkDo);
                eyasFrameworkDo.setIdList(idList);
                // 转数据
                D d = (D) eyasFrameworkDo;
                count.set(count.get() + this.eyasFrameworkMiddle.batchUpdate(d));
            });
        }
        return count.get();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer batchDelete(Dto dto){
        D d = this.dtoToD(dto);
        return this.eyasFrameworkMiddle.batchDelete(d);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateByDelete(Dto dto){
        D d = this.dtoToD(dto);
        // 先执行删除
        this.eyasFrameworkMiddle.delete(d);
        // 再执行新增
        return this.insert(dto);
    }

}
