package com.itheima.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.Order;
import com.itheima.domain.OrderItem;
import com.itheima.domain.Product;
import com.itheima.domain.User;
import com.itheima.factory.BasicFactory;
import com.itheima.service.OrderService;

public class AddOrderServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OrderService service = BasicFactory.getFactory().getService(OrderService.class);
		//封装order信息
//		 String id;	uuid
//		 double money;	总价
//		 String receiverinfo;	收货地址
//		 int paystate;	支付状态
//		 Timestamp ordertime;	订单时间，自动生成
//		 private int user_id;
//		 List<OrderItem> list;	
		Order order = new Order();
		order.setId(UUID.randomUUID().toString());
		
		double money =0;
		Map<Product,Integer> cartmap =(Map<Product, Integer>) request.getSession().getAttribute("cartmap");
		List<OrderItem> list = new ArrayList<OrderItem>();
		for(Map.Entry<Product,Integer> entry : cartmap.entrySet()){
			money += entry.getKey().getPrice()*entry.getValue();
			OrderItem item = new OrderItem();
			item.setOrder_id(order.getId());
			item.setProduct_id(entry.getKey().getId());
			item.setBuynum(entry.getValue());
			list.add(item);
		}
		order.setMoney(money);
		order.setList(list);
		
		String receiverinfo = request.getParameter("receiverinfo");
		order.setReceiverinfo(receiverinfo);
		
		order.setPaystate(0);
		
		User user = (User) request.getSession().getAttribute("user");
		order.setUser_id(user.getId());
		
		//调用service方法添加order
		service.addOrder(order);
		
		//清空购物车
		cartmap.clear();
		
		//回到主页(与实际情况不符，这个项目就当作是这样吧，需要你回到主页去支付)
		response.getWriter().write("订单生成成功，3秒后回到主页...");
		response.setHeader("Refresh", "3;url=/index.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
