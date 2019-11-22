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
		//��װorder��Ϣ
//		 String id;	uuid
//		 double money;	�ܼ�
//		 String receiverinfo;	�ջ���ַ
//		 int paystate;	֧��״̬
//		 Timestamp ordertime;	����ʱ�䣬�Զ�����
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
		
		//����service�������order
		service.addOrder(order);
		
		//��չ��ﳵ
		cartmap.clear();
		
		//�ص���ҳ(��ʵ����������������Ŀ�͵����������ɣ���Ҫ��ص���ҳȥ֧��)
		response.getWriter().write("�������ɳɹ���3���ص���ҳ...");
		response.setHeader("Refresh", "3;url=/index.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
