package com.our.yun.controller.interceptor;

import com.our.yun.utils.UserUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String url = request.getRequestURI();
		if (url.indexOf("login.action") >= 0 || url.indexOf("register.action") >= 0 || url.indexOf("share.action") >= 0 || url.indexOf("getShareFiles.action") >= 0 || url.indexOf("download.action") >= 0 || url.indexOf("loginForApp.action") >= 0 || url.indexOf("getAppFiles.action") >= 0|| url.indexOf("uploadForApp.action") >= 0) {
			return true;
		}

		String username = UserUtils.getUsername(request);

		if (username != null) {
			return true;
		}

		response.sendRedirect("/user/login.action");
		return false;
	}

}
