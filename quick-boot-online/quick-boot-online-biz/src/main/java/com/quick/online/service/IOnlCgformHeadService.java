package com.quick.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.online.dto.OnlCgformHeadDetailsVO;
import com.quick.online.dto.OnlCgformHeadVO;
import com.quick.online.dto.SyncedTableInfoDTO;
import com.quick.online.entity.OnlCgformHead;

public interface IOnlCgformHeadService extends IService<OnlCgformHead> {
    OnlCgformHeadDetailsVO getOnlCgformHeadDetails(Long id);
    OnlCgformHeadVO details(Long id);

    OnlCgformHead selectByTableName(String tableName, String dsName);
    Boolean syncedTableInfo(SyncedTableInfoDTO syncedTableInfoDTO);

    OnlCgformHeadVO bizSave(OnlCgformHeadVO saveDTO);
    Boolean bizUpdateById(OnlCgformHeadVO saveDTO);

}
