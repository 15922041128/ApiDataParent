package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.bean.DBEntity;
import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.base.service.LocalDBService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.core.dao.ApiLogDao;
import org.pbccrc.api.core.dao.BlackDao;
import org.pbccrc.api.core.dao.DBOperatorDao;
import org.pbccrc.api.core.dao.LdbApiDao;
import org.pbccrc.api.core.dao.LocalApiDao;
import org.pbccrc.api.core.dao.ScoreDao;
import org.pbccrc.api.core.dao.TelPersonDao;
import org.pbccrc.api.core.dao.ZhIdentificationDao;
import org.pbccrc.api.core.dao.datasource.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class LocalDBServiceImpl implements LocalDBService {
	
	@Autowired
	private DBOperatorDao dbOperatorDao;
	
	@Autowired
	private LdbApiDao ldbApiDao;
	
	@Autowired
	private ZhIdentificationDao zhIdentificationDao;
	
	@Autowired
	private TelPersonDao telPersonDao;
	
	@Autowired
	private BlackDao blackDao;
	
	@Autowired
	private ScoreDao scoreDao;
	
	@Autowired
	private LocalApiDao localApiDao;
	
	@Autowired
	private ApiLogDao apiLogDao;
	
	/***
	 * 根据身份证和姓名查询信贷信息
	 * @param name			姓名
	 * @param idCardNo		身份证号
	 * @return
	 * @throws Exception
	 */
	public String query(String name, String idCardNo) throws Exception {
		String tableName = "ldb_isn";
		DBEntity entity = new DBEntity();
		entity.setTableName(tableName);
		List<String> fields = new ArrayList<String>();
		fields.add("name");
		fields.add("idCardNo");
		List<String> values = new ArrayList<String>();
		values.add(name);
		values.add(idCardNo);
		entity.setFields(fields);
		entity.setValues(values);
		Map<String, Object> user = dbOperatorDao.queryData(entity);
		if (null == user) {
			ResultContent resultContent = new ResultContent();
			resultContent.setCode(Constants.CODE_ERR_FAIL);
			resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
			return resultContent.toString();
		}
		String userID = String.valueOf(user.get("id"));
		
		fields = new ArrayList<String>();
		fields.add("user_id");
		values = new ArrayList<String>();
		values.add(userID);
		entity.setFields(fields);
		entity.setValues(values);
		
		tableName = "ldb_locan";
		entity.setTableName(tableName);
		List<Map<String, Object>> locanList = dbOperatorDao.queryDatas(entity);
		
		tableName = "ldb_guarantee";
		entity.setTableName(tableName);
		List<Map<String, Object>> geeList = dbOperatorDao.queryDatas(entity);
		
		tableName = "ldb_credit_card";
		entity.setTableName(tableName);
		Map<String, Object> creditInfo = dbOperatorDao.queryData(entity);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LocanInfo", locanList);
		map.put("GuaranteeInfo", geeList);
		map.put("Credit_Card", creditInfo);
		String total = JSON.toJSONString(map);
		return total;
	}
	

	/***
	 * 根据身份证和姓名查询失信被执行人信息
	 * @param idCardNo		身份证号
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSxr(String idCardNo) throws Exception {
		
		String tableName = "ldb_dishonest_info";
		DBEntity entity = new DBEntity();
		entity.setTableName(tableName);
		List<String> fields = new ArrayList<String>();
		fields.add("CARDNUM");
		List<String> values = new ArrayList<String>();
		values.add(idCardNo);
		entity.setFields(fields);
		entity.setValues(values);
		
		String[] selectItems = new String[]{
				Constants.LDB_DISHONEST_INFO_CARDNUM, Constants.LDB_DISHONEST_INFO_COURT_NAME, 
				Constants.LDB_DISHONEST_INFO_CASE_CODE, Constants.LDB_DISHONEST_INFO_INAME, 
				Constants.LDB_DISHONEST_INFO_PERFORMANCE, Constants.LDB_DISHONEST_INFO_PUBLISH_DATE,
				Constants.LDB_DISHONEST_INFO_AREA_NAME, Constants.LDB_DISHONEST_INFO_DUTY,
				Constants.LDB_DISHONEST_INFO_DISREPUT_TYPE_NAME};
		entity.setSelectItems(selectItems);
		
		List<Map<String, Object>> dishonestList = dbOperatorDao.queryDatas(entity);
		
		return dishonestList;
	}
	
	/**
	 * 查询本地api
	 * @param service
	 * @param name
	 * @param idCardNo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryApi(String service, String name, String idCardNo) throws Exception {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("isNull", "N");
		
		// 根据身份证号获取内码信息
		DynamicDataSourceHolder.change2oracle();
		Map<String, Object> insideCodeMap = zhIdentificationDao.getInnerID(name, idCardNo);
		DynamicDataSourceHolder.change2mysql();
		if (null == insideCodeMap) {
			returnMap.put("isNull", "Y");
			return returnMap;
		}
		// 内码
		String innerID = String.valueOf(insideCodeMap.get("INNERID"));
		
		// 获取api
		Map<String, Object> api = ldbApiDao.queryByService(service);
		// 获取返回字段
		String[] returnParams = String.valueOf(api.get("returnParams")).split(Constants.COMMA);
		
		// DB操作对象
		DBEntity entity = new DBEntity();
		entity.setTableName("ldb_" + service);
		List<String> fields = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		fields.add("insideCode");
		values.add(innerID);
		entity.setFields(fields);
		entity.setValues(values);
		
		// 查询本地数据库
		Map<String, Object> dbMap = dbOperatorDao.queryData(entity);

		// 获取返回字段
		Map<String, Object> result = new HashMap<String, Object>();
		for (String returnParam : returnParams) {
			result.put(returnParam, dbMap.get(returnParam));
		}
		returnMap.put("result", result);
		
		return returnMap;
	}
	
	/***
	 * 根据身份证和姓名查询内码
	 * @param name			姓名
	 * @param idCardNo		身份证号
	 * @return
	 * @throws Exception
	 */
	public String getInnerID(String name, String identifier) throws Exception {
		
		String innerID = Constants.BLANK;
		
		DynamicDataSourceHolder.change2oracle();
		Map<String, Object> returnMap = zhIdentificationDao.getInnerID(name, identifier);
		DynamicDataSourceHolder.change2mysql();
		
		if (null == returnMap) {
			return innerID;
		}

		innerID = String.valueOf(returnMap.get("INNERID"));
		
		return innerID;
	}
	
	/***
	 * 根据身份证查询内码
	 * @param idCardNo		身份证号
	 * @return
	 * @throws Exception
	 */
	public String getInnerID(String identifier) throws Exception {
		
		String innerID = Constants.BLANK;
		
		DynamicDataSourceHolder.change2oracle();
		Map<String, Object> returnMap = zhIdentificationDao.getInnerID(identifier);
		DynamicDataSourceHolder.change2mysql();
		
		if (null == returnMap) {
			return innerID;
		}

		innerID = String.valueOf(returnMap.get("INNERID"));
		
		return innerID;
	}
	
	/***
	 * 根据手机号查询内码
	 * @param telNum	手机号码
	 * @return
	 * @throws Exception
	 */
	public String getInnerIDByTelNum(String telNum) throws Exception {
		
		String innerID = Constants.BLANK;
		
		DynamicDataSourceHolder.change2oracle();
		Map<String, Object> returnMap = telPersonDao.getInnerID(telNum);
		DynamicDataSourceHolder.change2mysql();
		
		if (null == returnMap) {
			return innerID;
		}

		innerID = String.valueOf(returnMap.get("INNERID"));
		
		return innerID;
	}
	
	/**
	 * 根据内码获得黑名单
	 * @param innerID
	 * @return
	 */
	public JSONObject getBlack(String innerID) {
		
		DynamicDataSourceHolder.change2oracle();
		List<Map<String, Object>> blackMapList = blackDao.getBlack(innerID);
		DynamicDataSourceHolder.change2mysql();
		
		if (blackMapList.size() == 0) {
			return null;
		}
		
		JSONObject object = (JSONObject) JSONObject.toJSON(blackMapList.get(0));
		
		return object;
	}
	
	/**
	 * 根据内码获得信用分
	 * @param innerID
	 * @return
	 */
	public JSONObject getScore(String innerID) {
		
		DynamicDataSourceHolder.change2oracle();
		List<Map<String, Object>> scoreMapList = scoreDao.getScore(innerID);
		DynamicDataSourceHolder.change2mysql();
		
		if (scoreMapList.size() == 0) {
			return null;
		}
		
		JSONObject object = (JSONObject) JSONObject.toJSON(scoreMapList.get(0));
		
		return object;
	}
	
	/**
	 * 查询本地数据库
	 * @param uuid
	 * @param userID
	 * @param service
	 * @param innerID
	 * @param params
	 * @return
	 */
	public Map<String, Object> getResult(String uuid, String userID, String service, String innerID, JSONObject params) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", true);
		
		Map<String, Object> localApi = localApiDao.queryByService(service);
		
		// 获取表名
		String tblName = String.valueOf(localApi.get("tblName"));
		
		// 获取返回参数
		String returnParam = String.valueOf(localApi.get("returnParam"));
		
		JSONArray paramArray = JSONArray.parseArray(returnParam);
		String[] returnParams = new String[paramArray.size()];
		for (int i = 0; i < paramArray.size(); i++) {
			JSONObject object = (JSONObject) JSONObject.toJSON(paramArray.get(i));
			returnParams[i] = object.getString(Constants.EN_NAME);
		}
		
		// 设置DBEntity
		DBEntity entity = new DBEntity();
		// 表名
		entity.setTableName(tblName);
		// 查询条件
		List<String> fields = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		fields.add("INNERID");
		values.add(innerID);
		entity.setFields(fields);
		entity.setValues(values);
		// 返回值
		String[] selectItems = new String[returnParams.length];
		for (int i = 0; i < returnParams.length; i++) {
			selectItems[i] = returnParams[i].toUpperCase();
		}
		entity.setSelectItems(selectItems);
		// 数据库类型
		entity.setDataBaseType(Constants.DATABASE_TYPE_ORACLE);
		
		DynamicDataSourceHolder.change2oracle();
		Map<String, Object> dbMap = dbOperatorDao.queryData(entity);
		DynamicDataSourceHolder.change2mysql();
		
		if (null == dbMap || dbMap.size() == 0) {
			map.put("isSuccess", false);
		} else {
			map.put("result", JSONObject.toJSON(dbMap));
		}
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userID);
		apiLog.setLocalApiID(String.valueOf(localApi.get("ID")));
		apiLog.setParams(params.toJSONString());
		apiLog.setDataFrom(Constants.DATA_FROM_LOCAL);
		apiLog.setIsSuccess(String.valueOf(map.get("isSuccess")));
		apiLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		apiLogDao.addLog(apiLog);
		
		return map;
	}
}
