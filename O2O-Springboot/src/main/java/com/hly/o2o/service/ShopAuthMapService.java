package com.hly.o2o.service;

import com.hly.o2o.dto.ShopAuthMapExecution;
import com.hly.o2o.entity.ShopAuthMap;
import com.hly.o2o.exceptions.ShopAuthMapOperationException;

public interface ShopAuthMapService {


    /**
     * 根据传入的shopId分页显示店铺授权信息
     * @param ShopId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ShopAuthMapExecution getShopAuthMapByShopId(Long ShopId,Integer pageIndex,Integer pageSize);

    /**
     * 根据shopId获取shopAuthMap
     * @param ShopAuthId
     * @return
     */
    ShopAuthMap getShopAuthMapById(Long ShopAuthId);


    /**
     * 添加一条店铺授权信息
     * @param shopAuthMap
     * @return
     * @throws ShopAuthMapOperationException
     */
    ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;


    /**
     * 更新店铺授权信息
     * @param shopAuthMap
     * @return
     * @throws ShopAuthMapOperationException
     */
    ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;





}
