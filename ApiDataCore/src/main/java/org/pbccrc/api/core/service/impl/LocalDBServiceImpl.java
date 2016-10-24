package org.pbccrc.api.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.service.LocalDBService;
import org.pbccrc.api.base.bean.DBEntity;
import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.core.dao.DBOperatorDao;
import org.pbccrc.api.core.dao.LdbApiDao;
import org.pbccrc.api.core.dao.ZhInsideCodeDao;
import org.pbccrc.api.base.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class LocalDBServiceImpl implements LocalDBService {
	
	@Autowired
	private DBOperatorDao dbOperatorDao;
	
	@Autowired
	private LdbApiDao ldbApiDao;
	
	@Autowired
	private ZhInsideCodeDao zhInsideCodeDao;
	
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
			resultContent.setErrNum(Constants.CODE_ERR_FAIL);
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
	 * @param idCardNo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryApi(String service, String idCardNo) throws Exception {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("isNull", "N");
		
		// 根据身份证号获取内码信息
		Map<String, Object> insideCodeMap = zhInsideCodeDao.queryByCode(idCardNo);
		if (null == insideCodeMap) {
			returnMap.put("isNull", "Y");
			return returnMap;
		}
		// 内码
		String insideCode = String.valueOf(insideCodeMap.get("insideCode"));
		
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
		values.add(insideCode);
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
}
