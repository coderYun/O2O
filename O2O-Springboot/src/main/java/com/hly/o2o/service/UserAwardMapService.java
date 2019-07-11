package com.hly.o2o.service;

import com.hly.o2o.dto.UserAwardMapExecution;
import com.hly.o2o.entity.UserAwardMap;
import com.hly.o2o.exceptions.UserAwardMapOperationException;

public interface  UserAwardMapService {
    /**
     * 根据传入的查询条件分页获取列表及总数
     * @param userAwardCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize);

    /**
     * 根据传入的ID获取映射信息
     * @param userAwardMapId
     * @return
     */
    UserAwardMap getUserAwardMapById(long userAwardMapId);

    /**
     * 领取奖品，添加记录
     * @param userAwardMap
     * @return
     */
    UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap)throws UserAwardMapOperationException;

    /**
     * 修改奖品领取状态
     * @param userAwardMap
     * @return
     */
    UserAwardMapExecution modifyUserrAwardMap(UserAwardMap userAwardMap)throws UserAwardMapOperationException;
}
