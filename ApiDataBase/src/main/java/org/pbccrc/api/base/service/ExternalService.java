package org.pbccrc.api.base.service;

import org.pbccrc.api.base.bean.LocalApi;

import com.alibaba.fastjson.JSONObject;

public interface ExternalService {

	/**
	 * 唯品会个人风险查询
	 * @param idNo
	 * @param cardName
	 * @param phone
	 * @param cardNo
	 * @param uuid
	 * @return
	 * @throws Exception
	 */
	public JSONObject vipQueryBlackList(String idNo, String cardName, String phone, String cardNo, String userId, String uuid, LocalApi localApi) throws Exception;
	
	/**
	 * 凭安电话标签查询
	 * @param phone
	 * @param userId
	 * @param uuid
	 * @return
	 * @throws Exception
	 */
	public JSONObject paPhoneTag(String phone, String userId, String uuid, LocalApi localApi) throws Exception;
	
	/**
	 * 凭安失信被执行人查询
	 * @param uid
	 * @param orgName
	 * @param userId
	 * @param name
	 * @param idCard
	 * @return
	 * @throws Exception
	 */
	public JSONObject paShixin(String name, String idCard, String orgName, String userId, String uuid, LocalApi localApi) throws Exception;
	
	/**
	 * 凭安逾期查询
	 * @param phone
	 * @param uid
	 * @param orgName
	 * @param imsi
	 * @param imei
	 * @param queryDate
	 * @param userId
	 * @param name
	 * @param idCard
	 * @return
	 * @throws Exception
	 */
	public JSONObject paOverdue(String phone, String name, String idCard, String orgName, String imsi, String imei, 
			String queryDate, String userId, String uuid, LocalApi localApi) throws Exception;
	
	/**
	 * 凭安借贷查询
	 * @param phone
	 * @param uid
	 * @param orgName
	 * @param imsi
	 * @param imei
	 * @param queryDate
	 * @param userId
	 * @param name
	 * @param idCard
	 * @return
	 * @throws Exception
	 */
	public JSONObject paLoan(String phone, String name, String idCard, String orgName, String imsi, String imei, 
			String queryDate, String userId, String uuid, LocalApi localApi) throws Exception;
	
	/**
	 * 凭安黑名单查询
	 * @param phone
	 * @param uid
	 * @param orgName
	 * @param imsi
	 * @param imei
	 * @param userId
	 * @param name
	 * @param idCard
	 * @return
	 * @throws Exception
	 */
	public JSONObject paBlackList(String phone, String name, String idCard, String orgName, String imsi, String imei, 
			String userId, String uuid, LocalApi localApi) throws Exception;
	
	/**
	 * 凭安申请人属性查询
	 * @param phone
	 * @param uid
	 * @param orgName
	 * @param imsi
	 * @param imei
	 * @param queryDate
	 * @param userId
	 * @param name
	 * @param idCard
	 * @return
	 * @throws Exception
	 */
	public JSONObject paPhkjModelerScore(String phone, String name, String idCard, String orgName, String imsi, String imei, 
			String queryDate, String userId, String uuid, LocalApi localApi) throws Exception;
	
	/**
	 * 凭安综合查询
	 * @param phone
	 * @param name
	 * @param idCard
	 * @param orgName
	 * @param imsi
	 * @param imei
	 * @param queryDate
	 * @param userId
	 * @param name
	 * @param idCard
	 * @return
	 * @throws Exception
	 */
	public JSONObject paZh(String phone, String name, String idCard, String orgName, String imsi, String imei, 
			String queryDate, String userId, String uuid, LocalApi localApi) throws Exception;
}
