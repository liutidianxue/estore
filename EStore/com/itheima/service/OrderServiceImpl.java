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
			// ���ɶ���
			orderDao.addOrder(order);
			// ���ɶ�����/�۳���Ʒ����
			for (OrderItem item : order.getList()) {
				orderDao.addOrderItem(item);
				productDao.delPnum(item.getProduct_id(), item.getBuynum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

//	 �����������û�id
//	 private String id; �����ţ����������ԣ�
//	 private double money; �ܼۣ����������ԣ�
//	 private int paystate; ֧��״̬�����������ԣ�
//	 private Timestamp ordertime; ����ʱ�䣨���������ԣ���ǰ4���ҵ�����beanUtils��copyһ�£�
//	 private String username; �û��� ��ͨ���û�id�ҵ����û��õ��û�����
//	 private Map<Product,Integer>
//	 prodMap;��������ҵ��������¼����Ʒid�͹���������ͨ����Ʒid�����ҵ���Ʒ������map�м��ɣ�
	public List<OrderListForm> findOrders(int user_id) {
		try {
			// �����û�id�ҳ����û������ж���
			List<Order> list = orderDao.findOrders(user_id);
			// ����ͨ�������ҵ������ţ�ͨ���������ҵ��������¼����,����������
			// ��¼�õ���Ʒid��������ȡ��Ʒ���󣩺͹��������
			// ����Map<Product,Integer>��
			List<OrderListForm> olflist = new ArrayList<OrderListForm>();
			for (Order order : list) {
				OrderListForm olf = new OrderListForm();
				//��װ��4��������
				BeanUtils.copyProperties(olf, order);
				//��װ���û���
				User user = userDao.finUserById(order.getUser_id());
				olf.setUsername(user.getUsername());
				//��װMap<Product,Integer>
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
	//���������Ƕ�����
	public void delOrderById(String id) {
		//���ݶ������ҵ��������¼���ҵ�������������������¼�õ���ÿ����Ʒid�õ�product��pnum+��ȥ
		List<OrderItem> ois = orderDao.findOrderItems(id);
		for (OrderItem oi : ois) {
			productDao.addProdNum(oi.getProduct_id(),oi.getBuynum());
		}
		//ɾ���������¼
		orderDao.delOrderItemById(id);
		
		//ɾ������
		orderDao.delOrderById(id);
		
	}
	public List<SaleInfo> saleList() {
		return orderDao.saleList();
	}
}
