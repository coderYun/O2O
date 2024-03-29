package com.hly.o2o.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.hly.o2o.dao.ShopAuthMapDao;
import com.hly.o2o.entity.ShopAuthMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.hly.o2o.dao.ShopDao;
import com.hly.o2o.dto.ImageHolder;
import com.hly.o2o.dto.ShopExecution;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.enums.ShopStateEnum;
import com.hly.o2o.exceptions.ShopOperationException;
import com.hly.o2o.service.ShopService;
import com.hly.o2o.util.ImageUtil;
import com.hly.o2o.util.PageCalculator;
import com.hly.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;

    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
        // 判断新插入的shop是否为空
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.Null_SHOP);
        }
        try {
            // 给店铺信息赋予初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            int effectNum = shopDao.insertShop(shop);
            // 判断一下店铺是否插入成功
            if (effectNum <= 0) {
                throw new RuntimeException("店铺创建失败");
            } else {
                // 判断一下注册店铺的人的图片是否为空
                if (thumbnail.getImage() != null) {
                    // 存储图片
                    try {
                        addShopImg(shop, thumbnail);
                    } catch (Exception e) {
                        throw new ShopOperationException("add shopImg error:" + e.getMessage());
                    }
                    // 更新店铺的图片地址
                    int effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败!");

                    }
                    //执行增加shopAuthMap操作
                    ShopAuthMap shopAuthMap = new ShopAuthMap();
                    shopAuthMap.setEmployee(shop.getOwner());
                    shopAuthMap.setShop(shop);
                    shopAuthMap.setTitle("店家");
                    shopAuthMap.setTitleFlag(0);
                    shopAuthMap.setCreateTime(new Date());
                    shopAuthMap.setLastEditTime(new Date());
                    shopAuthMap.setEnableStatus(1);
                    try {
                        int i = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                        if (i <= 0) {
                            throw new ShopOperationException("授权创建失败");
                        }
                    } catch (Exception e) {

                        throw new ShopOperationException("insert ShopAuthMap error" + e.toString());
                    }
                }

            }

        } catch (Exception e) {
            throw new ShopOperationException("add shop error!" + e.getMessage());

        }

        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    private void addShopImg(Shop shop, ImageHolder thumbnail) {
        // 获取shop图片目录的相对路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        shop.setShopImg(shopImgAddr);
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail)
            throws ShopOperationException {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.Null_SHOP);

        } else {
            // 判断是否需要更新图片
            try {
                if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())) {
                    // 先删除原来的店铺图片
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopImg() != null) {
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop, thumbnail);
                }

                // 更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if (effectedNum <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            } catch (Exception e) {
                throw new ShopOperationException("modify shop error!" + e.getMessage());
            }

        }
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        //将pageIndex转换成rowIndex
        int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        //根据查询条件，返回店铺总数
        int shopCount = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList != null) {
            se.setShopList(shopList);
            se.setCount(shopCount);
        } else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }

        return se;
    }
}
