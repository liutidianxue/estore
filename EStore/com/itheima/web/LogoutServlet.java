package com.itheima.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(request.getSession(false)!=null){
			//ɱ��session
			request.getSession().invalidate();
			//ɱ���Զ���¼cookie
			Cookie autologin = new Cookie("autologin","");
			autologin.setPath("/");
			autologin.setMaxAge(0);
			response.addCookie(autologin);
		}
		//�ض���ص���ҳ
		response.sendRedirect("/index.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
