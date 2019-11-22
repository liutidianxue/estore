package com.itheima.service;

import java.util.List;

import com.itheima.annotation.Tran;
import com.itheima.domain.Order;
import com.itheima.domain.OrderListForm;
import com.itheima.domain.SaleInfo;

public interface OrderService extends Service{
	@Tran
	void addOrder(Order order);


	List<OrderListForm> findOrders(int user_id);

	@Tran
	void delOrderById(String id);


	List<SaleInfo> saleList();

}
