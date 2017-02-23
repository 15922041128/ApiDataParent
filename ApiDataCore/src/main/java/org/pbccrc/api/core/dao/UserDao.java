package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.bean.User;
import org.pbccrc.api.core.mapper.UserMapper;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class UserDao{
	
	@Resource
	private UserMapper userMapper;
	
	public Integer isExist(String userName){
		return userMapper.isExist(userName);
	}
	
	public int addUser(User user){
		userMapper.addUser(user);
		return user.getId();
	}
	
	public User login(User user){
		return userMapper.login(user);
	}
	
	public void updateUser(User user){
		userMapper.updateUser(user);
	}
	
	public User getUserByID(String userID) {
		return userMapper.getUserByID(userID);
	}
	
	public Pagination getUserByPage(User user, Pagination pagination) {
		PageHelper.startPage(pagination.getCurrentPage(), pagination.getPageSize());
		Page<User> users = (Page<User>) userMapper.getAllUser(user);
		Pagination userPagination = new Pagination();
		userPagination.setResult(users.getResult());
		userPagination.setTotalCount(users.getTotal());
		return userPagination;
	}
	
	public boolean resetPassword(String userID, String password) {
		
		User user = new User();
		user.setPassword(password);
		user.setId(Integer.parseInt(userID));
		
		int cnt = userMapper.resetPassword(user);
		
		return cnt != 0;
	}
	
}
