package com.hly.o2o.entity;

import java.util.Date;

//奖品
public class Award {
	// 主键ID
	private Long awardId;
	//奖品名
	private String awardName;
	//奖品描述
	private String awardDesc;
	//奖品图片地址
	private String awardImg;
	//需要多少积分去兑换奖品
	private Integer point;
	//权重，越大越靠前
	private Integer priority;
	//创建时间
	private Date createTime;
	//最新一次更新的时间
	private Date lastEditTime;
	//可用状态 1可用  0不可用
	private Integer enableStatus;
	//属于哪个店铺
	private Long shopId;


	public Long getAwardId() {
		return awardId;
	}

	public String getAwardName() {
		return awardName;
	}

	public String getAwardDesc() {
		return awardDesc;
	}

	public String getAwardImg() {
		return awardImg;
	}

	public Integer getPoint() {
		return point;
	}

	public Integer getPriority() {
		return priority;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getLastEditTime() {
		return lastEditTime;
	}

	public Integer getEnableStatus() {
		return enableStatus;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setAwardId(Long awardId) {
		this.awardId = awardId;
	}

	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}

	public void setAwardDesc(String awardDesc) {
		this.awardDesc = awardDesc;
	}

	public void setAwardImg(String awardImg) {
		this.awardImg = awardImg;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
}
