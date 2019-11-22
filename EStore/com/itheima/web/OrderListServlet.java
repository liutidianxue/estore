package com.itheima.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.OrderListForm;
import com.itheima.domain.User;
import com.itheima.factory.BasicFactory;
import com.itheima.service.OrderService;

public class OrderListServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OrderService orderService = BasicFactory.getFactory().getService(OrderService.class);
		//OrderService orderService = null;
		//获取用户id
		User user = (User) request.getSession().getAttribute("user");
		int id = user.getId();
		//调用service中根据用户id查询用户具有的订单的方法
		List<OrderListForm> list = orderService.findOrders(id);
		//存入request域带到页面显示
		request.setAttribute("list", list);
		request.getRequestDispatcher("/orderList.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
