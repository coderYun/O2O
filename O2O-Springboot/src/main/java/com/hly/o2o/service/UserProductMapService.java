package com.hly.o2o.service;

import com.hly.o2o.dto.UserProductMapExecution;
import com.hly.o2o.entity.UserProductMap;
import com.hly.o2o.exceptions.UserProductMapOperationException;

public interface UserProductMapService {

    /**
     * 根据传入的条件分页查出用户消费信息列表
     * @param userProductCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex, Integer pageSize);


    /**
     * t添加消费记录
     * @param userProductMap
     * @return
     * @throws UserProductMapOperationException
     */
    UserProductMapExecution addUserProductMap(UserProductMap userProductMap) throws UserProductMapOperationException;


}
