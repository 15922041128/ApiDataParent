package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.User;
import org.pbccrc.api.core.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserDao{
	
	@Resource
	private UserMapper userMapper;
	
	public Integer isExist(String userName){
		return userMapper.isExist(userName);
	}
	
	public int addUser(User user){
		userMapper.addUser(user);
		return user.getID();
	}
	
	public User login(User user){
		return userMapper.login(user);
	}
	
	public void updateUser(User user){
		userMapper.updateUser(user);;
	}
	
	public User getUserByID(String userID) {
		return userMapper.getUserByID(userID);
	}
}
