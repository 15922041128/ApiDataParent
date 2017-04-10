package org.pbccrc.api.core.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.service.ApiLogService;
import org.pbccrc.api.base.service.ComplexService;
import org.pbccrc.api.base.service.LocalDBService;
import org.pbccrc.api.base.service.QueryApiService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RemoteApiOperator;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.dao.ZhAddressDao;
import org.pbccrc.api.core.dao.ZhCreditCardDao;
import org.pbccrc.api.core.dao.ZhEmploymentDao;
import org.pbccrc.api.core.dao.ZhGuaranteeDao;
import org.pbccrc.api.core.dao.ZhIdentificationDao;
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
	private ZhIdentificationDao zhIdentificationDao;
	
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
	
	@Autowired
	private ApiLogService apiLogService;
	
	@Autowired
	private LocalDBService localDBService;
	
	/**
	 * 失信人查询验证
	 * @param name
	 * @param identifier
	 * @return
	 */
	public String validateSxr(String name, String identifier) throws Exception {
		String isNull = "N";
		// 根据身份证号获取内码信息
		Map<String, Object> insideCodeMap = null;
		try {
			DynamicDataSourceHolder.change2oracle();
			insideCodeMap = zhIdentificationDao.getInnerID(name, identifier);
			DynamicDataSourceHolder.change2mysql();
		} catch (Exception e) {
			throw e;
		} finally {
			DynamicDataSourceHolder.change2mysql();
		}
		
		if (null == insideCodeMap) {
			isNull = "Y";
		}
		return isNull;
	}
	
//	/**
//	 * PDF单项查询
//	 * @param item
//	 * @param name
//	 * @param identifier
//	 * @param localApi
//	 * @return
//	 * @throws Exception
//	 */
//	public Map<String, Object> getPdfItem(String uuid, String userID, String service, 
//			String name, String identifier, String telNum, Map<String, Object> localApi) throws Exception {
//		
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		returnMap.put("isNull", "N");
//		
//		// 内码
//		String innerID = Constants.BLANK;
//		// 查询参数(记录日志用)
//		JSONObject paramObj = new JSONObject();
//		// 判断查询方式
//		String params = (String) localApi.get("params");
//		JSONArray array = JSONArray.parseArray(params);
//		if (array.size() == 1) {
//			// size为1 则通过电话号码查询内码
//			innerID = localDBService.getInnerIDByTelNum(telNum);
//			paramObj.put("telNum", telNum);
//		} else {
//			// size为2 则通过两标查询内码
//			innerID = localDBService.getInnerID(name, identifier);
//			paramObj.put("name", name);
//			paramObj.put("identifier", identifier);
//		}
//		
//		if (Constants.BLANK.equals(innerID)) {
//			returnMap.put("isNull", "Y");
//		} else {
//			// 获得返回值信息
//			String returnParam = String.valueOf(localApi.get("returnParam"));
//			
//			JSONArray paramArray = JSONArray.parseArray(returnParam);
//			String[] returnParams = new String[paramArray.size()];
//			for (int i = 0; i < paramArray.size(); i++) {
//				JSONObject object = (JSONObject) JSONObject.toJSON(paramArray.get(i));
//				returnParams[i] = object.getString(Constants.EN_NAME);
//			}
//			
//			// 返回结果(Map)
//			Map<String, Object> returnResult = new HashMap<String, Object>();
//			// 返回结果(List)
//			List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
//			// 返回结果
//			Object returnStr = null;
//			
//			// 判断查询类型
//			if (Constants.SERVICE_ZH_PERSON2.startsWith(service)) {
//				// 人员基本信息
//				DynamicDataSourceHolder.change2oracle();
//				returnList = zhPersonDao.query(innerID);
//				DynamicDataSourceHolder.change2mysql();
//				JSONArray jsonArray = new JSONArray();
//				// 根据配置返回信息
//				if (null != returnList) {
//					for (Map<String, Object> map : returnList) {
//						for (String key : returnParams) {
//							returnResult.put(key, map.get(key.toUpperCase()));
//						}
//						jsonArray.add(returnResult);
//						returnResult = new HashMap<String, Object>();
//					}
//				}
//				returnStr = jsonArray;
//			} else if (Constants.SERVICE_ZH_ADDRESS2.startsWith(service)) {
//				// 居住信息
//				DynamicDataSourceHolder.change2oracle();
//				returnList = zhAddressDao.query(innerID);
//				DynamicDataSourceHolder.change2mysql();
//				JSONArray jsonArray = new JSONArray();
//				// 根据配置返回信息
//				if (null != returnList) {
//					for (Map<String, Object> map : returnList) {
//						for (String key : returnParams) {
//							returnResult.put(key, map.get(key.toUpperCase()));
//						}
//						jsonArray.add(returnResult);
//						returnResult = new HashMap<String, Object>();
//					}
//				}
//				returnStr = jsonArray;
//			} else if (Constants.SERVICE_ZH_EMPLOYMENT2.startsWith(service)) {
//				// 职业信息
//				DynamicDataSourceHolder.change2oracle();
//				returnList = zhEmploymentDao.query(innerID);
//				DynamicDataSourceHolder.change2mysql();
//				JSONArray jsonArray = new JSONArray();
//				// 根据配置返回信息
//				if (null != returnList) {
//					for (Map<String, Object> map : returnList) {
//						for (String key : returnParams) {
//							returnResult.put(key, map.get(key.toUpperCase()));
//						}
//						jsonArray.add(returnResult);
//						returnResult = new HashMap<String, Object>();
//					}
//				}
//				returnStr = jsonArray;
//			} else if (Constants.SERVICE_ZH_CREDITCARD2.startsWith(service)) {
//				// 信用卡信息
//				DynamicDataSourceHolder.change2oracle();
//				returnList = zhCreditCardDao.query(innerID);
//				DynamicDataSourceHolder.change2mysql();
//				JSONArray jsonArray = new JSONArray();
//				// 根据配置返回信息
//				if (null != returnList) {
//					for (Map<String, Object> map : returnList) {
//						for (String key : returnParams) {
//							returnResult.put(key, map.get(key.toUpperCase()));
//						}
//						jsonArray.add(returnResult);
//						returnResult = new HashMap<String, Object>();
//					}
//				}
//				returnStr = jsonArray;
//			} else if (Constants.SERVICE_ZH_CREDITCARD_ALL2.startsWith(service)) {
//				// 信用卡信息
//				DynamicDataSourceHolder.change2oracle();
//				returnList = zhCreditCardDao.query(innerID);
//				DynamicDataSourceHolder.change2mysql();
//				JSONArray jsonArray = new JSONArray();
//				// 根据配置返回信息
//				if (null != returnList) {
//					for (Map<String, Object> map : returnList) {
//						for (String key : returnParams) {
//							returnResult.put(key, map.get(key.toUpperCase()));
//						}
//						jsonArray.add(returnResult);
//						returnResult = new HashMap<String, Object>();
//					}
//				}
//				returnStr = jsonArray;
//			}else if (Constants.SERVICE_ZH_LOAN2.startsWith(service)) {
//				// 贷款信息
//				DynamicDataSourceHolder.change2oracle();
//				returnList = zhLoanDao.query(innerID);
//				DynamicDataSourceHolder.change2mysql();
//				JSONArray jsonArray = new JSONArray();
//				// 根据配置返回信息
//				if (null != returnList) {
//					for (Map<String, Object> map : returnList) {
//						for (String key : returnParams) {
//							returnResult.put(key, map.get(key.toUpperCase()));
//						}
//						jsonArray.add(returnResult);
//						returnResult = new HashMap<String, Object>();
//					}
//				}
//				returnStr = jsonArray;
//			} else if (Constants.SERVICE_ZH_GUARANTEE2.startsWith(service)) {
//				// 担保信息
//				DynamicDataSourceHolder.change2oracle();
//				returnList = zhGuaranteeDao.query(innerID);
//				DynamicDataSourceHolder.change2mysql();
//				JSONArray jsonArray = new JSONArray();
//				// 根据配置返回信息
//				if (null != returnList) {
//					for (Map<String, Object> map : returnList) {
//						for (String key : returnParams) {
//							returnResult.put(key, map.get(key.toUpperCase()));
//						}
//						jsonArray.add(returnResult);
//						returnResult = new HashMap<String, Object>();
//					}
//				}
//				returnStr = jsonArray;
//			} else {
//				// default nothing
//			}
//			returnResult.put("name", name);
//			returnResult.put("idCardNo", identifier);
//			returnMap.put("returnStr", returnStr);
//		}
//		
//		// 记录日志
//		ApiLog apiLog = new ApiLog();
//		// uuid
//		apiLog.setUuid(uuid);
//		apiLog.setUserID(userID);
//		apiLog.setLocalApiID(String.valueOf(localApi.get("ID")));
//		// 参数
//		apiLog.setParams(paramObj.toJSONString());
//		apiLog.setDataFrom(Constants.DATA_FROM_LOCAL);
//		apiLog.setIsSuccess(String.valueOf(!"Y".equals(String.valueOf(returnMap.get("isNull")))));
//		apiLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		apiLogService.addLog(apiLog);
//		
//		return returnMap;
//	}
	
	/**
	 * 失信人查询
	 * @param identifier
	 * @return
	 */
	public Map<String, Object> querySxr(String uuid, String userID, String name, String identifier, String[] queryItems) throws Exception{
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("isNull", "N");
		
		// 根据身份证号获取内码信息
		Map<String, Object> insideCodeMap = null;
		try {
			DynamicDataSourceHolder.change2oracle();
			insideCodeMap = zhIdentificationDao.getInnerID(name, identifier);
			DynamicDataSourceHolder.change2mysql();
		} catch (Exception e) {
			throw e;
		} finally {
			DynamicDataSourceHolder.change2mysql();
		}
	
		if (null == insideCodeMap) {
			returnMap.put("isNull", "Y");
		} else {
			// 内码
			String innerID = String.valueOf(insideCodeMap.get("INNERID"));
			
			// 获取用户信用评分信息
			String score = "暂无分数"; 
			String service = Constants.SERVICE_L_SCORE2;
			Map<String, String[]> params = new HashMap<String, String[]>();
			params.put("identifier", new String[]{identifier});
			params.put("name", new String[]{name});
			Object result = localDBService.getResult(uuid, userID, service, innerID, (JSONObject)JSONObject.toJSON(params)).get("result");
			JSONObject resultObj = null;
			if (result instanceof String) {
				// String
				resultObj = JSONObject.parseObject(String.valueOf(result));
			} else {
				// Object
				resultObj = (JSONObject) JSONObject.toJSON(result);
			}
			String resultScore = resultObj.getString("SCORE");
			if (!StringUtil.isNull(resultScore)) {
				score = resultScore;
			}
			returnMap.put("SCORE", score);
			
			// 遍历查询项
			for (String queryItem : queryItems) {
				// 人员基本信息
				if (Constants.ITEM_PERSON.equals(queryItem)) {
					try {
						DynamicDataSourceHolder.change2oracle();
						Map<String, Object> personMap = null;
						List<Map<String, Object>> personMapList = zhPersonDao.query(innerID);
						if (null == personMapList || personMapList.size() == 0) {
							personMap = new HashMap<String, Object>();
						} else {
							personMap = personMapList.get(0);
						}
						personMap.put("name", name);
						personMap.put("idCardNo", identifier);
						returnMap.put("person", personMap);
						DynamicDataSourceHolder.change2mysql();
					} catch (Exception e) {
						throw e;
					} finally {
						DynamicDataSourceHolder.change2mysql();
					}
					
					continue;
				}
				// 居住信息
				if (Constants.ITEM_ADDRESS.equals(queryItem)) {
					try {
						DynamicDataSourceHolder.change2oracle();
						Map<String, Object> addressMap = null;
						List<Map<String, Object>> addressMapList = zhAddressDao.query(innerID);
						if (null == addressMapList || addressMapList.size() == 0) {
							addressMap = new HashMap<String, Object>();
						} else {
							addressMap = addressMapList.get(0);
						}
						returnMap.put("address", addressMap);
						DynamicDataSourceHolder.change2mysql();
					} catch (Exception e) {
						throw e;
					} finally {
						DynamicDataSourceHolder.change2mysql();
					}
					
					continue;
				}
				// 就业信息
				if (Constants.ITEM_EMPLOYMENT.equals(queryItem)) {
					try {
						DynamicDataSourceHolder.change2oracle();
						Map<String, Object> employmentMap = null;
						List<Map<String, Object>> employmentMapList = zhEmploymentDao.query(innerID);
						if (null == employmentMapList || employmentMapList.size() == 0) {
							employmentMap = new HashMap<String, Object>();
						} else {
							employmentMap = employmentMapList.get(0);
						}
						returnMap.put("employment", employmentMap);
						DynamicDataSourceHolder.change2mysql();
					} catch (Exception e) {
						throw e;
					} finally {
						DynamicDataSourceHolder.change2mysql();
					}
				
					continue;
				}
				// 贷款信息
				if (Constants.ITEM_LOAN.equals(queryItem)) {
					try {
						DynamicDataSourceHolder.change2oracle();
						Map<String, Object> loanMap = null;
						List<Map<String, Object>> loanMapList = zhLoanDao.query(innerID);
						if (null == loanMapList || loanMapList.size() == 0) {
							loanMap = new HashMap<String, Object>();
						} else {
							loanMap = loanMapList.get(0);
						}
						returnMap.put("loan", loanMap);
						DynamicDataSourceHolder.change2mysql();
					} catch (Exception e) {
						throw e;
					} finally {
						DynamicDataSourceHolder.change2mysql();
					}
					
					continue;
				}
				// 信用卡信息
				if (Constants.ITEM_CREDITCARD.equals(queryItem)) {
					try {
						DynamicDataSourceHolder.change2oracle();
						Map<String, Object> creditCardMap = null;
						List<Map<String, Object>> creditCardMapList = zhCreditCardDao.query(innerID);
						if (null == creditCardMapList || creditCardMapList.size() == 0) {
							creditCardMap = new HashMap<String, Object>();
						} else {
							creditCardMap = creditCardMapList.get(0);
						}
						returnMap.put("creditCard", creditCardMap);
						DynamicDataSourceHolder.change2mysql();
					} catch (Exception e) {
						throw e;
					} finally {
						DynamicDataSourceHolder.change2mysql();
					}
					
					continue;
				}
				// 担保信息
				if (Constants.ITEM_GUARANTEE.equals(queryItem)) {
					try {
						DynamicDataSourceHolder.change2oracle();
						List<Map<String, Object>> guaranteeList = zhGuaranteeDao.query(innerID);
						if (null == guaranteeList) {
							guaranteeList = new ArrayList<Map<String, Object>>();
						}
						returnMap.put("guarantee", guaranteeList);
						DynamicDataSourceHolder.change2mysql();
					} catch (Exception e) {
						throw e;
					} finally {
						DynamicDataSourceHolder.change2mysql();
					}
					
					continue;
				}
				// 公积金信息
				if (Constants.ITEM_GGJ.equals(queryItem)) {
					JSONArray gjjArray = new JSONArray();
					service = Constants.SERVICE_S_QGJJD;
					params = new HashMap<String, String[]>();
					params.put("NAME", new String[]{name});
					params.put("CERTNO", new String[]{identifier});
					String gjj = String.valueOf(queryApiService.query(uuid, userID, service, params).get("result"));
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
					result = queryApiService.query(uuid, userID, service, params).get("result");
					if (result instanceof String) {
						// String
						zxggObj = JSONObject.parseObject(String.valueOf(result));
					} else {
						// Object
						zxggObj = (JSONObject) JSONObject.toJSON(result);
					}
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
//					String sxgg = String.valueOf(queryApiService.query(uuid, userID, service, params).get("result"));
					result = queryApiService.query(uuid, userID, service, params).get("result");
					if (result instanceof String) {
						// String
						sxggObj = JSONObject.parseObject(String.valueOf(result));
					} else {
						// Object
						sxggObj = (JSONObject) JSONObject.toJSON(result);
					}
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
//					String cpws = String.valueOf(queryApiService.query(uuid, userID, service, params).get("result"));
					result = queryApiService.query(uuid, userID, service, params).get("result");
					if (result instanceof String) {
						// String
						cpwsObj = JSONObject.parseObject(String.valueOf(result));
					} else {
						// Object
						cpwsObj = (JSONObject) JSONObject.toJSON(result);
					}
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
					StringBuffer url = new StringBuffer(Constants.WEB_URL + Constants.URL_LDB_GETSXR);
					url.append(Constants.URL_CONNECTOR);
					url.append("identifier");
					url.append(Constants.EQUAL);
					url.append(identifier);
					url.append(Constants.URL_PARAM_CONNECTOR);
					url.append("name");
					url.append(Constants.EQUAL);
					url.append(URLEncoder.encode(name, "UTF-8"));
					sxr = remoteApiOperator.remoteAccept(url.toString());
					returnMap.put("sxr", JSONObject.parse(sxr));
					continue;
				}
			}
		}
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userID);
		apiLog.setLocalApiID(Constants.API_ID_PAGE_PDF);
		// 参数
		JSONObject insDBParams = new JSONObject();
		insDBParams.put("identifier", identifier);
		insDBParams.put("queryItems", queryItems);
		apiLog.setParams(insDBParams.toJSONString());
		apiLog.setDataFrom(Constants.DATA_FROM_LOCAL);
		apiLog.setIsSuccess(String.valueOf(!"Y".equals(String.valueOf(returnMap.get("isNull")))));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogService.addLog(apiLog);
		
		return returnMap;
	}

//	/**
//	 * PDF自定义查询
//	 * @param item
//	 * @param name
//	 * @param identifier
//	 * @param localApi
//	 * @return
//	 * @throws Exception
//	 */
//	public Map<String, Object> getPdfCustom(String uuid, String userID, String name, String identifier, Map<String, Object> localApi) throws Exception {
//		
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		returnMap.put("isNull", "N");
//		
//		// 根据身份证号获取内码信息
//		DynamicDataSourceHolder.change2oracle();
//		Map<String, Object> insideCodeMap = zhIdentificationDao.getInnerID(name, identifier);
//		DynamicDataSourceHolder.change2mysql();
//		
//		if (null == insideCodeMap) {
//			returnMap.put("isNull", "Y");
//		} else {
//			// 内码
//			String innerID = String.valueOf(insideCodeMap.get("INNERID"));
//			
//			// 查询结果
//			Map<String, Object> queryResult = new HashMap<String, Object>();
//			// 返回结果(Map)
//			Map<String, Object> returnResult = new HashMap<String, Object>();
//			// 返回结果(List)
//			List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
//			// 返回结果(JSON)
//			JSONObject returnJson = new JSONObject();
//			
//			// 获得查询信息
//			String params[] = String.valueOf(localApi.get("params")).split(Constants.COMMA);
//			
//			// 获得返回值信息
//			String returnParam = String.valueOf(localApi.get("returnParam"));
//			JSONObject returnParamJson = JSONObject.parseObject(returnParam);
//			
//			// 遍历查询项
//			for (String service : params) {
//				
//				// 判断查询类型
//				if (Constants.SERVICE_ZH_PERSON.equals(service)) {
//					// 人员基本信息
//					returnResult = new HashMap<String, Object>();
//					DynamicDataSourceHolder.change2oracle();
//					queryResult = zhPersonDao.query(innerID);
//					DynamicDataSourceHolder.change2mysql();
//					// 获取返回值信息
//					String[] returnParams = returnParamJson.getString(service).split(Constants.COMMA);
//					// 根据配置返回信息
//					for (String key : returnParams) {
//						returnResult.put(key, queryResult.get(key.toUpperCase()));
//					}
//					returnJson.put(service, returnResult);
//				} else if (Constants.SERVICE_ZH_ADDRESS.equals(service)) {
//					// 居住信息
//					returnResult = new HashMap<String, Object>();
//					DynamicDataSourceHolder.change2oracle();
//					queryResult = zhAddressDao.query(innerID);
//					DynamicDataSourceHolder.change2mysql();
//					// 获取返回值信息
//					String[] returnParams = returnParamJson.getString(service).split(Constants.COMMA);
//					// 根据配置返回信息
//					for (String key : returnParams) {
//						returnResult.put(key, queryResult.get(key.toUpperCase()));
//					}
//					returnJson.put(service, returnResult);
//				} else if (Constants.SERVICE_ZH_EMPLOYMENT .equals(service)) {
//					// 职业信息
//					returnResult = new HashMap<String, Object>();
//					DynamicDataSourceHolder.change2oracle();
//					queryResult = zhEmploymentDao.query(innerID);
//					DynamicDataSourceHolder.change2mysql();
//					// 获取返回值信息
//					String[] returnParams = returnParamJson.getString(service).split(Constants.COMMA);
//					// 根据配置返回信息
//					for (String key : returnParams) {
//						returnResult.put(key, queryResult.get(key.toUpperCase()));
//					}
//					returnJson.put(service, returnResult);
//				} else if (Constants.SERVICE_ZH_CREDITCARD .equals(service)) {
//					// 信用卡信息
//					returnResult = new HashMap<String, Object>();
//					DynamicDataSourceHolder.change2oracle();
//					queryResult = zhCreditCardDao.query(innerID);
//					DynamicDataSourceHolder.change2mysql();
//					// 获取返回值信息
//					String[] returnParams = returnParamJson.getString(service).split(Constants.COMMA);
//					// 根据配置返回信息
//					for (String key : returnParams) {
//						returnResult.put(key, queryResult.get(key.toUpperCase()));
//					}
//					returnJson.put(service, returnResult);
//				} else if (Constants.SERVICE_ZH_LOAN.equals(service)) {
//					// 贷款信息
//					returnResult = new HashMap<String, Object>();
//					DynamicDataSourceHolder.change2oracle();
//					queryResult = zhLoanDao.query(innerID);
//					DynamicDataSourceHolder.change2mysql();
//					// 获取返回值信息
//					String[] returnParams = returnParamJson.getString(service).split(Constants.COMMA);
//					// 根据配置返回信息
//					for (String key : returnParams) {
//						returnResult.put(key, queryResult.get(key.toUpperCase()));
//					}
//					returnJson.put(service, returnResult);
//				} else if (Constants.SERVICE_ZH_GUARANTEE .equals(service)) {
//					// 担保信息
//					DynamicDataSourceHolder.change2oracle();
//					returnList = zhGuaranteeDao.query(innerID);
//					DynamicDataSourceHolder.change2mysql();
//					// 获取返回值信息
//					String[] returnParams = returnParamJson.getString(service).split(Constants.COMMA);
//					JSONArray jsonArray = new JSONArray();
//					// 根据配置返回信息
//					for (Map<String, Object> map : returnList) {
//						Map<String, Object> guaranteeMap = new HashMap<String, Object>();
//						for (String key : returnParams) {
//							guaranteeMap.put(key, map.get(key.toUpperCase()));
//						}
//						jsonArray.add(guaranteeMap);
//					}
//					returnJson.put(service, jsonArray);
//				} else {
//					// default nothing
//				}
//			}
//			
//			returnResult.put("name", name);
//			returnResult.put("idCardNo", identifier);
//			returnMap.put("returnStr", returnJson);
//		}
//		
//		// 记录日志
//		ApiLog apiLog = new ApiLog();
//		// uuid
//		apiLog.setUuid(uuid);
//		apiLog.setUserID(userID);
//		apiLog.setLocalApiID(String.valueOf(localApi.get("ID")));
//		// 参数
//		JSONObject insDBParams = new JSONObject();
//		insDBParams.put("identifier", identifier);
//		apiLog.setParams(insDBParams.toJSONString());
//		apiLog.setDataFrom(Constants.DATA_FROM_LOCAL);
//		apiLog.setIsSuccess(String.valueOf(!"Y".equals(String.valueOf(returnMap.get("isNull")))));
//		apiLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		apiLogService.addLog(apiLog);
//		
//		return returnMap;
//		return null;
//	}
}
