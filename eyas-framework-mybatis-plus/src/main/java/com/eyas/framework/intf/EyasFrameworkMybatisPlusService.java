package com.eyas.framework.intf;

import java.util.List;

public interface EyasFrameworkMybatisPlusService<D,Q> {

    /**
     * 乐观锁更新
     *
     * @param id 更新id
     * @param d 更新数据
     * @return 更新结果
     */
    Integer updateDto(Long id, D d);

    List<D> queryDto(Q q);
}
