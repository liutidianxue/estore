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
		//��ȡ�û�������	
		String username = request.getParameter("username");
		String password = MD5Utils.md5(request.getParameter("password"));
		//����service�и����û�����������û��ķ���
		User user = service.findUserByUsernameAndPassword(username,password);
		
		if(user == null){
			request.getSession().setAttribute("msg", "�û��������벻��ȷ");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		//��¼�û�
		request.getSession().setAttribute("user", user);
		//�����ס�û���
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
		//����30�����Զ���¼
		if("true".equals(request.getParameter("autologin"))){
			Cookie autologin = new Cookie("autologin",URLEncoder.encode(user.getUsername()+":"+user.getPassword(), "utf-8"));
			autologin.setPath("/");
			autologin.setMaxAge(3600*24*30);
			response.addCookie(autologin);
		}
		//�ض�����ҳ
		response.sendRedirect("/index.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
