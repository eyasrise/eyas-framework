package com.eyas.framework.service.intf;

import com.eyas.framework.entity.DipperInfo;
import com.eyas.framework.entity.DipperInfoQuery;

import java.util.List;

public interface DipperInfoService {
    void insertDto(DipperInfo dipperInfo);

//    Integer updateDto(DipperInfo dipperInfo);

    Integer updateDto(Long id, DipperInfo dipperInfo);

    Integer deleteDto(String id);

    List<DipperInfo> queryDto(DipperInfoQuery dipperInfoQuery);
}
