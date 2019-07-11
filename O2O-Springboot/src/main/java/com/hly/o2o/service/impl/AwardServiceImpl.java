package com.hly.o2o.service.impl;

import com.hly.o2o.dao.AwardDao;
import com.hly.o2o.dto.AwardExecution;
import com.hly.o2o.dto.ImageHolder;
import com.hly.o2o.entity.Award;
import com.hly.o2o.enums.AwardStateEnum;
import com.hly.o2o.exceptions.AwardOperationException;
import com.hly.o2o.service.AwardService;
import com.hly.o2o.util.ImageUtil;
import com.hly.o2o.util.PageCalculator;
import com.hly.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
public class AwardServiceImpl implements AwardService {

    @Autowired
    private AwardDao awardDao;

    @Override
    public AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculatorRowIndex(pageIndex,pageSize);
        List<Award> awardList = awardDao.queryAwardList(awardCondition,rowIndex,pageSize);
        int count = awardDao.queryAwardCount(awardCondition);
        AwardExecution ad = new AwardExecution();
        ad.setAwardList(awardList);
        ad.setCount(count);
        return ad;
    }

    @Override
    public Award getAwardById(long awardId) {
        return awardDao.queryAwardByAwardId(awardId);
    }

    @Override
    @Transactional
    public AwardExecution addAward(Award award, ImageHolder thumbnail) {
        // 空值判断
        if(award!=null && award.getShopId()!=null){
            award.setCreateTime(new Date());
            award.setLastEditTime(new Date());
            // 默认为上架的状态
            award.setEnableStatus(1);
            // 若商品缩略图不为空则添加
            if (thumbnail != null) {
                addThumbnail(award, thumbnail);
            }
            try {
                // 创建商品信息
                int effectedNum = awardDao.insertAward(award);
                if (effectedNum <= 0) {
                    throw new AwardOperationException("创建奖品失败");
                }
            } catch (Exception e) {
                throw new AwardOperationException("创建奖品失败:" + e.toString());
            }
            return new AwardExecution(AwardStateEnum.SUCCESS, award);
        } else {
            // 传参为空则返回空值错误信息
            return new AwardExecution(AwardStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public AwardExecution modifyAward(Award award, ImageHolder thumbnail) {
        if(award!=null && award.getAwardId()!=null){
            award.setLastEditTime(new Date());
            if(thumbnail!=null){
                Award tempAward = awardDao.queryAwardByAwardId(award.getAwardId());
                if(tempAward.getAwardImg()!=null){
                    ImageUtil.deleteFileOrPath(tempAward.getAwardImg());
                }
                // 存储图片流，获取相对路径
                addThumbnail(award,thumbnail);
            }
            try{
                int effectNum = awardDao.updateAward(award);
                if(effectNum<=0){
                    throw new AwardOperationException("更新奖品信息失败");
                }
                return new AwardExecution(AwardStateEnum.SUCCESS,award);
            }catch (Exception e){
                throw new AwardOperationException("更新奖品信息失败"+e.toString());
            }
        }else {
            return new AwardExecution(AwardStateEnum.EMPTY);
        }
    }

    /**
     * 添加缩略图
     * @param award
     * @param thumbnail
     */
    private void addThumbnail(Award award, ImageHolder thumbnail){
        // 获取图片存储路径
        String dest = PathUtil.getShopImagePath(award.getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail,dest);
        award.setAwardImg(thumbnailAddr);
    }

}
