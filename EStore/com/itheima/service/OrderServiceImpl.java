package com.itheima.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;


import com.itheima.dao.OrderDao;
import com.itheima.dao.ProductDao;
import com.itheima.dao.UserDao;
import com.itheima.domain.Order;
import com.itheima.domain.OrderItem;
import com.itheima.domain.OrderListForm;
import com.itheima.domain.Product;
import com.itheima.domain.SaleInfo;
import com.itheima.domain.User;
import com.itheima.factory.BasicFactory;

public class OrderServiceImpl implements OrderService {
	OrderDao orderDao = BasicFactory.getFactory().getDao(OrderDao.class);
	ProductDao productDao = BasicFactory.getFactory().getDao(ProductDao.class);
	UserDao	userDao = BasicFactory.getFactory().getDao(UserDao.class);
	public void addOrder(Order order) {
		try {
			// 生成订单
			orderDao.addOrder(order);
			// 生成订单项/扣除商品数量
			for (OrderItem item : order.getList()) {
				orderDao.addOrderItem(item);
				productDao.delPnum(item.getProduct_id(), item.getBuynum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

//	 传进来的是用户id
//	 private String id; 订单号（订单的属性）
//	 private double money; 总价（订单的属性）
//	 private int paystate; 支付状态（订单的属性）
//	 private Timestamp ordertime; 订单时间（订单的属性）（前4个找到订单beanUtils，copy一下）
//	 private String username; 用户名 （通过用户id找到该用户得到用户名）
//	 private Map<Product,Integer>
//	 prodMap;（下面会找到订单项记录，商品id和购买数量，通过商品id可以找到商品，存入map中即可）
	public List<OrderListForm> findOrders(int user_id) {
		try {
			// 根据用户id找出该用户的所有订单
			List<Order> list = orderDao.findOrders(user_id);
			// 可以通过订单找到订单号，通过订单号找到订单项记录集合,遍历订单项
			// 记录得到商品id（用来获取商品对象）和购买的数量
			// 存入Map<Product,Integer>中
			List<OrderListForm> olflist = new ArrayList<OrderListForm>();
			for (Order order : list) {
				OrderListForm olf = new OrderListForm();
				//封装了4个属性了
				BeanUtils.copyProperties(olf, order);
				//封装了用户名
				User user = userDao.finUserById(order.getUser_id());
				olf.setUsername(user.getUsername());
				//封装Map<Product,Integer>
				String order_id = order.getId();
				List<OrderItem> ois = orderDao.findOrderItems(order_id);
				Map<Product,Integer> prodMap = new HashMap<Product, Integer>();
				for (OrderItem oi : ois) {
					String product_id = oi.getProduct_id();
					Product prod = productDao.findProdByID(product_id);
					prodMap.put(prod, oi.getBuynum());
				}
				olf.setProdMap(prodMap);
				
				olflist.add(olf);
			}
			return olflist;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	//传进来的是订单号
	public void delOrderById(String id) {
		//根据订单号找到订单项记录，找到购买的数量，订单项记录得到的每个商品id得到product，pnum+回去
		List<OrderItem> ois = orderDao.findOrderItems(id);
		for (OrderItem oi : ois) {
			productDao.addProdNum(oi.getProduct_id(),oi.getBuynum());
		}
		//删除订单项记录
		orderDao.delOrderItemById(id);
		
		//删除订单
		orderDao.delOrderById(id);
		
	}
	public List<SaleInfo> saleList() {
		return orderDao.saleList();
	}
}
