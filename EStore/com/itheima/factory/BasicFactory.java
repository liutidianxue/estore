package com.itheima.factory;

import java.io.FileReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Properties;

import com.itheima.annotation.Tran;
import com.itheima.dao.Dao;
import com.itheima.service.Service;
import com.itheima.util.TransactionManager;

public class BasicFactory {
	private static BasicFactory factory = new BasicFactory();
	private static Properties prop = null;

	private BasicFactory() {
		// TODO Auto-generated constructor stub
	}

	static {
		prop = new Properties();
		try {
			prop.load(new FileReader(BasicFactory.class.getClassLoader()
					.getResource("config.properties").getPath()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static BasicFactory getFactory() {
		return factory;
	}

	/**
	 * 获取service方法
	 */
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(Class<T> clazz) {
		try {
			// 根据配置文件创建具体的service
			String infName = clazz.getSimpleName();
			String implName = prop.getProperty(infName);
			final T service = (T) Class.forName(implName).newInstance();

			// 为了实现AOP，生成service代理，根据注解确定在service方法执行之前和之后做一些操作，比如：事务管理/记录日志/细粒度权限控制....
			T proxyService = (T) Proxy.newProxyInstance(service.getClass()
					.getClassLoader(), service.getClass().getInterfaces(),
					new InvocationHandler() {
						public Object invoke(Object proxy, Method method,
								Object[] args) throws Throwable {
							if (method.isAnnotationPresent(Tran.class)) {// 如果有注解，则管理事务；
								try {
									TransactionManager.startTran();// 开启事务

									Object obj = method.invoke(service, args);// 真正执行方法

									TransactionManager.commit();// 提交事务
									return obj;
								} catch (InvocationTargetException e) {
									TransactionManager.roolBack();// 回滚事务
									throw new RuntimeException(e
											.getTargetException());
								} catch (Exception e) {
									TransactionManager.roolBack();// //回滚事务
									throw new RuntimeException(e);
								} finally {
									TransactionManager.release();
								}
							} else {// 如果没有注解，则不管理事务，直接执行方法
								return method.invoke(service, args);
							}
						}
					});
			return proxyService;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/***************************************************************************
	 * 获取dao方法
	 */
	public <T extends Dao> T getDao(Class<T> clazz) {
		try {
			String infName = clazz.getSimpleName();
			String implName = prop.getProperty(infName);
			return (T) Class.forName(implName).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
