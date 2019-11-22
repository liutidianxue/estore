package com.itheima.dao;

import java.util.List;

import com.itheima.domain.Order;
import com.itheima.domain.OrderItem;
import com.itheima.domain.SaleInfo;

public interface OrderDao extends Dao{

	void addOrder(Order order);


	void addOrderItem(OrderItem item);


	List<Order> findOrders(int user_id);


	List<OrderItem> findOrderItems(String order_id);


	void delOrderById(String id);


	void delOrderItemById(String order_id);


	List<SaleInfo> saleList();



}
