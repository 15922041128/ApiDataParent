package org.pbccrc.api.core.service.impl;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.User;
import org.pbccrc.api.base.service.CreditService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RemoteApiOperator;
import org.pbccrc.api.core.dao.PBaseInfoDao;
import org.pbccrc.api.core.dao.PPersonDao;
import org.pbccrc.api.core.dao.PReditDao;
import org.pbccrc.api.core.dao.ScoreDao;
import org.pbccrc.api.core.dao.ZhIdentificationDao;
import org.pbccrc.api.core.dao.datasource.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class CreditServiceImpl implements CreditService{
	
	@Autowired
	private PPersonDao pPersonDao;
	
	@Autowired
	private PBaseInfoDao pBaseInfoDao;
	
	@Autowired
	private PReditDao pReditDao;
	
	@Autowired
	private ZhIdentificationDao zhIdentificationDao;
	
	@Autowired
	private ScoreDao scoreDao;
	
	@Autowired
	private RemoteApiOperator remoteApiOperator;
	
	/**
	 * 信用风险信息报送step1
	 * @param name
	 * @param idCardNo
	 * @param tel
	 * @param address
	 * @param photo
	 * @param currentUser
	 * @return personID
	 */
	public JSONObject addPerson(String name, String idCardNo, String tel, String address, File photo, String currentUser) throws Exception{
		
		String personID = Constants.BLANK;
		
		Map<String, String> person = new HashMap<String, String>();
		person.put("name", name);
		person.put("idCardNo", idCardNo);
		
		/** p_person表操作 */
		if(pPersonDao.isExist(person) > 0) {
			// 当前person已存在,查询出personID
			Map<String, Object> resPerson = pPersonDao.selectOne(person);
			personID = String.valueOf(resPerson.get(Constants.PERSON_ID));
		} else {
			// 当前person不存在,插入person表数据并返回personID
			pPersonDao.addPerson(person);
			personID = String.valueOf(person.get(Constants.PERSON_ID));
		}
		
		Map<String, Object> pBaseInfo = new HashMap<String, Object>();
		pBaseInfo.put("personID", personID);
		pBaseInfo.put("tel", tel);
		pBaseInfo.put("address", address);
		pBaseInfo.put("photo", photo != null ? photo.getName() : Constants.BLANK);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		/** p_baseInfo表操作 */
		pBaseInfo.put("createUser", currentUser);
		pBaseInfo.put("createTime", format.format(new Date()));
		pBaseInfo.put("updateUser", currentUser);
		pBaseInfo.put("updateTime", format.format(new Date()));
		pBaseInfoDao.addPBaseInfo(pBaseInfo);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("personID", personID);
		jsonObject.put("pBaseInfoID", pBaseInfo.get("ID"));
		
		return jsonObject;
	}
	
	/**
	 * 信用风险信息报送step2
	 * @param personID 			personID
	 * @param contactDate 		合同日期
	 * @param hireDate			起租日
	 * @param expireDate		到期日
	 * @param type				业务类型
	 * @param loanUsed			贷款用途
	 * @param totalAmount		总金额
	 * @param balance			余额
	 * @param status			状态
	 * @param user				当前用户
	 * @return
	 */
	public JSONObject addPersonRedit(String personID, String contactDate, String hireDate, String expireDate, 
			String type, String loanUsed, String totalAmount, String balance, String status, User user) throws Exception {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Map<String, Object> pRedit = new HashMap<String, Object>();
		pRedit.put("personID", personID);
		pRedit.put("contactDate",contactDate);
		pRedit.put("hireDate", hireDate);
		pRedit.put("expireDate", expireDate);
		pRedit.put("type", type);
		pRedit.put("loanUsed", loanUsed);
		pRedit.put("totalAmount", totalAmount);
		pRedit.put("balance", balance);
		pRedit.put("status", status);
		pRedit.put("bizOccurOrg", user.getCompName());
		pRedit.put("createUser", user.getUserName());
		pRedit.put("createTime", format.format(new Date()));
		pRedit.put("updateUser", user.getUserName());
		pRedit.put("updateTime", format.format(new Date()));
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("isSuccess", pReditDao.addPRedit(pRedit) > 0);
		jsonObject.put("pRedit", pRedit);
		
		return jsonObject;
	}
	
	/**
	 * 信用风险信息查询
	 * @param name
	 * @param idCardNo
	 * @return
	 * @throws Exception
	 */
	public JSONObject getReditList(String name, String idCardNo) throws Exception {
		
		JSONObject returnObj = new JSONObject();
		
		// 获取用户ID
		Map<String, String> person = new HashMap<String, String>();
		person.put("name", name);
		person.put("idCardNo", idCardNo);
		Map<String, Object> resPerson = pPersonDao.selectOne(person);
		if (null == resPerson) {
			returnObj.put("status", "error");
			return returnObj;
		}
		String personID = String.valueOf(resPerson.get(Constants.PERSON_ID));
		returnObj.put("person", person);
		
		// 根据身份证号获取内码信息
		Map<String, Object> insideCodeMap = null;
		try {
			DynamicDataSourceHolder.change2oracle();
			insideCodeMap = zhIdentificationDao.getInnerID(name, idCardNo);
			DynamicDataSourceHolder.change2mysql();
		} catch (Exception e) {
			throw e;
		} finally {
			DynamicDataSourceHolder.change2mysql();
		}
		if (null == insideCodeMap) {
			returnObj.put("status", "error");
			return returnObj;
		}
		// 内码
		String innerID = String.valueOf(insideCodeMap.get("INNERID"));
		
		// 获取用户信用评分信息
		String score = "暂无分数";
		try {
			DynamicDataSourceHolder.change2oracle();
			List<Map<String, Object>> scoreList = scoreDao.getScore(innerID);
			DynamicDataSourceHolder.change2mysql();
			if (null != scoreList && 0 != scoreList.size()) {
				Map<String, Object> scoreMap = scoreList.get(0);
				score = String.valueOf(scoreMap.get("SCORE"));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DynamicDataSourceHolder.change2mysql();
		}
		returnObj.put("score", score);
		
		// 获取用户基本信息
		List<Map<String, Object>> pBaseInfoList = pBaseInfoDao.queryByPersonID(personID);
		if (null == pBaseInfoList || pBaseInfoList.size() == 0) {
			returnObj.put("pBaseInfo", null);
		} else {
			returnObj.put("pBaseInfo", pBaseInfoList.get(0));
		}
		
		// 获取用户信贷信息
		List<Map<String, Object>> reditList = pReditDao.queryAll(personID);
		JSONArray array = new JSONArray();
		for (int i = 0 ; i < reditList.size(); i++) {
			Map<String, Object> map = reditList.get(i);
			array.add(map);
		}
		returnObj.put("reditList", array);
		
		// 被失信人被执行信息
		StringBuffer url = new StringBuffer(Constants.WEB_URL + Constants.URL_LDB_GETSXR);
		url.append(Constants.URL_CONNECTOR);
		url.append("identifier");
		url.append(Constants.EQUAL);
		url.append(idCardNo);
		url.append(Constants.URL_PARAM_CONNECTOR);
		url.append("name");
		url.append(Constants.EQUAL);
		url.append(URLEncoder.encode(name, "UTF-8"));
		String sxr = remoteApiOperator.remoteAccept(url.toString());
		returnObj.put("sxr", JSONArray.parse(sxr));
		
		return returnObj;
	}
	
}
