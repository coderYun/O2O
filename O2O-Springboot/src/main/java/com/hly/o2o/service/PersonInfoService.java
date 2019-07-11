package com.hly.o2o.service;

import com.hly.o2o.entity.PersonInfo;

public interface PersonInfoService {

    /**
     * 通过用户ID查询用户的信息
     * @param userId
     * @return
     */
	PersonInfo getPersonInfoById(Long userId);
}
