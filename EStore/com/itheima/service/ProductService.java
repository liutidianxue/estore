package com.itheima.service;

import java.util.List;

import com.itheima.domain.Product;

public interface ProductService extends Service{

	void addProd(Product prod);

	List<Product> getAllProd();

	Product findProdByID(String id);


}
