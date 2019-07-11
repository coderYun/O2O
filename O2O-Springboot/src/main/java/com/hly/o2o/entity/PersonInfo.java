package com.hly.o2o.entity;

import java.util.Date;

/**
 * 
 * @author hp
 *用户实体类
 */

public class PersonInfo {
	 private Long userId;
	 private String name;
	 //用户头像
	 private String profileImg;
	 private String email;
	 private String gender;
	 //用户账号状态
	 private Integer enableStatus;
	 //用户身份标识(即是商家还是普通买家 1--顾客 2--店家 3--超级管理员)
	 private Integer userType;
	 private Date createTime;
	 private Date lastEditTime;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProfileImg() {
		return profileImg;
	}
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	
	 
	 
	 

}
