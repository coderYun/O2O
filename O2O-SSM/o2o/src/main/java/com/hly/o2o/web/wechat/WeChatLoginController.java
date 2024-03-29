package com.hly.o2o.web.wechat;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hly.o2o.dto.UserAccessToken;
import com.hly.o2o.dto.WeChatUser;
import com.hly.o2o.dto.WechatAuthExecution;
import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.WechatAuth;
import com.hly.o2o.enums.WechatAuthStateEnum;
import com.hly.o2o.service.PersonInfoService;
import com.hly.o2o.service.WechatAuthService;
import com.hly.o2o.util.wechat.WeChatUtil;

/**
 * 获取关注了微信公众号之后的微信用户信息的接口
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9b66f571a856018b&redirect_uri=http://47.100.232.113/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 
 * 
 * 
 * @author hp
 *
 */
@Controller
@RequestMapping("/wechatlogin")
public class WeChatLoginController {

	private static Logger log = LoggerFactory.getLogger(WeChatLoginController.class);
	private static final String FRONTEND = "1";
	private static final String SHOPEND = "2";
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("weixin login get...");
		// 获取从微信公众号传递过来的code，通过code获取到用户的信息
		String code = request.getParameter("code");
		// 这里的state用于传递我们自定义的信息，方便调用，这里可不用
		String roleType = request.getParameter("state");
		log.debug("weixin login code:" + code);
		WeChatUser user = null;
		WechatAuth auth = null;
		String openId = null;
		if (null != code) {
			UserAccessToken token;
			try {
				// 通过code获取到access_token
				token = WeChatUtil.getUserAccessToken(code);
				log.debug("weixin login token:" + token.toString());
				// 通过access_token获取到token
				String accessToken = token.getAccessToken();
				// 通过token获取到openId
				openId = token.getOpenId();
				// 通过token和openId获取到用户昵称等信息
				user = WeChatUtil.getUserInfo(accessToken, openId);
				log.debug("weixin login user:" + user.toString());
				request.getSession().setAttribute("openId", openId);
				auth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
				e.printStackTrace();
			}
		}
		/**
		 * To DO 获取到openId后，可以通过他与数据库判断该微信账号是否在我们的网站里面有对应的账号
		 * 没有的话自动创建上，直接实现微信与我们网站的对接
		 * 
		 * 
		 */
		// 如果微信账号为空，那就给他创建用户信息
		if (auth == null) {
			PersonInfo personInfo = WeChatUtil.getPersonInfoFromRequest(user);
			auth=new WechatAuth();
			auth.setOpenId(openId);
			if (FRONTEND.equals(roleType)) {// 如果是普通用户
				personInfo.setUserType(1);
			} else { // 如果是店家
				personInfo.setUserType(2);
			}
			auth.setPersonInfo(personInfo);

			// 设置完了就给他注册
			WechatAuthExecution we = wechatAuthService.register(auth);
			if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
				return null;
			} else {
				personInfo = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
				request.setAttribute("user", personInfo);
			}
		}
		// 如果是用户点击的是前端展示系统按钮则进入前端展示系统
		if (FRONTEND.equals(roleType)) {
			return "frontend/index";
		} else { 
			return "shop/shoplist";
		}

	}
}
