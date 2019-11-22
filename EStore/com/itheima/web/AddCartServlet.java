package com.itheima.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.Product;
import com.itheima.factory.BasicFactory;
import com.itheima.service.ProductService;

public class AddCartServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProductService service = BasicFactory.getFactory().getService(ProductService.class);
		//根据id查出要购买的商品
		String id = request.getParameter("id");
		Product prod = service.findProdByID(id);
		//想cartmap中添加这个商品，如果之前没有这个商品，则添加并将数量设置为1.如果已经有过这个商品，数量+1
		if(prod ==null){
			throw new RuntimeException("找不到该商品");
		}else{
			Map<Product,Integer> cartmap = (Map<Product, Integer>) request.getSession().getAttribute("cartmap");
			cartmap.put(prod, cartmap.containsKey(prod)?cartmap.get(prod)+1:1);
		}
		//3.重定向到购物车页面进行展示
		response.sendRedirect("/cart.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
