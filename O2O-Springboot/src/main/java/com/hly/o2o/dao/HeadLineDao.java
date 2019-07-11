package com.hly.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hly.o2o.entity.HeadLine;

public interface HeadLineDao {
    /**
     * 根据传入的查询条件(条件名查询头条)
     * @param headLineCondition
     * @return
     */
	List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);
	

}
