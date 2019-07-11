package com.hly.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
	public static boolean checkVerifyCode(HttpServletRequest request){
		String verifyCodeExpected=(String)request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		//在这李先写一个获取name看能不能行什么》。。。
		String name=HttpServletRequestUtil.getString(request, "userName");
		String verifyCodeActual=HttpServletRequestUtil.getString(request, "verifyCodeActual");
		if(verifyCodeActual==null||!verifyCodeActual.equals(verifyCodeExpected)){
			return false;
		}
		return true;
	}

}
