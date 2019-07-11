package com.hly.o2o.interceptor.shopadmin;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hly.o2o.entity.PersonInfo;

/**
 * 店家管理系统登录的拦截器
 * 
 * @author hp
 *
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 主要做事前拦截，即用户操作发生前,改写preHandle的逻辑，进行拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从session取出相应的user用户信息来
		Object object = request.getSession().getAttribute("user");
		if (object != null) {
			// 若用户信息不为空，则转化为相对应的PersonInfo实体类
			PersonInfo user = (PersonInfo) object;
			// 做空值判断,确保user不为空且该账号的可用状态为1，并且用户类型为店家

			if (user != null && user.getUserId() != null && user.getUserId() > 0 && user.getEnableStatus() == 1) {
				//通过验证后返回ture，用户才可以进行后续的操作
				return true;

			}
	
		}
		//若不满足条件直接跳到登录页面
		PrintWriter out=response.getWriter();
		out.println("<html>");
		out.println("<script>");
		out.println("window.open('"+request.getContextPath()+"/local/login?usertype=2','_self')");
		out.println("</script>");
		out.println("</html>");
	
		return false;
	}

}
