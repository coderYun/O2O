package com.hly.o2o.service.impl;

import com.hly.o2o.dao.UserAwardMapDao;
import com.hly.o2o.dao.UserShopMapDao;
import com.hly.o2o.dto.UserAwardMapExecution;
import com.hly.o2o.entity.UserAwardMap;
import com.hly.o2o.entity.UserShopMap;
import com.hly.o2o.enums.UserAwardMapStateEnum;
import com.hly.o2o.exceptions.UserAwardMapOperationException;
import com.hly.o2o.service.UserAwardMapService;
import com.hly.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {
    @Autowired
    private UserAwardMapDao userAwardMapDao;
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    public UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize) {
        if (userAwardCondition != null && pageIndex >= 0 && pageSize >= 0) {
            // 页转行
            int beginIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
            // 根据传入的查询条件分页返回用户积分列表信息
            List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardCondition, beginIndex, pageSize);
            // 返回总数
            int count = userAwardMapDao.queryUserAwardMapCount(userAwardCondition);
            UserAwardMapExecution ud = new UserAwardMapExecution();
            ud.setUserAwardMapList(userAwardMapList);
            ud.setCount(count);
            return ud;
        }
        return null;
    }

    @Override
    public UserAwardMap getUserAwardMapById(long userAwardMapId) {
        return userAwardMapDao.queryUserAwardMapById(userAwardMapId);
    }

    @Override
    @Transactional
    public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
        if (userAwardMap != null && userAwardMap.getUser() != null && userAwardMap.getShop() != null && userAwardMap.getShop().getShopId() != null) {
            userAwardMap.setCreateTime(new Date());
            userAwardMap.setUsedStatus(0);
            try {
                int effectNum = 0;
                if (userAwardMap.getPoint() != null && userAwardMap.getPoint() > 0) {
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userAwardMap.getUser().getUserId(),
                            userAwardMap.getShop().getShopId());
                    if (userAwardMap != null) {
                        if (userShopMap.getPoint() >= userAwardMap.getPoint()) {
                            userShopMap.setPoint(userShopMap.getPoint() - userAwardMap.getPoint());
                            effectNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
                            if (effectNum <= 0) {
                                throw new UserAwardMapOperationException("更新积分信息失败");
                            }
                        } else {
                            throw new UserAwardMapOperationException("积分不足，无法领取");
                        }
                    } else {
                        throw new UserAwardMapOperationException("在本店没有积分，无法兑换奖品");
                    }
                }
                effectNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
                if (effectNum <= 0)
                    throw new UserAwardMapOperationException("领取奖励失败");
                return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
            } catch (Exception e) {
                throw new UserAwardMapOperationException("领取奖励失败" + e.toString());
            }
        } else {
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USER_AWARD_MAP);
        }
    }

    @Override
    @Transactional
    public UserAwardMapExecution modifyUserrAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
// 空值判断，主要是检查传入的userAwardId以及领取状态是否为空
        if (userAwardMap == null || userAwardMap.getUserAwardId() == null || userAwardMap.getUsedStatus() == null) {
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USER_AWARD_MAP);
        } else {
            try {
                // 更新可用状态
                int effectedNum = userAwardMapDao.updateUserAwardMap(userAwardMap);
                if (effectedNum <= 0) {
                    return new UserAwardMapExecution(UserAwardMapStateEnum.FAIL);
                } else {
                    return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
                }
            } catch (Exception e) {
                throw new UserAwardMapOperationException("modifyUserAwardMap error: " + e.getMessage());
            }
        }
    }

}

