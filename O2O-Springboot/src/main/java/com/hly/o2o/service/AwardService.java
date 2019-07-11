package com.hly.o2o.service;

import com.hly.o2o.dto.AwardExecution;
import com.hly.o2o.dto.ImageHolder;
import com.hly.o2o.entity.Award;
import com.hly.o2o.exceptions.AwardOperationException;

import java.util.List;

public interface AwardService {
    /**
     * 返回奖品列表
     * @param awardCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
        AwardExecution getAwardList(Award awardCondition,int pageIndex,int pageSize);

    /**
     * 根据awardId查询奖品信息
     * @param awardId
     * @return
     */
    Award getAwardById(long awardId);

    /**
     * 添加奖品信息，并添加奖品图片
     * @param award
     * @param thumbnail
     * @return
     */
    AwardExecution addAward(Award award, ImageHolder thumbnail);

    /**
     * 修改奖品信息，并替换原有图片
     * @param award
     * @param thumbnail
     * @return
     */
    AwardExecution modifyAward(Award award, ImageHolder thumbnail);

}
