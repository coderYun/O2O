package com.hly.o2o.service;

import com.hly.o2o.entity.ProductSellDaily;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public interface ProductSellDailyService {

    /**
     * 定时对所有的店铺产品的销量进行统计
     */
    void dailyCalculate();


    /**
     * 按查询条件返回商品日销售统计列表
     *
     * @param productSellDailyCondition
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime);
}
