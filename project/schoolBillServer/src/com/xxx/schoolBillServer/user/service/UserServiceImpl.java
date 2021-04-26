package com.xxx.schoolBillServer.user.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xxx.schoolBillServer.entity.User;
import com.xxx.schoolBillServer.user.dao.UserDaoImpl;
@Service
@Transactional(readOnly = false)
public class UserServiceImpl {
	@Resource
	private UserDaoImpl userDaoImpl;
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
