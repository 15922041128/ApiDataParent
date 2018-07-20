package org.pbccrc.api.base.service;

import com.alibaba.fastjson.JSONObject;

public interface ApiProductService {
	
	/**
	 * bhyh
	 * @param name     姓名 
	 * @param idCard   身份证号码
	 * @param userID   用户ID
	 * @param uuid     UUID
	 * @return
	 * @throws Exception
	 */
	public JSONObject bhyh(String name, String idCard, String userID, String uuid) throws Exception;

	/**
	 * 反欺诈服务
	 * @param phone    电话号码
	 * @param name     姓名 
	 * @param idCard   身份证号码
	 * @param userID   用户ID
	 * @param uuid     UUID
	 * @return
	 * @throws Exception
	 */
	public JSONObject fraud(String phone, String name, String idCard, String userID, String uuid) throws Exception;
	
	/**
	 * 新反欺诈服务
	 * @param phone    电话号码
	 * @param name     姓名 
	 * @param idCard   身份证号码
	 * @param userID   用户ID
	 * @param uuid     UUID
	 * @return
	 * @throws Exception
	 */
	public JSONObject fraud2(String phone, String name, String idCard, String userID, String uuid) throws Exception;
	
	/**
	 * 信用卡申请风控
	 * @param phone    电话号码
	 * @param phonepassword  电话服务密码
	 * @param name     姓名 
	 * @param idCard   身份证号码
	 * @param userID   用户ID
	 * @param uuid     UUID
	 * @return
	 * @throws Exception
	 */
	public JSONObject reditCardApplyRiskControl(String phone, String phonePassword, String name, String idCard, String userID, String uuid) throws Exception;
	
	/**
	 * 获取动态码(信用卡申请风控-电话详情用)
	 * @param phone    电话号码
	 * @param name     姓名 
	 * @param idCard   身份证号码
	 * @return
	 * @throws Exception
	 */
	public JSONObject getDynamicCode(String phone, String name, String idCard) throws Exception;
	
	/**
	 * 重置密码(信用卡申请风控-电话详情用)
	 * @param token     获取动态码时返回的token
	 * @param password  要重置的密码 
	 * @param captcha   动态码
	 * @return
	 * @throws Exception
	 */
	public JSONObject resetPassword(String token, String password, String captcha) throws Exception;
	
	/**
	 * 一致性验证(身份证与电话号码 )
	 * @param phone    电话号码
	 * @param idCard   身份证号码
	 * @param userID   用户ID
	 * @param uuid     UUID
	 * @return
	 * @throws Exception
	 */
	public JSONObject checkConsistency(String phone, String idCard, String userID, String uuid) throws Exception;
}
