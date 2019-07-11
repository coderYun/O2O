package com.hly.o2o.dto;
/**
 * 用户授权的token
 * @author hp
 *
 */

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccessToken {

	// 获取到的凭证
	@JsonProperty("access_token")
	private String accessToken;
	// 凭证有效时间:秒
	@JsonProperty("expires_in")
	private String expiresIn;
	// 表示更新令牌，用来获取下一次的访问令牌，这里没有太大的用处
	@JsonProperty("refresh_token")
	private String refreshToken;
	// 该用户在公众号下的唯一表示
	@JsonProperty("openid")
	private String openId;
	// 表示权限范围这里可省略
	@JsonProperty("scope")
	private String scope;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	

}
