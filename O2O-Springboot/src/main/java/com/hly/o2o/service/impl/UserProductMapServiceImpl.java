package com.hly.o2o.service.impl;

import com.hly.o2o.dao.UserProductMapDao;
import com.hly.o2o.dao.UserShopMapDao;
import com.hly.o2o.dto.UserProductMapExecution;
import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.entity.UserProductMap;
import com.hly.o2o.entity.UserShopMap;
import com.hly.o2o.enums.UserProductMapStateEnum;
import com.hly.o2o.exceptions.UserProductMapOperationException;
import com.hly.o2o.service.UserProductMapService;
import com.hly.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserProductMapServiceImpl implements UserProductMapService {
    @Autowired
    private UserProductMapDao userProductMapDao;
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    public UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex, Integer pageSize) {
        //空值判断
        if (userProductCondition != null && pageIndex != null && pageIndex != null) {
            //页转行
            int beginIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
            //依据条件分页查询列表
            List<UserProductMap> productMapList = userProductMapDao.queryUserProductMapList(userProductCondition, beginIndex, pageSize);
            //依据查询条件分页查出总数
            int count = userProductMapDao.queryUserProductMapCount(userProductCondition);
            UserProductMapExecution se = new UserProductMapExecution();
            se.setUserProductMapList(productMapList);
            se.setCount(count);
            return se;

        } else {
            return null;
        }
    }

    /**
     * 添加消费记录
     *
     * @param userProductMap
     * @return
     * @throws UserProductMapOperationException
     */
    @Override
    @Transactional
    public UserProductMapExecution addUserProductMap(UserProductMap userProductMap) throws UserProductMapOperationException {
        //空值判断
        if (userProductMap != null && userProductMap.getUser().getUserId() != null
                && userProductMap.getShop().getShopId() != null
                && userProductMap.getOperator().getUserId() != null){
            //设定默认值
            userProductMap.setCreateTime(new Date());

            try{

                //添加消费记录
                int effectNum = userProductMapDao.insertUserProductMap(userProductMap);
                if(effectNum<0){
                    throw new UserProductMapOperationException("添加消费记录失败");
                }
                //若本次消费能够积分
                if(userProductMap.getPoint()!=null&&userProductMap.getPoint()>0){
                    //查询顾客是否消费过
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userProductMap.getUser().getUserId(), userProductMap.getShop().getShopId());
                    if(userShopMap!=null&&userShopMap.getShop().getShopId()!=null){
                        //若之前消费过,则进行积分加操作
                        userShopMap.setPoint(userProductMap.getPoint()+userShopMap.getPoint());
                        effectNum=userShopMapDao.updateUserShopMapPoint(userShopMap);
                        if(effectNum<=0){
                            throw new UserProductMapOperationException("更新积分失败");
                        }

                    }else{
                        // 在店铺没有过消费记录，添加一条店铺积分信息(就跟初始化会员一样)
                        userShopMap = compactUserShopMap4Add(userProductMap.getUser().getUserId(),
                                userProductMap.getShop().getShopId(), userProductMap.getPoint());
                        effectNum = userShopMapDao.insertUserShopMap(userShopMap);
                        if (effectNum <= 0) {
                            throw new UserProductMapOperationException("创建积分失败");
                        }
                    }
                    return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS, userProductMap);
                }
            }catch (Exception e) {
                throw new UserProductMapOperationException("添加消费记录失败 :" + e.toString());
            }
        }
        return new UserProductMapExecution(UserProductMapStateEnum.NULL_ERROR);
    }
    /**
     * 封装顾客积分信息
     *
     * @param userId
     * @param shopId
     * @param point
     * @return
     */
    private UserShopMap compactUserShopMap4Add(Long userId, Long shopId, Integer point) {
        UserShopMap userShopMap = null;
        // 空值判断
        if (userId != null && shopId != null) {
            userShopMap = new UserShopMap();
            PersonInfo customer = new PersonInfo();
            customer.setUserId(userId);
            Shop shop = new Shop();
            shop.setShopId(shopId);
            userShopMap.setUser(customer);
            userShopMap.setShop(shop);
            userShopMap.setCreateTime(new Date());
            userShopMap.setPoint(point);
        }
        return userShopMap;
    }
}
