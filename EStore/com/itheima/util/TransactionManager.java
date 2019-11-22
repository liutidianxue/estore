package com.itheima.util;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TransactionManager {
	private TransactionManager() {
		
	}
	//数据源，整个程序中都只有这一个数据源
	private static DataSource source = new ComboPooledDataSource();
	
	//是否开启事务的标记
	private static ThreadLocal<Boolean> isTran_local = new ThreadLocal<Boolean>(){
		@Override
		protected Boolean initialValue() {
			return false;//最开始false，表面默认不开启事务
		}
	};
	//保存真实连接的代理连接，改造过close方法
	private static ThreadLocal<Connection> proxyConn_local = new ThreadLocal<Connection>(){};
	//保存真实连接
	private static ThreadLocal<Connection> realConn_local = new ThreadLocal<Connection>(){};
	
	/**
	 * 开启事务的方法
	 * @throws SQLException
	 */
	public static void startTran() throws SQLException {
		isTran_local.set(true);//设置事务标记为true
		final Connection conn = source.getConnection();//创建连接，所有当前线程中的数据库操作都基于这个conn
		conn.setAutoCommit(false);//开启事务
		realConn_local.set(conn);//为了方便后续关闭连接，将这个连接保存起来在当前线程中
		
		//由于一个事务需要执行多条sql，每个sql执行过后都关闭连接，这样一来后续的sql方法没法执行，所以这个地方改造close方法，使他不能关闭连接
		Connection ProxyConn = (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), conn.getClass().getInterfaces()
				,new InvocationHandler(){
			public Object invoke(Object proxy, Method method,
					Object[] args) throws Throwable {
				if("close".equals(method.getName())){
					return null;
				}else{
					return method.invoke(conn, args);
				}
			}
		});
		proxyConn_local.set(ProxyConn);
	}
	
	/**
	 * 提交事务
	 */
	public static void commit(){
		DbUtils.commitAndCloseQuietly(proxyConn_local.get());
	}
	
	/**
	 * 回滚事务
	 */
	public static void roolBack(){
		DbUtils.rollbackAndCloseQuietly(proxyConn_local.get());
	}
	
	public static DataSource getSource(){
		if(isTran_local.get()){//如果开始过事务，则返回改造的Datasource，改造为每次调用getConnection都返回同一个开启过事务的Conn
			return (DataSource) Proxy.newProxyInstance(source.getClass().getClassLoader(), source.getClass().getInterfaces()
					, new InvocationHandler(){
				public Object invoke(Object proxy, Method method,
						Object[] args) throws Throwable {
					if("getConnection".equals(method.getName())){
						return proxyConn_local.get();
					}else{
						return method.invoke(source, args);
					}
				}
			});
		}else{//没有开启过事务，返回普通的数据源
			return source;
		}
	}
	/**
	 * 释放资源
	 */
	public static void release(){
		DbUtils.closeQuietly(realConn_local.get());
		realConn_local.remove();
		proxyConn_local.remove();
		isTran_local.remove();
	}
}
