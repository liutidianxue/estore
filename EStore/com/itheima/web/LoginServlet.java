package com.itheima.web;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;
import com.itheima.factory.BasicFactory;
import com.itheima.service.UserService;
import com.itheima.util.MD5Utils;

public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserService service = BasicFactory.getFactory().getService(UserService.class);
		//获取用户名密码	
		String username = request.getParameter("username");
		String password = MD5Utils.md5(request.getParameter("password"));
		//调用service中根据用户名密码查找用户的方法
		User user = service.findUserByUsernameAndPassword(username,password);
		
		if(user == null){
			request.getSession().setAttribute("msg", "用户名或密码不正确");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		//登录用户
		request.getSession().setAttribute("user", user);
		//处理记住用户名
		if("true".equals(request.getParameter("remname"))){
			Cookie remname = new Cookie("remname",URLEncoder.encode(user.getUsername(), "utf-8"));
			remname.setPath("/");
			remname.setMaxAge(3600*24*30);
			response.addCookie(remname);
		}else{
			Cookie remname = new Cookie("remname","");
			remname.setPath("/");
			remname.setMaxAge(0);
			response.addCookie(remname);
		}
		//处理30天内自动登录
		if("true".equals(request.getParameter("autologin"))){
			Cookie autologin = new Cookie("autologin",URLEncoder.encode(user.getUsername()+":"+user.getPassword(), "utf-8"));
			autologin.setPath("/");
			autologin.setMaxAge(3600*24*30);
			response.addCookie(autologin);
		}
		//重定向到主页
		response.sendRedirect("/index.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
