package com.itheima.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.itheima.domain.User;
import com.itheima.factory.BasicFactory;
import com.itheima.service.UserService;
import com.itheima.util.MD5Utils;

public class RegistServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			UserService service =  BasicFactory.getFactory().getService(UserService.class);
			//У����֤��
			String valistr1 = request.getParameter("valistr");
			String valistr2 = (String) request.getSession().getAttribute("valistr");
			if(valistr1 == null || valistr2 == null|| !valistr1.equals(valistr2)){
				request.setAttribute("msg","��֤�����");
				request.getRequestDispatcher("/regist.jsp").forward(request, response);
				return;
			}
			//��װ���ݲ�У��
			User user = new User();
			BeanUtils.populate(user, request.getParameterMap());
			user.setPassword(MD5Utils.md5(user.getPassword()));
			//����service����ע���û�
			service.regist(user);
			
			//��¼�û����ض�����ҳ
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
