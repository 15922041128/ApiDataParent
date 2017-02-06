package org.pbccrc.api.base.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface LocalDBService {
	
	/***
	 * 根据身份证和姓名查询信贷信息
	 * @param name			姓名
	 * @param idCardNo		身份证号
	 * @return
	 * @throws Exception
	 */
	public String query(String name, String idCardNo) throws Exception;
	
	/***
	 * 根据身份证和姓名查询失信被执行人信息
	 * @param idCardNo		身份证号
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSxr(String idCardNo) throws Exception;
	
	/**
	 * 查询本地api
	 * @param service
	 * @param name
	 * @param idCardNo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryApi(String service, String name, String idCardNo) throws Exception;
	
	/***
	 * 根据身份证和姓名查询内码
	 * @param name			姓名
	 * @param identifier	身份证号
	 * @return
	 * @throws Exception
	 */
	public String getInnerID(String name, String identifier) throws Exception;
	
	/***
	 * 根据身份证查询内码
	 * @param identifier	身份证号
	 * @return
	 * @throws Exception
	 */
	public String getInnerID(String identifier) throws Exception;
	
	/***
	 * 根据手机号查询内码
	 * @param telNum	手机号码
	 * @return
	 * @throws Exception
	 */
	public String getInnerIDByTelNum(String telNum) throws Exception;
	
	/**
	 * 查询本地数据库
	 * @param uuid
	 * @param userID
	 * @param service
	 * @param innerID
	 * @param params
	 * @return
	 */
	public Map<String, Object> getResult(String uuid, String userID, String service, String innerID, JSONObject params);

}
