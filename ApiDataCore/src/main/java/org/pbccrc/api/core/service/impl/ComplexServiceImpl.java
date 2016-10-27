package org.pbccrc.api.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.service.ComplexService;
import org.pbccrc.api.base.service.QueryApiService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RemoteApiOperator;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.dao.ZhAddressDao;
import org.pbccrc.api.core.dao.ZhCreditCardDao;
import org.pbccrc.api.core.dao.ZhEmploymentDao;
import org.pbccrc.api.core.dao.ZhGuaranteeDao;
import org.pbccrc.api.core.dao.ZhInsideCodeDao;
import org.pbccrc.api.core.dao.ZhLoanDao;
import org.pbccrc.api.core.dao.ZhPersonDao;
import org.pbccrc.api.core.dao.datasource.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class ComplexServiceImpl implements ComplexService{
	
	@Autowired
	private ZhInsideCodeDao zhInsideCodeDao;
	
	@Autowired
	private ZhAddressDao zhAddressDao;
	
	@Autowired
	private ZhEmploymentDao zhEmploymentDao;
	
	@Autowired
	private ZhCreditCardDao zhCreditCardDao;
	
	@Autowired
	private ZhLoanDao zhLoanDao;
	
	@Autowired
	private ZhGuaranteeDao zhGuaranteeDao;
	
	@Autowired
	private ZhPersonDao zhPersonDao;
	
	@Autowired
	private QueryApiService queryApiService;
	
	@Autowired
	private RemoteApiOperator remoteApiOperator;
	
	/**
	 * 失信人查询验证
	 * @param identifier
	 * @return
	 */
	public String validateSxr(String identifier) throws Exception {
		String isNull = "N";
		// 根据身份证号获取内码信息
		DynamicDataSourceHolder.change2oracle();
		Map<String, Object> insideCodeMap = zhInsideCodeDao.queryByCode(identifier);
		DynamicDataSourceHolder.change2mysql();
		if (null == insideCodeMap) {
			isNull = "Y";
		}
		return isNull;
	}
	
	/**
	 * PDF单项查询
	 * @param item
	 * @param identifier
	 * @param localApi
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPdfItem(String service, String identifier, Map<String, Object> localApi) throws Exception {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 根据身份证号获取内码信息
		DynamicDataSourceHolder.change2oracle();
		Map<String, Object> insideCodeMap = zhInsideCodeDao.queryByCode(identifier);
		DynamicDataSourceHolder.change2mysql();
		
		// 获得返回值信息
		String returnParam = String.valueOf(localApi.get("returnParam"));
		String[] returnParams = returnParam.split(Constants.COMMA);
		
		// 内码
		String insideCode = String.valueOf(insideCodeMap.get("INSIDECODE"));
		// 姓名
		String name = String.valueOf(insideCodeMap.get("NAME"));
		
		// 查询结果
		Map<String, Object> queryResult = new HashMap<String, Object>();
		// 返回结果(Map)
		Map<String, Object> returnResult = new HashMap<String, Object>();
		// 返回结果(List)
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		// 返回结果(String)
		String returnStr = Constants.BLANK;
		
		// 判断查询类型
		if (Constants.SERVICE_ZH_PERSON.equals(service)) {
			// 人员基本信息
			DynamicDataSourceHolder.change2oracle();
			queryResult = zhPersonDao.query(insideCode);
			DynamicDataSourceHolder.change2mysql();
			// 根据配置返回信息
			for (String key : returnParams) {
				returnResult.put(key, queryResult.get(key));
			}
			returnStr = JSONObject.toJSONString(returnResult);
		} else if (Constants.SERVICE_ZH_ADDRESS.equals(service)) {
			// 居住信息
			DynamicDataSourceHolder.change2oracle();
			queryResult = zhAddressDao.query(insideCode);
			DynamicDataSourceHolder.change2mysql();
			// 根据配置返回信息
			for (String key : returnParams) {
				returnResult.put(key, queryResult.get(key));
			}
			returnStr = JSONObject.toJSONString(returnResult);
		} else if (Constants.SERVICE_ZH_EMPLOYMENT .equals(service)) {
			// 职业信息
			DynamicDataSourceHolder.change2oracle();
			queryResult = zhEmploymentDao.query(insideCode);
			DynamicDataSourceHolder.change2mysql();
			// 根据配置返回信息
			for (String key : returnParams) {
				returnResult.put(key, queryResult.get(key));
			}
			returnStr = JSONObject.toJSONString(returnResult);
		} else if (Constants.SERVICE_ZH_CREDITCARD .equals(service)) {
			// 信用卡信息
			DynamicDataSourceHolder.change2oracle();
			queryResult = zhCreditCardDao.query(insideCode);
			DynamicDataSourceHolder.change2mysql();
			// 根据配置返回信息
			for (String key : returnParams) {
				returnResult.put(key, queryResult.get(key));
			}
			returnStr = JSONObject.toJSONString(returnResult);
		} else if (Constants.SERVICE_ZH_LOAN.equals(service)) {
			// 贷款信息
			DynamicDataSourceHolder.change2oracle();
			queryResult = zhLoanDao.query(insideCode);
			DynamicDataSourceHolder.change2mysql();
			// 根据配置返回信息
			for (String key : returnParams) {
				returnResult.put(key, queryResult.get(key));
			}
			returnStr = JSONObject.toJSONString(returnResult);
		} else if (Constants.SERVICE_ZH_GUARANTEE .equals(service)) {
			// 担保信息
			DynamicDataSourceHolder.change2oracle();
			returnList = zhGuaranteeDao.query(insideCode);
			DynamicDataSourceHolder.change2mysql();
			JSONArray jsonArray = new JSONArray();
			// 根据配置返回信息
			for (Map<String, Object> map : returnList) {
				for (String key : returnParams) {
					returnResult.put(key, map.get(key));
				}
				jsonArray.add(returnResult);
				returnResult = new HashMap<String, Object>();
			}
			returnStr = jsonArray.toJSONString();
		} else {
			// default nothing
		}
		
		returnResult.put("name", name);
		returnResult.put("idCardNo", identifier);
		returnMap.put("returnStr", returnStr);
		
		return returnMap;
	}
	
	/**
	 * 失信人查询
	 * @param identifier
	 * @return
	 */
	public Map<String, Object> querySxr(String identifier, String[] queryItems) throws Exception{
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("isNull", "N");
		
		// 根据身份证号获取内码信息
		DynamicDataSourceHolder.change2oracle();
		Map<String, Object> insideCodeMap = zhInsideCodeDao.queryByCode(identifier);
		DynamicDataSourceHolder.change2mysql();
		if (null == insideCodeMap) {
			returnMap.put("isNull", "Y");
			return returnMap;
		}
		// 内码
		String insideCode = String.valueOf(insideCodeMap.get("INSIDECODE"));
		// 姓名
		String name = String.valueOf(insideCodeMap.get("NAME"));
		
		// 获取用户信用评分信息
		String score = "暂无分数"; 
		String service = Constants.SERVICE_S_QUERYSCORE;
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("identityCard", new String[]{identifier});
		String result = String.valueOf(queryApiService.query(service, params).get("result"));
		JSONObject resultObj = JSONObject.parseObject(result);
		String resultScore = resultObj.getString("score");
		if (!StringUtil.isNull(resultScore)) {
			score = resultScore;
		}
		returnMap.put("score", score);
		
		// 遍历查询项
		for (String queryItem : queryItems) {
			// 人员基本信息
			if (Constants.ITEM_PERSON.equals(queryItem)) {
				DynamicDataSourceHolder.change2oracle();
				Map<String, Object> personMap = zhPersonDao.query(insideCode);
				if (null == personMap) {
					personMap = new HashMap<String, Object>();
				}
				personMap.put("name", name);
				personMap.put("idCardNo", identifier);
				returnMap.put("person", personMap);
				DynamicDataSourceHolder.change2mysql();
				continue;
			}
			// 居住信息
			if (Constants.ITEM_ADDRESS.equals(queryItem)) {
				DynamicDataSourceHolder.change2oracle();
				Map<String, Object> addressMap = zhAddressDao.query(insideCode);
				if (null == addressMap) {
					addressMap = new HashMap<String, Object>();
				}
				returnMap.put("address", addressMap);
				DynamicDataSourceHolder.change2mysql();
				continue;
			}
			// 就业信息
			if (Constants.ITEM_EMPLOYMENT.equals(queryItem)) {
				DynamicDataSourceHolder.change2oracle();
				Map<String, Object> employmentMap = zhEmploymentDao.query(insideCode);
				if (employmentMap == null) {
					employmentMap = new HashMap<String, Object>();
				}
				returnMap.put("employment", employmentMap);
				DynamicDataSourceHolder.change2mysql();
				continue;
			}
			// 贷款信息
			if (Constants.ITEM_LOAN.equals(queryItem)) {
				DynamicDataSourceHolder.change2oracle();
				Map<String, Object> loanMap = zhLoanDao.query(insideCode);
				if (null == loanMap) {
					loanMap = new HashMap<String, Object>();
				}
				returnMap.put("loan", loanMap);
				DynamicDataSourceHolder.change2mysql();
				continue;
			}
			// 信用卡信息
			if (Constants.ITEM_CREDITCARD.equals(queryItem)) {
				DynamicDataSourceHolder.change2oracle();
				Map<String, Object> creditCardMap = zhCreditCardDao.query(insideCode);
				if (creditCardMap == null) {
					creditCardMap = new HashMap<String, Object>();
				}
				returnMap.put("creditCard", creditCardMap);
				DynamicDataSourceHolder.change2mysql();
				continue;
			}
			// 担保信息
			if (Constants.ITEM_GUARANTEE.equals(queryItem)) {
				DynamicDataSourceHolder.change2oracle();
				List<Map<String, Object>> guaranteeList = zhGuaranteeDao.query(insideCode);
				if (null == guaranteeList) {
					guaranteeList = new ArrayList<Map<String, Object>>();
				}
				returnMap.put("guarantee", guaranteeList);
				DynamicDataSourceHolder.change2mysql();
				continue;
			}
			// 公积金信息
			if (Constants.ITEM_GGJ.equals(queryItem)) {
				JSONArray gjjArray = new JSONArray();
				service = Constants.SERVICE_S_QGJJD;
				params = new HashMap<String, String[]>();
				params.put("NAME", new String[]{name});
				params.put("CERTNO", new String[]{identifier});
				String gjj = String.valueOf(queryApiService.query(service, params).get("result"));
				JSONObject ggjObj = JSONObject.parseObject(gjj);
				JSONObject ggjResult = ggjObj.getJSONObject("Result");
				if (null != ggjResult){
					JSONArray jsonArray = ggjResult.getJSONArray("basicList");
					if (null != jsonArray) {
						gjjArray = jsonArray;
					}
				}
				returnMap.put("gjj", gjjArray);
				continue;
			}
			// 涉法涉诉信息
			if (Constants.ITEM_SFSS.equals(queryItem)) {
				// 涉诉信息(从执行公告中查询某人)
				JSONObject zxggObj = new JSONObject();
				service = Constants.SERVICE_S_UCACCIND_ZXGG;
				params = new HashMap<String, String[]>();
				params.put("pname", new String[]{name});
				params.put("idcardNo", new String[]{identifier});
				String zxgg = String.valueOf(queryApiService.query(service, params).get("result"));
				zxggObj = JSONObject.parseObject(zxgg);
				JSONArray zxggArray = new JSONArray();
				JSONObject zxggResultObject = zxggObj.getJSONObject("Result");
				if (null != zxggResultObject) {
					JSONArray resultZxggArray = zxggResultObject.getJSONArray("zxggList");
					if (null != resultZxggArray){
						zxggArray = resultZxggArray;
						// 设置涉法涉诉类型
						for (Object o : zxggArray){
							JSONObject object = (JSONObject) o;
							object.put("sfssType", "执行公告");
						}
					}
				}
				returnMap.put("zxgg", zxggArray);
				
				// 涉诉信息(从失信公告中查询某人)
				JSONObject sxggObj = new JSONObject();
				service = Constants.SERVICE_S_UCACCIND_SXGG;
				params = new HashMap<String, String[]>();
				params.put("pname", new String[]{name});
				params.put("idcardNo", new String[]{identifier});
				String sxgg = String.valueOf(queryApiService.query(service, params).get("result"));
				sxggObj = JSONObject.parseObject(sxgg);
				JSONArray sxggArray = new JSONArray();
				JSONObject sxggResultObject = sxggObj.getJSONObject("Result");
				if (null != sxggResultObject) {
					JSONArray resultSxggArray = sxggResultObject.getJSONArray("sxggList");
					if (null != resultSxggArray){
						sxggArray = resultSxggArray;
						// 设置涉法涉诉类型
						for (Object o : sxggArray){
							JSONObject object = (JSONObject) o;
							object.put("sfssType", "失信公告");
						}
					}
				}
				returnMap.put("sxgg", sxggArray);
				
				// 涉诉信息(从裁判文书中查询某人)
				JSONObject cpwsObj = new JSONObject();
				service = Constants.SERVICE_S_UCACCIND_CPWS;
				params = new HashMap<String, String[]>();
				params.put("pname", new String[]{name});
				params.put("idcardNo", new String[]{identifier});
				String cpws = String.valueOf(queryApiService.query(service, params).get("result"));
				cpwsObj = JSONObject.parseObject(cpws);
				JSONArray cpwsArray = new JSONArray();
				JSONObject cpwsResultObject = cpwsObj.getJSONObject("Result");
				if (null != cpwsResultObject) {
					JSONArray resultCpwsArray = cpwsResultObject.getJSONArray("cpwsList");
					if (null != resultCpwsArray){
						cpwsArray = resultCpwsArray;
						// 设置涉法涉诉类型
						for (Object o : cpwsArray){
							JSONObject object = (JSONObject) o;
							object.put("sfssType", "裁判文书");
						}
					}
				}
				returnMap.put("cpws", cpwsArray);
				continue;
			}
			// 失信被执行人信息
			if (Constants.ITEM_SXR.equals(queryItem)) {
				String sxr = "暂无信息";
				StringBuffer url = new StringBuffer(Constants.URL_LDB_GETSXR);
				url.append(Constants.URL_CONNECTOR);
				url.append("idCardNo");
				url.append(Constants.EQUAL);
				url.append(identifier);
				sxr = remoteApiOperator.remoteAccept(url.toString());
				returnMap.put("sxr", JSONObject.parse(sxr));
				continue;
			}
		}
		
		return returnMap;
	}

	/**
	 * PDF自定义查询
	 * @param item
	 * @param identifier
	 * @param localApi
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPdfCustom(String identifier, Map<String, Object> localApi) throws Exception {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("isNull", "N");
		
		// 根据身份证号获取内码信息
		DynamicDataSourceHolder.change2oracle();
		Map<String, Object> insideCodeMap = zhInsideCodeDao.queryByCode(identifier);
		if (null == insideCodeMap) {
			returnMap.put("isNull", "Y");
			return returnMap;
		}
		DynamicDataSourceHolder.change2mysql();
		
		// 内码
		String insideCode = String.valueOf(insideCodeMap.get("INSIDECODE"));
		// 姓名
		String name = String.valueOf(insideCodeMap.get("NAME"));
		
		// 查询结果
		Map<String, Object> queryResult = new HashMap<String, Object>();
		// 返回结果(Map)
		Map<String, Object> returnResult = new HashMap<String, Object>();
		// 返回结果(List)
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		// 返回结果(JSON)
		JSONObject returnJson = new JSONObject();
		
		// 获得查询信息
		String params[] = String.valueOf(localApi.get("params")).split(Constants.COMMA);
		
		// 获得返回值信息
		String returnParam = String.valueOf(localApi.get("returnParam"));
		JSONObject returnParamJson = JSONObject.parseObject(returnParam);
		
		// 遍历查询项
		for (String service : params) {
			
			// 判断查询类型
			if (Constants.SERVICE_ZH_PERSON.equals(service)) {
				// 人员基本信息
				DynamicDataSourceHolder.change2oracle();
				queryResult = zhPersonDao.query(insideCode);
				DynamicDataSourceHolder.change2mysql();
				// 获取返回值信息
				String[] returnParams = returnParamJson.getString(service).split(Constants.COMMA);
				// 根据配置返回信息
				for (String key : returnParams) {
					returnResult.put(key, queryResult.get(key));
				}
				returnJson.put(service, returnResult);
			} else if (Constants.SERVICE_ZH_ADDRESS.equals(service)) {
				// 居住信息
				DynamicDataSourceHolder.change2oracle();
				queryResult = zhAddressDao.query(insideCode);
				DynamicDataSourceHolder.change2mysql();
				// 获取返回值信息
				String[] returnParams = returnParamJson.getString(service).split(Constants.COMMA);
				// 根据配置返回信息
				for (String key : returnParams) {
					returnResult.put(key, queryResult.get(key));
				}
				returnJson.put(service, returnResult);
			} else if (Constants.SERVICE_ZH_EMPLOYMENT .equals(service)) {
				// 职业信息
				DynamicDataSourceHolder.change2oracle();
				queryResult = zhEmploymentDao.query(insideCode);
				DynamicDataSourceHolder.change2mysql();
				// 获取返回值信息
				String[] returnParams = returnParamJson.getString(service).split(Constants.COMMA);
				// 根据配置返回信息
				for (String key : returnParams) {
					returnResult.put(key, queryResult.get(key));
				}
				returnJson.put(service, returnResult);
			} else if (Constants.SERVICE_ZH_CREDITCARD .equals(service)) {
				// 信用卡信息
				DynamicDataSourceHolder.change2oracle();
				queryResult = zhCreditCardDao.query(insideCode);
				DynamicDataSourceHolder.change2mysql();
				// 获取返回值信息
				String[] returnParams = returnParamJson.getString(service).split(Constants.COMMA);
				// 根据配置返回信息
				for (String key : returnParams) {
					returnResult.put(key, queryResult.get(key));
				}
				returnJson.put(service, returnResult);
			} else if (Constants.SERVICE_ZH_LOAN.equals(service)) {
				// 贷款信息
				DynamicDataSourceHolder.change2oracle();
				queryResult = zhLoanDao.query(insideCode);
				DynamicDataSourceHolder.change2mysql();
				// 获取返回值信息
				String[] returnParams = returnParamJson.getString(service).split(Constants.COMMA);
				// 根据配置返回信息
				for (String key : returnParams) {
					returnResult.put(key, queryResult.get(key));
				}
				returnJson.put(service, returnResult);
			} else if (Constants.SERVICE_ZH_GUARANTEE .equals(service)) {
				// 担保信息
				DynamicDataSourceHolder.change2oracle();
				returnList = zhGuaranteeDao.query(insideCode);
				DynamicDataSourceHolder.change2mysql();
				// 获取返回值信息
				String[] returnParams = returnParamJson.getString(service).split(Constants.COMMA);
				JSONArray jsonArray = new JSONArray();
				// 根据配置返回信息
				for (Map<String, Object> map : returnList) {
					for (String key : returnParams) {
						returnResult.put(key, map.get(key));
					}
					jsonArray.add(returnResult);
					returnResult = new HashMap<String, Object>();
				}
				returnJson.put(service, jsonArray.toString());
			} else {
				// default nothing
			}
		}
		
		returnResult.put("name", name);
		returnResult.put("idCardNo", identifier);
		returnMap.put("returnStr", returnJson.toString());
		
		return returnMap;
	}
}
