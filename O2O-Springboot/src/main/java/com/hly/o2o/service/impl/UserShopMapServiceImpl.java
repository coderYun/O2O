package com.hly.o2o.service.impl;

import com.hly.o2o.dao.UserShopMapDao;
import com.hly.o2o.dto.UserProductMapExecution;
import com.hly.o2o.dto.UserShopMapExecution;
import com.hly.o2o.entity.UserProductMap;
import com.hly.o2o.entity.UserShopMap;
import com.hly.o2o.service.UserProductMapService;
import com.hly.o2o.service.UserShopMapService;
import com.hly.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserShopMapServiceImpl implements UserShopMapService {
    @Autowired
    private UserShopMapDao userShopMapDao;


    @Override
    public UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex, int pageSize) {
        //空值判断
        if (userShopMapCondition != null && (pageIndex > -1) && (pageSize > -1)) {
            //页数转换为行数
            int beginIndex=PageCalculator.calculatorRowIndex(pageIndex,pageSize);
            //依据条件分页查出用户积分列表
            List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopMapCondition, beginIndex, pageSize);
            //返回总数
            int count = userShopMapDao.queryUserShopMapCount(userShopMapCondition);
            UserShopMapExecution se=new UserShopMapExecution();
            se.setCount(count);
            se.setUserShopMapList(userShopMapList);
            return se;
        }else {
            return null;
        }

    }

    @Override
    public UserShopMap getUserShopMap(long userId, long shopId) {
        return userShopMapDao.queryUserShopMap(userId,shopId);
    }
}
