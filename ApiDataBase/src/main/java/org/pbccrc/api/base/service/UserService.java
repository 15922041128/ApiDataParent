package org.pbccrc.api.base.service;

import org.pbccrc.api.base.bean.User;

public interface UserService {
	
	/**
	 * @param userName 	帐号
	 * @return			是否存在
	 */
	public boolean isExist(String userName);
	
	/**
	 * @param user 	用户信息
	 */
	public void addUser(User user);
	
	/**
	 * @param userName	帐号
	 * @param password	密码
	 * @return			是否成功
	 */
	public User login(String userName, String password);
	
	/**
	 * 
	 * @param userID    用户ID
	 * @param password  用户新密码
	 */
	public void resetPassword(int userID, String password);
	
	/**
	 * @param user 	用户信息
	 */
	public void modifyUser(User user);

}
