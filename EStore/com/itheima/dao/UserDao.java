package com.itheima.dao;

import java.sql.Connection;

import com.itheima.domain.User;

public interface UserDao extends Dao{
	
	/**
	 * 根据用户名查找用户
	 * @param username	用户名
	 * @return	查找到的用户，如果没有找到返回null
	 */
	User findUserByUsername(String username);
	
	
	/**
	 * 添加用户
	 * @param user	封装了用户信息的bean
	 */
	void addUser(User user);




	void delete(int id);


	User findUserByUsernameAndPassword(String username, String password);


	User finUserById(int user_id);



	
}
