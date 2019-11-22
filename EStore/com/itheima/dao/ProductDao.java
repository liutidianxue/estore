package com.itheima.dao;

import java.sql.SQLException;
import java.util.List;

import com.itheima.domain.Product;

public interface ProductDao extends Dao{

	void addProd(Product prod);

	List<Product> getAllProd();

	Product findProdByID(String id);

	void delPnum(String product_id, int buynum) throws SQLException;

	void addProdNum(String product_id, int buynum);
	
}
