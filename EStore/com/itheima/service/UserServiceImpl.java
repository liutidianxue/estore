package com.itheima.service;

import java.util.UUID;


import com.itheima.dao.UserDao;
import com.itheima.domain.User;
import com.itheima.factory.BasicFactory;

public class UserServiceImpl implements UserService {
	private UserDao dao = BasicFactory.getFactory().getDao(UserDao.class);
	public void regist(User user) {
			//У���û����Ƿ��Ѿ�����
			if(dao.findUserByUsername(user.getUsername()) != null){
				throw new RuntimeException("�û����Ѿ�����");
			}
			//����dao�еķ�����ӵ����ݿ�
			user.setRole("user");
			user.setState(0);
			user.setActivecode(UUID.randomUUID().toString());
			dao.addUser(user);
	}
	public User findUserByUsernameAndPassword(String username, String password) {
		
		return dao.findUserByUsernameAndPassword(username,password);
	}
}
