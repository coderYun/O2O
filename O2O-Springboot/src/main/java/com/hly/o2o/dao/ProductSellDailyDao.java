package com.hly.o2o.dao;

import com.hly.o2o.entity.ProductSellDaily;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ProductSellDailyDao {
    /**
     * 根据查询条件返回产品日销售的统计列表
     * @param productSellDailyCondition
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductSellDaily> queryProductSellDailyList(@Param("productSellDailyCondition") ProductSellDaily productSellDailyCondition,
                                                     @Param("beginTime")Date beginTime,@Param("endTime") Date endTime);


    /**
     * 统计平台所有商品的日销售量
     * @return
     */
    int insertProductSellDaily();

    /**
     * 统计当天平台没有销量的商品，将它们销量置为0，补全它们的信息
     * @return
     */
    int insertDefaultProductSellDaily();

}
