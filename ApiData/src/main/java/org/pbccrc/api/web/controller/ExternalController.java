package org.pbccrc.api.web.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;

import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.base.external.vip.grcredit.Conts;
import org.pbccrc.api.base.service.CostService;
import org.pbccrc.api.base.service.ExternalService;
import org.pbccrc.api.base.service.SystemLogService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.DesUtils;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.base.util.SystemUtil;
import org.pbccrc.api.base.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/external")
public class ExternalController {
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private CostService costService;
	
	@Autowired
	private ExternalService externalService;

	/**
	 * 唯品会个人风险查询
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/vip/webapp/queryBlackList", produces={"application/json;charset=UTF-8"})
	public Object vipQueryBlackList(String requestStr, HttpServletRequest request) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		// 获取ip地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_VIP_QUERYBLACKLIST, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		String idNo = json.getString("idNo");
		String cardName = json.getString("cardName");
		String phone = json.getString("phone");
		String cardNo = json.getString("cardNo");
		
		String timestamp = startTime + "";
        SortedMap<String, Object> sortedMap = new TreeMap<String, Object>();
        sortedMap.put("innerreqId", Conts.appid + timestamp);
        
		if (!StringUtil.isNull(idNo)) {
			sortedMap.put("idNo", idNo);
		}
		if (!StringUtil.isNull(cardName)) {
			sortedMap.put("cardName", cardName);
		}
		if (!StringUtil.isNull(phone)) {
			sortedMap.put("phone", phone);
		}
		if (!StringUtil.isNull(cardNo)) {
			sortedMap.put("cardNo", cardNo);
		}
        
        // 生成UUID
     	String uuid = StringUtil.createUUID();
        
     	JSONObject resultJson = externalService.vipQueryBlackList(idNo, cardName, phone, cardNo, userID, uuid);
     	
     	JSONObject resultObject = JSONObject.parseObject(resultJson.getString("result"));
     	
     	boolean isSuccess = resultJson.getBoolean("isSuccess");
     	
        resultContent.setRetData(resultObject);
        
        // get body
        String body = resultObject.getString("body");
        
        if (!isSuccess) {
        	resultContent.setCode(Constants.CODE_ERR_FAIL);
			resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
        } else {
        	// 计费
			Map<String, Object> costRetMap = costService.cost(userID, apiKey);
			String queryCount = String.valueOf(costRetMap.get("queryCount"));
			// 查询次数
			resultContent.setQueryCount(queryCount);
        }
        
        resultContent.setRetData(body);
        
        long endTime = System.currentTimeMillis();
        
        // 记录日志
 		SystemLog systemLog = new SystemLog();
 		// uuid
 		systemLog.setUuid(uuid);
 		// ip地址
 		systemLog.setIpAddress(ipAddress);
 		// apiKey
 		systemLog.setApiKey(apiKey);
 		// 产品ID
 		// 从缓存中获取relation对象
 		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
 		systemLog.setProductID(relation.getString("productID"));
 		// localApiID
 		systemLog.setLocalApiID(Constants.API_ID_VIP_QUERYBLACKLIST);
 		// 参数
 		systemLog.setParams(JSON.toJSONString(sortedMap));
 		// 用户ID
 		systemLog.setUserID(userID);
 		// 是否成功
 		systemLog.setIsSuccess(String.valueOf(isSuccess));
 		// 是否计费
 		systemLog.setIsCount(String.valueOf(isSuccess));
 		// 查询时间
 		systemLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FORMAT_SYSTEMLOG).format(new Date()));
 		// 查询用时
 		systemLog.setQueryTime(endTime - startTime);
 		// 返回数据
 		systemLog.setReturnData(resultObject.toJSONString());
 		systemLogService.addLog(systemLog);
    
 		return JSONObject.toJSON(resultContent);
	}
	
	
	/**
	 * 凭安电话标签查询
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/pa/phoneTag", produces={"application/json;charset=UTF-8"})
	public Object paPhoneTag(String requestStr, HttpServletRequest request) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		// 获取ip地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_PA_PHONE_TAG, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		String phone = json.getString("phone");
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
		
		JSONObject resultJson = externalService.paPhoneTag(phone, userID, uuid);
     	
     	JSONObject resultObject = JSONObject.parseObject(resultJson.getString("result"));
     	
     	boolean isSuccess = resultJson.getBoolean("isSuccess");
     	
        resultContent.setRetData(resultObject);
        
        // get data
        String data = resultObject.getString("data");
        
        // get message
        String message = resultObject.getString("message");
        
        if (!isSuccess) {
        	resultContent.setCode(Constants.CODE_ERR_FAIL);
			resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
        } else {
        	// 计费
			Map<String, Object> costRetMap = costService.cost(userID, apiKey);
			String queryCount = String.valueOf(costRetMap.get("queryCount"));
			// 查询次数
			resultContent.setQueryCount(queryCount);
        }
        
        // 判断message是否为空
        if (StringUtil.isNull(message)) {
        	resultContent.setRetData(data);
        } else {
        	resultContent.setRetData(message);	
        }
        
        long endTime = System.currentTimeMillis();
        
        // 记录日志
 		SystemLog systemLog = new SystemLog();
 		// uuid
 		systemLog.setUuid(uuid);
 		// ip地址
 		systemLog.setIpAddress(ipAddress);
 		// apiKey
 		systemLog.setApiKey(apiKey);
 		// 产品ID
 		// 从缓存中获取relation对象
 		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
 		systemLog.setProductID(relation.getString("productID"));
 		// localApiID
 		systemLog.setLocalApiID(Constants.API_ID_PA_PHONE_TAG);
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
		param.put("phone", phone);
 		systemLog.setParams(JSON.toJSONString(param));
 		// 用户ID
 		systemLog.setUserID(userID);
 		// 是否成功
 		systemLog.setIsSuccess(String.valueOf(isSuccess));
 		// 是否计费
 		systemLog.setIsCount(String.valueOf(isSuccess));
 		// 查询时间
 		systemLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FORMAT_SYSTEMLOG).format(new Date()));
 		// 查询用时
 		systemLog.setQueryTime(endTime - startTime);
 		// 返回数据
 		systemLog.setReturnData(resultObject.toJSONString().replace("\\", ""));
 		systemLogService.addLog(systemLog);
    
 		return JSONObject.toJSON(resultContent);
	}
	
	/**
	 * 凭安失信被执行人查询
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/pa/shixin", produces={"application/json;charset=UTF-8"})
	public Object paShixin(String requestStr, HttpServletRequest request) throws Exception {
	
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		// 获取ip地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_PA_SHIXIN, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr, "utf-8"));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		String name = json.getString("name");
		
		String idCard = json.getString("idCard");
		
		String orgName = json.getString("orgName");
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
		
		JSONObject resultJson = externalService.paShixin(name, idCard, orgName, userID, uuid);
     	
     	JSONObject resultObject = JSONObject.parseObject(resultJson.getString("result"));
     	
     	boolean isSuccess = resultJson.getBoolean("isSuccess");
     	
        resultContent.setRetData(resultObject);
        
        // get data
        String data = resultObject.getString("data");
        
        // get message
        String message = resultObject.getString("message");
        
        if (!isSuccess) {
        	resultContent.setCode(Constants.CODE_ERR_FAIL);
			resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
        } else {
        	// 计费
			Map<String, Object> costRetMap = costService.cost(userID, apiKey);
			String queryCount = String.valueOf(costRetMap.get("queryCount"));
			// 查询次数
			resultContent.setQueryCount(queryCount);
        }
        
        // 判断message是否为空
        if (StringUtil.isNull(message)) {
        	resultContent.setRetData(data);
        } else {
        	resultContent.setRetData(message);	
        }
        
        long endTime = System.currentTimeMillis();
        
        // 记录日志
 		SystemLog systemLog = new SystemLog();
 		// uuid
 		systemLog.setUuid(uuid);
 		// ip地址
 		systemLog.setIpAddress(ipAddress);
 		// apiKey
 		systemLog.setApiKey(apiKey);
 		// 产品ID
 		// 从缓存中获取relation对象
 		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
 		systemLog.setProductID(relation.getString("productID"));
 		// localApiID
 		systemLog.setLocalApiID(Constants.API_ID_PA_SHIXIN);
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
		param.put("name", name);
		param.put("idCard", idCard);
		if (!StringUtil.isNull(orgName)) {
			param.put("orgName", orgName);
		}
 		systemLog.setParams(JSON.toJSONString(param));
 		// 用户ID
 		systemLog.setUserID(userID);
 		// 是否成功
 		systemLog.setIsSuccess(String.valueOf(isSuccess));
 		// 是否计费
 		systemLog.setIsCount(String.valueOf(isSuccess));
 		// 查询时间
 		systemLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FORMAT_SYSTEMLOG).format(new Date()));
 		// 查询用时
 		systemLog.setQueryTime(endTime - startTime);
 		// 返回数据
 		systemLog.setReturnData(resultObject.toJSONString().replace("\\", ""));
 		systemLogService.addLog(systemLog);
    
 		return JSONObject.toJSON(resultContent);
	}
	
	/**
	 * 凭安逾期查询
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/pa/overdue", produces={"application/json;charset=UTF-8"})
	public Object paOverdue(String requestStr, HttpServletRequest request) throws Exception{
	
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		// 获取ip地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_PA_OVERDUE, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		String phone = json.getString("phone");
		String name = json.getString("name");
		String idCard = json.getString("idCard");
		
		String orgName = json.getString("orgName");
		String imsi = json.getString("imsi");
		String imei = json.getString("imei");
		String queryDate = json.getString("queryDate");
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
		
		JSONObject resultJson = externalService.paOverdue(phone, name, idCard, orgName, imsi, imei, queryDate, userID, uuid);
     	
     	JSONObject resultObject = JSONObject.parseObject(resultJson.getString("result"));
     	
     	boolean isSuccess = resultJson.getBoolean("isSuccess");
     	
        resultContent.setRetData(resultObject);
        
        // get data
        String data = resultObject.getString("data");
        
        // get message
        String message = resultObject.getString("message");
        
        if (!isSuccess) {
        	resultContent.setCode(Constants.CODE_ERR_FAIL);
			resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
        } else {
        	// 计费
			Map<String, Object> costRetMap = costService.cost(userID, apiKey);
			String queryCount = String.valueOf(costRetMap.get("queryCount"));
			// 查询次数
			resultContent.setQueryCount(queryCount);
        }
        
        // 判断message是否为空
        if (StringUtil.isNull(message)) {
        	resultContent.setRetData(data);
        } else {
        	resultContent.setRetData(message);	
        }
        
        long endTime = System.currentTimeMillis();
        
        // 记录日志
 		SystemLog systemLog = new SystemLog();
 		// uuid
 		systemLog.setUuid(uuid);
 		// ip地址
 		systemLog.setIpAddress(ipAddress);
 		// apiKey
 		systemLog.setApiKey(apiKey);
 		// 产品ID
 		// 从缓存中获取relation对象
 		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
 		systemLog.setProductID(relation.getString("productID"));
 		// localApiID
 		systemLog.setLocalApiID(Constants.API_ID_PA_OVERDUE);
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("phone", phone);
		param.put("name", name);
		param.put("idCard", idCard);
		if (!StringUtil.isNull(orgName)) {
			param.put("orgName", orgName);
		}
		if (!StringUtil.isNull(imsi)) {
			param.put("imsi", imsi);
		}
		if (!StringUtil.isNull(imei)) {
			param.put("imei", imei);
		}
		if (!StringUtil.isNull(queryDate)) {
			param.put("queryDate", queryDate);
		}
 		systemLog.setParams(JSON.toJSONString(param));
 		// 用户ID
 		systemLog.setUserID(userID);
 		// 是否成功
 		systemLog.setIsSuccess(String.valueOf(isSuccess));
 		// 是否计费
 		systemLog.setIsCount(String.valueOf(isSuccess));
 		// 查询时间
 		systemLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FORMAT_SYSTEMLOG).format(new Date()));
 		// 查询用时
 		systemLog.setQueryTime(endTime - startTime);
 		// 返回数据
 		systemLog.setReturnData(resultObject.toJSONString().replace("\\", ""));
 		systemLogService.addLog(systemLog);
    
 		return JSONObject.toJSON(resultContent);
	}
	
	/**
	 * 凭安借贷查询
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/pa/loan", produces={"application/json;charset=UTF-8"})
	public Object paLoan(String requestStr, HttpServletRequest request) throws Exception{
	
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		// 获取ip地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_PA_LOAN, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		String phone = json.getString("phone");
		String name = json.getString("name");
		String idCard = json.getString("idCard");
		
		String orgName = json.getString("orgName");
		String imsi = json.getString("imsi");
		String imei = json.getString("imei");
		String queryDate = json.getString("queryDate");
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
		
		JSONObject resultJson = externalService.paLoan(phone, name, idCard, orgName, imsi, imei, queryDate, userID, uuid);
     	
     	JSONObject resultObject = JSONObject.parseObject(resultJson.getString("result"));
     	
     	boolean isSuccess = resultJson.getBoolean("isSuccess");
     	
        resultContent.setRetData(resultObject);
        
        // get data
        String data = resultObject.getString("data");
        
        // get message
        String message = resultObject.getString("message");
        
        if (!isSuccess) {
        	resultContent.setCode(Constants.CODE_ERR_FAIL);
			resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
        } else {
        	// 计费
			Map<String, Object> costRetMap = costService.cost(userID, apiKey);
			String queryCount = String.valueOf(costRetMap.get("queryCount"));
			// 查询次数
			resultContent.setQueryCount(queryCount);
        }
        
        // 判断message是否为空
        if (StringUtil.isNull(message)) {
        	resultContent.setRetData(data);
        } else {
        	resultContent.setRetData(message);	
        }
        
        long endTime = System.currentTimeMillis();
        
        // 记录日志
 		SystemLog systemLog = new SystemLog();
 		// uuid
 		systemLog.setUuid(uuid);
 		// ip地址
 		systemLog.setIpAddress(ipAddress);
 		// apiKey
 		systemLog.setApiKey(apiKey);
 		// 产品ID
 		// 从缓存中获取relation对象
 		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
 		systemLog.setProductID(relation.getString("productID"));
 		// localApiID
 		systemLog.setLocalApiID(Constants.API_ID_PA_LOAN);
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("phone", phone);
		param.put("name", name);
		param.put("idCard", idCard);
		if (!StringUtil.isNull(orgName)) {
			param.put("orgName", orgName);
		}
		if (!StringUtil.isNull(imsi)) {
			param.put("imsi", imsi);
		}
		if (!StringUtil.isNull(imei)) {
			param.put("imei", imei);
		}
		if (!StringUtil.isNull(queryDate)) {
			param.put("queryDate", queryDate);
		}
 		systemLog.setParams(JSON.toJSONString(param));
 		// 用户ID
 		systemLog.setUserID(userID);
 		// 是否成功
 		systemLog.setIsSuccess(String.valueOf(isSuccess));
 		// 是否计费
 		systemLog.setIsCount(String.valueOf(isSuccess));
 		// 查询时间
 		systemLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FORMAT_SYSTEMLOG).format(new Date()));
 		// 查询用时
 		systemLog.setQueryTime(endTime - startTime);
 		// 返回数据
 		systemLog.setReturnData(resultObject.toJSONString().replace("\\", ""));
 		systemLogService.addLog(systemLog);
    
 		return JSONObject.toJSON(resultContent);
	}
	
	/**
	 * 凭安黑名单查询
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/pa/blackList", produces={"application/json;charset=UTF-8"})
	public Object paBlackList(String requestStr, HttpServletRequest request) throws Exception{
	
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		// 获取ip地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_PA_BLACK_LIST, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		String phone = json.getString("phone");
		String name = json.getString("name");
		String idCard = json.getString("idCard");
		
		String orgName = json.getString("orgName");
		String imsi = json.getString("imsi");
		String imei = json.getString("imei");
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
		
		JSONObject resultJson = externalService.paBlackList(phone, name, idCard, orgName, imsi, imei, userID, uuid);
     	
     	JSONObject resultObject = JSONObject.parseObject(resultJson.getString("result"));
     	
     	boolean isSuccess = resultJson.getBoolean("isSuccess");
     	
        resultContent.setRetData(resultObject);
        
        // get data
        String data = resultObject.getString("data");
        
        // get message
        String message = resultObject.getString("message");
        
        if (!isSuccess) {
        	resultContent.setCode(Constants.CODE_ERR_FAIL);
			resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
        } else {
        	// 计费
			Map<String, Object> costRetMap = costService.cost(userID, apiKey);
			String queryCount = String.valueOf(costRetMap.get("queryCount"));
			// 查询次数
			resultContent.setQueryCount(queryCount);
        }
        
        // 判断message是否为空
        if (StringUtil.isNull(message)) {
        	resultContent.setRetData(data);
        } else {
        	resultContent.setRetData(message);	
        }
        
        long endTime = System.currentTimeMillis();
        
        // 记录日志
 		SystemLog systemLog = new SystemLog();
 		// uuid
 		systemLog.setUuid(uuid);
 		// ip地址
 		systemLog.setIpAddress(ipAddress);
 		// apiKey
 		systemLog.setApiKey(apiKey);
 		// 产品ID
 		// 从缓存中获取relation对象
 		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
 		systemLog.setProductID(relation.getString("productID"));
 		// localApiID
 		systemLog.setLocalApiID(Constants.API_ID_PA_BLACK_LIST);
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("phone", phone);
		param.put("name", name);
		param.put("idCard", idCard);
		if (!StringUtil.isNull(orgName)) {
			param.put("orgName", orgName);
		}
		if (!StringUtil.isNull(imsi)) {
			param.put("imsi", imsi);
		}
		if (!StringUtil.isNull(imei)) {
			param.put("imei", imei);
		}
 		systemLog.setParams(JSON.toJSONString(param));
 		// 用户ID
 		systemLog.setUserID(userID);
 		// 是否成功
 		systemLog.setIsSuccess(String.valueOf(isSuccess));
 		// 是否计费
 		systemLog.setIsCount(String.valueOf(isSuccess));
 		// 查询时间
 		systemLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FORMAT_SYSTEMLOG).format(new Date()));
 		// 查询用时
 		systemLog.setQueryTime(endTime - startTime);
 		// 返回数据
 		systemLog.setReturnData(resultObject.toJSONString().replace("\\", ""));
 		systemLogService.addLog(systemLog);
    
 		System.out.println(JSONObject.toJSON(resultContent));
 		
 		return JSONObject.toJSON(resultContent);
	}
	
	/**
	 * 凭安申请人属性查询
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/pa/score", produces={"application/json;charset=UTF-8"})
	public Object paPhkjModelerScore(String requestStr, HttpServletRequest request) throws Exception{
	
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		// 获取ip地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_PA_PHKJ_MODELER_SCORE, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		String phone = json.getString("phone");
		String name = json.getString("name");
		String idCard = json.getString("idCard");
		
		String orgName = json.getString("orgName");
		String imsi = json.getString("imsi");
		String imei = json.getString("imei");
		String queryDate = json.getString("queryDate");
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
		
		JSONObject resultJson = externalService.paPhkjModelerScore(phone, name, idCard, orgName, imsi, imei, queryDate, userID, uuid);
     	
     	JSONObject resultObject = JSONObject.parseObject(resultJson.getString("result"));
     	
     	boolean isSuccess = resultJson.getBoolean("isSuccess");
     	
        resultContent.setRetData(resultObject);
        
        // get data
        String data = resultObject.getString("data");
        
        // get message
        String message = resultObject.getString("message");
        
        if (!isSuccess) {
        	resultContent.setCode(Constants.CODE_ERR_FAIL);
			resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
        } else {
        	// 计费
			Map<String, Object> costRetMap = costService.cost(userID, apiKey);
			String queryCount = String.valueOf(costRetMap.get("queryCount"));
			// 查询次数
			resultContent.setQueryCount(queryCount);
        }
        
        // 判断message是否为空
        if (StringUtil.isNull(message)) {
        	resultContent.setRetData(data);
        } else {
        	resultContent.setRetData(message);	
        }
        
        long endTime = System.currentTimeMillis();
        
        // 记录日志
 		SystemLog systemLog = new SystemLog();
 		// uuid
 		systemLog.setUuid(uuid);
 		// ip地址
 		systemLog.setIpAddress(ipAddress);
 		// apiKey
 		systemLog.setApiKey(apiKey);
 		// 产品ID
 		// 从缓存中获取relation对象
 		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
 		systemLog.setProductID(relation.getString("productID"));
 		// localApiID
 		systemLog.setLocalApiID(Constants.API_ID_PA_PHKJ_MODELER_SCORE);
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("phone", phone);
		param.put("name", name);
		param.put("idCard", idCard);
		if (!StringUtil.isNull(orgName)) {
			param.put("orgName", orgName);
		}
		if (!StringUtil.isNull(imsi)) {
			param.put("imsi", imsi);
		}
		if (!StringUtil.isNull(imei)) {
			param.put("imei", imei);
		}
		if (!StringUtil.isNull(queryDate)) {
			param.put("queryDate", queryDate);
		}
 		systemLog.setParams(JSON.toJSONString(param));
 		// 用户ID
 		systemLog.setUserID(userID);
 		// 是否成功
 		systemLog.setIsSuccess(String.valueOf(isSuccess));
 		// 是否计费
 		systemLog.setIsCount(String.valueOf(isSuccess));
 		// 查询时间
 		systemLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FORMAT_SYSTEMLOG).format(new Date()));
 		// 查询用时
 		systemLog.setQueryTime(endTime - startTime);
 		// 返回数据
 		systemLog.setReturnData(resultObject.toJSONString().replace("\\", ""));
 		systemLogService.addLog(systemLog);
    
 		return JSONObject.toJSON(resultContent);
	}
	
	/**
	 * 凭安综合查询
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/pa/synthesize", produces={"application/json;charset=UTF-8"})
	public Object paZh(String requestStr, HttpServletRequest request) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		// 获取ip地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_PA_ZH, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		String phone = json.getString("phone");
		String name = json.getString("name");
		String idCard = json.getString("idCard");
		
		String orgName = json.getString("orgName");
		String imsi = json.getString("imsi");
		String imei = json.getString("imei");
		String queryDate = json.getString("queryDate");
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
		
		JSONObject resultJson = externalService.paZh(phone, name, idCard, orgName, imsi, imei, queryDate, userID, uuid);
     	
     	JSONObject resultObject = resultJson.getJSONObject("result");
     	
     	boolean isSuccess = resultJson.getBoolean("isSuccess");
     	
        resultContent.setRetData(resultObject);
        
        if (!isSuccess) {
        	resultContent.setCode(Constants.CODE_ERR_FAIL);
			resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
        } else {
        	// 计费
			Map<String, Object> costRetMap = costService.cost(userID, apiKey);
			String queryCount = String.valueOf(costRetMap.get("queryCount"));
			// 查询次数
			resultContent.setQueryCount(queryCount);
        }
        
        long endTime = System.currentTimeMillis();
        
        // 记录日志
 		SystemLog systemLog = new SystemLog();
 		// uuid
 		systemLog.setUuid(uuid);
 		// ip地址
 		systemLog.setIpAddress(ipAddress);
 		// apiKey
 		systemLog.setApiKey(apiKey);
 		// 产品ID
 		// 从缓存中获取relation对象
 		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
 		systemLog.setProductID(relation.getString("productID"));
 		// localApiID
 		systemLog.setLocalApiID(Constants.API_ID_PA_ZH);
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("phone", phone);
		param.put("name", name);
		param.put("idCard", idCard);
		if (!StringUtil.isNull(orgName)) {
			param.put("orgName", orgName);
		}
		if (!StringUtil.isNull(imsi)) {
			param.put("imsi", imsi);
		}
		if (!StringUtil.isNull(imei)) {
			param.put("imei", imei);
		}
		if (!StringUtil.isNull(queryDate)) {
			param.put("queryDate", queryDate);
		}
 		systemLog.setParams(JSON.toJSONString(param));
 		// 用户ID
 		systemLog.setUserID(userID);
 		// 是否成功
 		systemLog.setIsSuccess(String.valueOf(isSuccess));
 		// 是否计费
 		systemLog.setIsCount(String.valueOf(isSuccess));
 		// 查询时间
 		systemLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FORMAT_SYSTEMLOG).format(new Date()));
 		// 查询用时
 		systemLog.setQueryTime(endTime - startTime);
 		// 返回数据
 		systemLog.setReturnData(resultObject.toJSONString().replace("\\", ""));
 		systemLogService.addLog(systemLog);
 		
 		return JSONObject.toJSON(resultContent);
	}
}
