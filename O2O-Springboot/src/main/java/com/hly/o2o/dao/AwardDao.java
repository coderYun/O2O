package com.hly.o2o.dao;

import com.hly.o2o.entity.Award;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AwardDao {

    /**
     * 根据传入的条件查询奖品列表
     *
     * @param awardCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Award> queryAwardList(@Param("awardCondition") Award awardCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 配合queryAwardList相同条件下查询奖品的数量
     * @param awardCondition
     * @return
     */
    int queryAwardCount(@Param("awardCondition") Award awardCondition);

    /**
     * 根据awardId查询对应的奖品
     * @param awardId
     * @return
     */
    Award queryAwardByAwardId(long awardId);

    /**
     * 添加奖品
     * @param award
     * @return
     */
    int insertAward(@Param("award") Award award);

    /**
     * 更新奖品
     * @param award
     * @return
     */
    int updateAward(Award award);

    /**
     * 删除奖品(根据奖品ID和其对应所在的店铺，因为不能删除其他店铺下的奖品)
     * @param awardId
     * @param shopId
     * @return
     */
    int deleteAward(@Param("awardId") long awardId,@Param("shopId") long shopId);
}
