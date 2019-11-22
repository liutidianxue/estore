package com.itheima.dao;

import java.sql.Connection;

import com.itheima.domain.User;

public interface UserDao extends Dao{
	
	/**
	 * �����û��������û�
	 * @param username	�û���
	 * @return	���ҵ����û������û���ҵ�����null
	 */
	User findUserByUsername(String username);
	
	
	/**
	 * ����û�
	 * @param user	��װ���û���Ϣ��bean
	 */
	void addUser(User user);




	void delete(int id);


	User findUserByUsernameAndPassword(String username, String password);


	User finUserById(int user_id);



	
}
