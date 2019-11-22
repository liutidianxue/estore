package com.itheima.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.SaleInfo;
import com.itheima.factory.BasicFactory;
import com.itheima.service.OrderService;

public class SaleListServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//调用Service中查询销售榜单的方法
		OrderService service = BasicFactory.getFactory().getService(OrderService.class);
		List<SaleInfo> list = service.saleList();
		//将销售榜单信息组织成csv格式的数据
		StringBuffer buffer = new StringBuffer();
		buffer.append("商品编号,商品名称,销售数量\r\n");
		for (SaleInfo si : list) {
			buffer.append(si.getProd_id()+","+si.getProd_name()+","+si.getSale_num()+"\r\n");
		}
		String data = buffer.toString();
		//提供下载
		String filename = "Estore销售榜单_"+new Date().toLocaleString()+".csv";
		response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(filename,"utf-8"));
		response.setContentType(this.getServletContext().getMimeType(filename));
		response.getWriter().write(data);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
