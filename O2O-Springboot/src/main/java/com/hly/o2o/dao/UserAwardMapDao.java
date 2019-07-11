package com.hly.o2o.dao;

import com.hly.o2o.entity.UserAwardMap;
import org.apache.catalina.mbeans.UserMBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAwardMapDao {

    /**
     * 根据传入的查询条件分页查询用户兑换奖品记录的信息
     * @param userAwardCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<UserAwardMap> queryUserAwardMapList(@Param("userAwardCondition") UserAwardMap userAwardCondition,
                                             @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 配合queryUserAwardMapList查询相同查询条件下的奖品兑换记录数
     * @param userAwardCondition
     * @return
     */
    int queryUserAwardMapCount(@Param("userAwardCondition") UserAwardMap userAwardCondition);

    /**
     * 根据awardId查询相应的奖品兑换记录信息
     * @param userAwardId
     * @return
     */
    UserAwardMap queryUserAwardMapById(long userAwardId);

    /**
     * 添加一条奖品兑换记录
     * @param userAwardMap
     * @return
     */
    int insertUserAwardMap(UserAwardMap userAwardMap);

    /**
     * 更新一条奖品兑换记录
     * @param userAwardMap
     * @return
     */
    int updateUserAwardMap(UserAwardMap userAwardMap);


}
