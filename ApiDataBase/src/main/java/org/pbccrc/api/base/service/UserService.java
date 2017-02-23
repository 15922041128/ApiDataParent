package org.pbccrc.api.base.service;

import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.bean.User;

public interface UserService {
	
	/**
	 * @param userName 	帐号
	 * @return			是否存在
	 */
	public boolean isExist(String userName);
	
	/**
	 * 
	 * @param user 用户信息
	 */
	public void addUser(User user);
	
	/**
	 * 
	 * @param userName	帐号
	 * @param password	密码
	 * @return			登录帐号
	 */
	public User login(String userName, String password);
	
	/**
	 * 
	 * @param userID    用户ID
	 * @param password  用户新密码
	 */
	public void resetPassword(Integer userID, String password);
	
	/**
	 * @param user 	用户信息
	 */
	public void modifyUser(User user);
	
	/** 根据userID获取用户信息 */
	public User getUserByID(String userID);
	
	/**
	 * 判断用户传入密码是否正确
	 * @param userID
	 * @param password
	 * @return
	 */
	public boolean passwordIsTrue(String userID, String password);
	/**
	 * 查询返回所有用户
	 * @return
	 */
	public Pagination queryAllUser(User user, Pagination pagination); 
	
	/**
	 * 重置用户密码
	 * @param userID
	 * @param password
	 * @return
	 */
	boolean resetPassword(String userID, String password);

}
