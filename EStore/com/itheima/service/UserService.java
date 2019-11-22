package com.itheima.service;

import com.itheima.domain.User;

public interface UserService extends Service{

	void regist(User user);

	User findUserByUsernameAndPassword(String username, String password);

	
}
