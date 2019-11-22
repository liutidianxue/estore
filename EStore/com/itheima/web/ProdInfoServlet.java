package com.itheima.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.Product;
import com.itheima.factory.BasicFactory;
import com.itheima.service.ProductService;

public class ProdInfoServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProductService service = BasicFactory.getFactory().getService(ProductService.class);
		//根据id找到商品
		String id = request.getParameter("id");
		Product prod = service.findProdByID(id);
		//将商品传到request域中，返回展示页面
		if(prod == null){
			throw new RuntimeException("找不到该商品！！");
		}else{
			request.setAttribute("prod", prod);
			request.getRequestDispatcher("/prodInfo.jsp").forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
