package com.hly.o2o.service.impl;

import com.hly.o2o.dao.ShopAuthMapDao;
import com.hly.o2o.dto.ShopAuthMapExecution;
import com.hly.o2o.entity.ShopAuthMap;
import com.hly.o2o.enums.ShopAuthMapStateEnum;
import com.hly.o2o.exceptions.ShopAuthMapOperationException;
import com.hly.o2o.exceptions.ShopCategoryOperationException;
import com.hly.o2o.service.ShopAuthMapService;
import com.hly.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;


    @Override
    public ShopAuthMapExecution getShopAuthMapByShopId(Long ShopId, Integer pageIndex, Integer pageSize) {

        //非空判断
        if (ShopId != null && pageIndex != null && pageSize != null) {
            //页转行
            int beginIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);

            //查询返回店铺授权信息列表
            List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(ShopId, beginIndex, pageSize);
            //返回总数
            int count = shopAuthMapDao.queryShopAuthCountByShopId(ShopId);
            ShopAuthMapExecution se = new ShopAuthMapExecution();
            se.setShopAuthMapList(shopAuthMapList);
            se.setCount(count);
            return se;

        } else {
            return null;
        }
    }

    @Override
    public ShopAuthMap getShopAuthMapById(Long ShopAuthId) {
        return shopAuthMapDao.queryShopAuthMapById(ShopAuthId);
    }

    @Override
    @Transactional
    public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
        //空值判断，主要是对员工的id和店铺ID进行校验
        if (shopAuthMap != null && shopAuthMap.getShop() != null &&
                shopAuthMap.getShop().getShopId() != null && shopAuthMap.getEmployee() != null
                && shopAuthMap.getEmployee().getUserId() != null) {
            shopAuthMap.setCreateTime(new Date());
            shopAuthMap.setLastEditTime(new Date());
            shopAuthMap.setEnableStatus(1);
            shopAuthMap.setTitleFlag(0);
            try {
                //添加授权信息
                int effectNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                if (effectNum <= 0) {
                    throw new ShopAuthMapOperationException("店铺授权失败!");
                }
                return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);

            } catch (Exception e) {
                throw new ShopCategoryOperationException("添加授权失败" + e.toString());
            }
        } else {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_INFO);

        }
    }

    @Override
    @Transactional
    public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
        //空值判断，主要是对授权ID进行校验
        if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
            try {
                int effectNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
                if (effectNum <= 0) {
                    return new ShopAuthMapExecution(ShopAuthMapStateEnum.INNER_ERROR);
                } else {
                    return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
                }
            } catch (Exception e) {
                throw new ShopAuthMapOperationException("更新失败" + e.toString());
            }
        } else {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_ID);
        }
    }
}
