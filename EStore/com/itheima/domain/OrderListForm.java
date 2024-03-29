package com.itheima.domain;

import java.sql.Timestamp;
import java.util.Map;

public class OrderListForm {
	private String id;
	private double money;
	private String receiverinfo;
	private int paystate;
	private Timestamp ordertime;
	private String username;
	private Map<Product,Integer> prodMap;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public int getPaystate() {
		return paystate;
	}
	public void setPaystate(int paystate) {
		this.paystate = paystate;
	}
	public Timestamp getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Timestamp ordertime) {
		this.ordertime = ordertime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Map<Product, Integer> getProdMap() {
		return prodMap;
	}
	public void setProdMap(Map<Product, Integer> prodMap) {
		this.prodMap = prodMap;
	}
	public String getReceiverinfo() {
		return receiverinfo;
	}
	public void setReceiverinfo(String receiverinfo) {
		this.receiverinfo = receiverinfo;
	}
	
}
