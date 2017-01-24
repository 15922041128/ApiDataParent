package org.pbccrc.api.core.service.impl;

import java.math.BigDecimal;

import org.pbccrc.api.base.bean.ApiUser;
import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.bean.User;
import org.pbccrc.api.base.service.UserService;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.dao.ApiUserDao;
import org.pbccrc.api.core.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ApiUserDao apiUserDao;

	/**
	 * @param userName 	帐号
	 * @return			是否存在
	 */
	public boolean isExist(String userName) {
		return userDao.isExist(userName) > 0;
	}

	/**
	 * 
	 * @param user 用户信息
	 * @return	      主键
	 */
	public void addUser(User user) {
		
		int userID = userDao.addUser(user);
		
		ApiUser apiUser = new ApiUser();
		apiUser.setId(userID);
		apiUser.setBlance(new BigDecimal("0.00"));
		apiUser.setCreditLimit(new BigDecimal("0.00"));
		
		RedisClient.setObject("apiUser_" + userID, apiUser);
		
		apiUserDao.addApiUser(apiUser);
	}
	
	/**
	 * 
	 * @param userName	帐号
	 * @param password	密码
	 * @return			登录帐号
	 */
	public User login(String userName, String password) {
		
		User user = new User();
		user.setUserName(userName);
		user.setPassword(StringUtil.string2MD5(password));
		
		user = userDao.login(user);
		
		return user;
	}
	
	/**
	 * 
	 * @param userID    用户ID
	 * @param password  用户新密码
	 */
	public void resetPassword(Integer userID, String password) {
		User user = new User();
		user.setId(userID);
		user.setPassword(StringUtil.string2MD5(password));
		userDao.updateUser(user);
	}
	
	/**
	 * @param user 	用户信息
	 */
	public void modifyUser(User user) {
		userDao.updateUser(user);
	}
	
	/** 根据userID获取用户信息 */
	public User getUserByID(String userID) {
		return userDao.getUserByID(userID);
	}
	
	/**
	 * 判断用户传入密码是否正确
	 * @param userID
	 * @param password
	 * @return
	 */
	public boolean passwordIsTrue(String userID, String password) {
		
		User user = userDao.getUserByID(userID);
		
		return user.getPassword().equals(StringUtil.string2MD5(password));
	}

	public Pagination queryAllUser(User user, Pagination pagination) {
		return userDao.getUserByPage(user, pagination);
	}
}
