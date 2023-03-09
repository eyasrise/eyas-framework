package com.eyas.framework.service.intf;


import java.util.List;

/**
 * @author Created by yixuan on 2019/1/7.
 */
public interface EyasFrameworkService<Dto, Q> {

    /**
     * 添加
     *
     * @param dto 对象do
     * @return 1
     */
    Integer insert(Dto dto);


    /**
     * 批量新增数据-默认集合上限500
     *
     * @param dtoList 对象集合
     * @return 批量新增条数
     */
    Integer batchInsert(List<Dto> dtoList);

    /**
     * 批量新增数据——需要告知集合上限，一般最好低于1000
     *
     * @param dtoList 对象集合
     * @param splitNumber-集合上限
     * @return 批量新增条数
     */
    Integer batchInsert(List<Dto> dtoList, Integer splitNumber);

    /**
     * 修改
     *
     * @param dto 对象do
     * @return 1
     */
    Integer update(Dto dto);

    /**
     * 更新数据-带乐观锁
     *
     * @param dto
     * @return
     */
    Integer updateById(Dto dto);

    /**
     * 批量更新
     *
     * @param dto 对象dto
     * @return 更新记录数
     */
    Integer batchUpdate(Dto dto);

    /**
     * 删除
     *
     * @param id 被删除对象id
     * @return 1
     */
    Integer deleteById(Long id);

    /**
     *
     *
     * @param d
     * @return
     */
    Integer delete(Dto d);

    /**
     * 批量删除
     *
     * @param dto 对象dto
     * @return 删除记录数
     */
    Integer batchDelete(Dto dto);

    /**
     * 更新数据-先删除再更新
     * 复杂的逻辑可以先执行删除操作，再执行新增逻辑
     *
     * @param dto 对象dto
     * @return 更新记录数
     */
    Integer updateByDelete(Dto dto);

    /**
     * 查询
     *
     * @param q 对象query
     * @return 对象组
     */
    List<Dto> query(Q q);

    /**
     * 通用多值查询
     *
     * @param q
     * @return
     */
    List<Dto> queryStatusStr(Q q);

    /**
     * 查询记录数
     *
     * @param q 对象query
     * @return 记录数
     */
    Integer queryCount(Q q);

    /**
     * 根据不同条件查询数据
     *
     * @param q q
     * @return dtoList
     */
    List<Dto> queryByDifferentConditions(Q q);

    /**
     * 根据id查询数据
     *
     * @param id id
     * @return Dto
     */
    Dto getInfoById(Long id);

    /**
     * 根据id查询数据-用于乐观锁
     *
     * @param dto dto
     * @return dto
     */
    Dto getInfoById(Dto dto);

    /**
     * 乐观锁更新
     *
     * @param dto
     * @param id
     * @return
     */
    Integer updateByLock(Dto dto, Long id);

    Integer updateNoLock(Dto dto);

    /**
     * 动态条件更新数据
     * 逻辑:
     * 1、先根据条件查询出数据
     * 2、然后将数据拆分-粒度可以调整(默认10)
     * 3、拆分以后获取拆分后的id，然后根据批量id去更新数据
     *
     * @param q 批量更新的入参
     * @return 批量更新结果
     */
    Integer updateByLock(Q q);
}
