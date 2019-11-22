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
	//����Դ�����������ж�ֻ����һ������Դ
	private static DataSource source = new ComboPooledDataSource();
	
	//�Ƿ�������ı��
	private static ThreadLocal<Boolean> isTran_local = new ThreadLocal<Boolean>(){
		@Override
		protected Boolean initialValue() {
			return false;//�ʼfalse������Ĭ�ϲ���������
		}
	};
	//������ʵ���ӵĴ������ӣ������close����
	private static ThreadLocal<Connection> proxyConn_local = new ThreadLocal<Connection>(){};
	//������ʵ����
	private static ThreadLocal<Connection> realConn_local = new ThreadLocal<Connection>(){};
	
	/**
	 * ��������ķ���
	 * @throws SQLException
	 */
	public static void startTran() throws SQLException {
		isTran_local.set(true);//����������Ϊtrue
		final Connection conn = source.getConnection();//�������ӣ����е�ǰ�߳��е����ݿ�������������conn
		conn.setAutoCommit(false);//��������
		realConn_local.set(conn);//Ϊ�˷�������ر����ӣ���������ӱ��������ڵ�ǰ�߳���
		
		//����һ��������Ҫִ�ж���sql��ÿ��sqlִ�й��󶼹ر����ӣ�����һ��������sql����û��ִ�У���������ط�����close������ʹ�����ܹر�����
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
	 * �ύ����
	 */
	public static void commit(){
		DbUtils.commitAndCloseQuietly(proxyConn_local.get());
	}
	
	/**
	 * �ع�����
	 */
	public static void roolBack(){
		DbUtils.rollbackAndCloseQuietly(proxyConn_local.get());
	}
	
	public static DataSource getSource(){
		if(isTran_local.get()){//�����ʼ�������򷵻ظ����Datasource������Ϊÿ�ε���getConnection������ͬһ�������������Conn
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
		}else{//û�п��������񣬷�����ͨ������Դ
			return source;
		}
	}
	/**
	 * �ͷ���Դ
	 */
	public static void release(){
		DbUtils.closeQuietly(realConn_local.get());
		realConn_local.remove();
		proxyConn_local.remove();
		isTran_local.remove();
	}
}
