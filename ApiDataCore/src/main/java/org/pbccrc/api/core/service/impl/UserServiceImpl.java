package org.pbccrc.api.core.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

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
	 * @throws SQLException 
	 * @throws SerialException 
	 */
	public void addUser(User user) throws Exception {
		
//		String userID = StringUtil.createRandomID();
//		user.setId(userID);
		// 设置生成私钥文件并存放
//		File f = new File("D:/privateKeyFile");
//		byte[] keyBytes = new byte[(int)f.length()]; 
//		Blob privateKey = new SerialBlob(keyBytes);
//		user.setPrivateKey(privateKey);
				
		userDao.addUser(user);
		
		ApiUser apiUser = new ApiUser();
		apiUser.setId(user.getId());
		apiUser.setBlance(new BigDecimal("0.00"));
		apiUser.setCreditLimit(new BigDecimal("0.00"));
		
		RedisClient.set("apiUser_" + user.getId(), apiUser);
		
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
	
	/**
	 * 重置用户密码
	 * @param userID
	 * @param password
	 * @return
	 */
	public boolean resetPassword(String userID, String password) {
		
		return userDao.resetPassword(userID, StringUtil.string2MD5(password));
	}
}
