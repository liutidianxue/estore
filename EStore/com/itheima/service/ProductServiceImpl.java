package com.itheima.service;

import java.util.List;
import java.util.UUID;

import com.itheima.dao.ProductDao;
import com.itheima.domain.Product;
import com.itheima.factory.BasicFactory;

public class ProductServiceImpl implements ProductService {
	ProductDao dao = BasicFactory.getFactory().getDao(ProductDao.class);
	public void addProd(Product prod) {
		prod.setId(UUID.randomUUID().toString());
		dao.addProd(prod);
		
	}
	
	public List<Product> getAllProd() {
		return dao.getAllProd();
	}
	
	public Product findProdByID(String id) {
		return dao.findProdByID(id);
	}
}
