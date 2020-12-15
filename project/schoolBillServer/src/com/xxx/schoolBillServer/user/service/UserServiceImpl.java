package com.xxx.schoolBillServer.user.service;

import com.xxx.schoolBillServer.entity.User;
import com.xxx.schoolBillServer.user.dao.UserDaoImpl;

public class UserServiceImpl {
	private UserDaoImpl userDaoImpl = new UserDaoImpl();
	public User findUser(String phone) {
		return userDaoImpl.findUser(phone);
	}
	public boolean addUser(User user) {
		return userDaoImpl.addUser(user);
	}
	public boolean updateUser(User user) {
		return userDaoImpl.updateUser(user);
	}

}
