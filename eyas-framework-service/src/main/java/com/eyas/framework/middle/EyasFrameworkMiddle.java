package com.eyas.framework.middle;

import com.eyas.framework.mapper.EyasFrameworkDao;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Created by yixuan on 2019/1/17.
 */
@Component
public class EyasFrameworkMiddle<D, Q> {

    private final EyasFrameworkDao<D, Q> eyasFrameworkDao;

    public EyasFrameworkMiddle(EyasFrameworkDao<D, Q> eyasFrameworkDao) {
        this.eyasFrameworkDao = eyasFrameworkDao;
    }

    public Integer insert(D d) {
        return this.eyasFrameworkDao.insertEyasFramework(d);
    }

    public Integer batchInsert(List<D> dList) {
        return this.eyasFrameworkDao.batchInsertEyasFramework(dList);
    }


    /**
     * 修改
     *
     * @param d 对象do
     * @return 1
     */
    public Integer update(D d) {
        return this.eyasFrameworkDao.updateEyasFramework(d);
    }

    /**
     * 修改
     *
     * @param d 对象do
     * @return 1
     */
    public Integer updateNoLock(D d) {
        return this.eyasFrameworkDao.updateNoLockEyasFramework(d);
    }


    /**
     * 删除
     *
     * @param id 被删除对象id
     * @return 1
     */
    public Integer deleteById(Long id) {
        return this.eyasFrameworkDao.deleteByIdEyasFramework(id);
    }

    /**
     * 通过不同条件删除数据
     *
     * @param d 对象
     * @return 1
     */
    public Integer delete(D d) {
        return this.eyasFrameworkDao.deleteEyasFramework(d);
    }

    /**
     * 查询
     *
     * @param q 对象query
     * @return 对象组
     */
    public List<D> query(Q q) {
        return this.eyasFrameworkDao.queryEyasFramework(q);
    }

    /**
     * 查询记录数
     *
     * @param q 对象query
     * @return 记录数
     */
    public Integer queryCount(Q q) {
        return this.eyasFrameworkDao.queryCountEyasFramework(q);
    }

    /**
     * 批量更新
     *
     * @param d 对象do
     * @return 更新记录数
     */
    public Integer batchUpdate(D d) {
        return this.eyasFrameworkDao.batchUpdateEyasFramework(d);
    }

    /**
     * 批量删除
     *
     * @param d 对象do
     * @return 删除记录数
     */
    public Integer batchDelete(D d) {
        return this.eyasFrameworkDao.batchDeleteEyasFramework(d);
    }

    /**
     * 业务查询-根据不同的条件查询数据
     *
     * @param q q
     * @return dList
     */
    public List<D> queryByDifferentConditions(Q q) {
        return this.eyasFrameworkDao.queryByDifferentConditionsEyasFramework(q);
    }

    /**
     * 业务查询-根据主键id查询数据
     *
     * @param id id
     * @return D
     */
    public D getInfoById(Long id) {
        return this.eyasFrameworkDao.getInfoByIdEyasFramework(id);
    }

    /**
     * 根据id查询数据-用于乐观锁
     *
     * @param d d
     * @return D
     */
    public D getInfoById(D d) {
        return this.eyasFrameworkDao.getInfoByIdEyasFramework(d);
    }
}
