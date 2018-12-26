package org.pbccrc.api.web.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;

import org.pbccrc.api.base.bean.LocalApi;
import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.base.service.ApiProductService;
import org.pbccrc.api.base.service.CostService;
import org.pbccrc.api.base.service.LocalApiService;
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

/**
 * API产品Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/product")
public class ApiProductController {
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private CostService costService;
	
	@Autowired
	private ApiProductService apiProductService;
	
	@Autowired
	private LocalApiService localApiService;
	
	/**
	 * bhyh
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/bhyh", produces={"application/json;charset=UTF-8"})
	public Object bhyh(String requestStr, HttpServletRequest request) throws Exception {
		
		
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
		
		String idCard = json.getString("idCard");
		String name = json.getString("name");
		
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("name", name);
		urlParams.put("idCard", idCard);
		
		// 获取本地api
		LocalApi localApi = localApiService.queryByService(Constants.API_SERVICE_PRODUCT_BHYH);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, localApi, urlParams, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
		
		JSONObject resultJson = apiProductService.bhyh(name, idCard, userID, uuid, localApi);
     	
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
 		systemLog.setLocalApiID(String.valueOf(localApi.getId()));
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("name", name);
 		param.put("idCard", idCard);
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
	 * 新反欺诈服务
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/fraud2", produces={"application/json;charset=UTF-8"})
	public Object fraud2(String requestStr, HttpServletRequest request) throws Exception {
		
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
		String idCard = json.getString("idCard");
		String name = json.getString("name");
		
		// 记录日志用参数
		String logIdCard = Constants.BLANK;
		String logName = Constants.BLANK;
		
		if (StringUtil.isNull((idCard))) {
			idCard = "152201196504049370";
		} else {
			logIdCard = idCard;
		}
		
		if (StringUtil.isNull(name)) {
			name = "钢铁侠";
		} else {
			logName = name;
		}
		
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("name", name);
		urlParams.put("idCard", idCard);
		urlParams.put("phone", phone);
		
		// 获取本地api
		LocalApi localApi = localApiService.queryByService(Constants.API_SERVICE_PRODUCT_ANTI_NEW_FRAUD);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, localApi, urlParams, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
		
		JSONObject resultJson = apiProductService.fraud2(phone, name, idCard, userID, uuid, localApi);
     	
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
 		systemLog.setLocalApiID(String.valueOf(localApi.getId()));
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("phone", phone);
 		param.put("name", logName);
 		param.put("idCard", logIdCard);
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
	 * 反欺诈服务
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/fraud", produces={"application/json;charset=UTF-8"})
	public Object fraud(String requestStr, HttpServletRequest request) throws Exception {
		
		
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
		
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("name", name);
		urlParams.put("idCard", idCard);
		urlParams.put("phone", phone);
		
		// 获取本地api
		LocalApi localApi = localApiService.queryByService(Constants.API_SERVICE_PRODUCT_ANTI_FRAUD);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, localApi, urlParams, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
		
		JSONObject resultJson = apiProductService.fraud(phone, name, idCard, userID, uuid, localApi);
     	
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
 		systemLog.setLocalApiID(String.valueOf(localApi.getId()));
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("phone", phone);
		param.put("name", name);
		param.put("idCard", idCard);
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
	 * step1-获取手机服务密码/动态码
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/rcarc_getDynamicCode", produces={"application/json;charset=UTF-8"})
	public Object getDynamicCode(String requestStr, HttpServletRequest request) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		// 获取ip地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
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
		
		JSONObject resultObject = apiProductService.getDynamicCode(phone, name, idCard);
		
		resultContent.setRetData(resultObject);
		
		long endTime = System.currentTimeMillis();
        
        // 记录日志
 		SystemLog systemLog = new SystemLog();
 		// uuid
 		systemLog.setUuid(Constants.BLANK);
 		// ip地址
 		systemLog.setIpAddress(ipAddress);
 		// apiKey
 		systemLog.setApiKey(apiKey);
 		// 产品ID
 		// 从缓存中获取relation对象
 		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
 		systemLog.setProductID(relation.getString("productID"));
 		// localApiID
 		systemLog.setLocalApiID(Constants.API_ID_PRODUCT_REDIT_CARD_APPLY_RISK_CONTROL_GET_CODE);
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("phone", phone);
		param.put("name", name);
		param.put("idCard", idCard);
 		systemLog.setParams(JSON.toJSONString(param));
 		// 用户ID
 		systemLog.setUserID(userID);
 		// 是否成功
 		systemLog.setIsSuccess("true");
 		// 是否计费
 		systemLog.setIsCount("false");
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
	 * step2-重置密码
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/rcarc_resetPassword", produces={"application/json;charset=UTF-8"})
	public Object resetPassword(String requestStr, HttpServletRequest request) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		// 获取ip地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		String token = json.getString("token");
		String password = json.getString("password");
		String captcha = json.getString("captcha");
		
		JSONObject resultObject = apiProductService.resetPassword(token, password, captcha);
		
		resultContent.setRetData(resultObject);
		
		long endTime = System.currentTimeMillis();
        
        // 记录日志
 		SystemLog systemLog = new SystemLog();
 		// uuid
 		systemLog.setUuid(Constants.BLANK);
 		// ip地址
 		systemLog.setIpAddress(ipAddress);
 		// apiKey
 		systemLog.setApiKey(apiKey);
 		// 产品ID
 		// 从缓存中获取relation对象
 		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
 		systemLog.setProductID(relation.getString("productID"));
 		// localApiID
 		systemLog.setLocalApiID(Constants.API_ID_PRODUCT_REDIT_CARD_APPLY_RISK_CONTROL_RESET_PASSWORD);
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("token", token);
		param.put("password", password);
		param.put("captcha", captcha);
 		systemLog.setParams(JSON.toJSONString(param));
 		// 用户ID
 		systemLog.setUserID(userID);
 		// 是否成功
 		systemLog.setIsSuccess(resultObject.getString("isSuccess"));
 		// 是否计费
 		systemLog.setIsCount("false");
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
	 * step3-信用卡申请风控
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/rcarc", produces={"application/json;charset=UTF-8"})
	public Object reditCardApplyRiskControl(String requestStr, HttpServletRequest request) throws Exception {
		
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
		String phonePassword = json.getString("phonePassword");
		
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("name", name);
		urlParams.put("idCard", idCard);
		urlParams.put("phone", phone);
		urlParams.put("phonePassword", phonePassword);
		
		// 获取本地api
		LocalApi localApi = localApiService.queryByService(Constants.API_SERVICE_PRODUCT_REDIT_CARD_APPLY_RISK_CONTROL);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, localApi, urlParams, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
     	
     	JSONObject resultJson = apiProductService.reditCardApplyRiskControl(phone, phonePassword, name, idCard, userID, uuid, localApi);
     	
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
 		systemLog.setLocalApiID(String.valueOf(localApi.getId()));
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("phone", phone);
		param.put("name", name);
		param.put("idCard", idCard);
		param.put("phonePasswrod", phonePassword);
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
	 * 一致性验证(身份证与电话号码 )
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/checkConsistency", produces={"application/json;charset=UTF-8"})
	public Object checkConsistency(String requestStr, HttpServletRequest request) throws Exception {
		
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
		String idCard = json.getString("idCard");
		
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("phone", phone);
		urlParams.put("idCard", idCard);
		
		// 获取本地api
		LocalApi localApi = localApiService.queryByService(Constants.API_SERVICE_PRODUCT_CHECK_CONSISTENCY);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, localApi, urlParams, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
     	
     	JSONObject resultJson = apiProductService.checkConsistency(phone, idCard, userID, uuid, localApi);
     	
     	String resultObject = resultJson.getString("result");
     	
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
 		systemLog.setLocalApiID(String.valueOf(localApi.getId()));
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("phone", phone);
		param.put("idCard", idCard);
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
// 		systemLog.setReturnData(resultObject.toJSONString().replace("\\", ""));
 		systemLog.setReturnData(resultObject);
 		systemLogService.addLog(systemLog);
		
		return JSONObject.toJSON(resultContent);
	}
	
	/**
	 * 空号检测
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/batch-check", produces={"application/json;charset=UTF-8"})
	public Object batchCheckMobile(String requestStr, HttpServletRequest request) throws Exception {
		
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
		
		// 该api不需要base64
//		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		String mobiles = json.getString("mobiles");
		
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("mobiles", mobiles);
		
		// 获取本地api
		LocalApi localApi = localApiService.queryByService(Constants.API_SERVICE_PRODUCT_BATCH_CHECK_MOBILE);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, localApi, urlParams, ipAddress, resultContent)) {
			return ((JSONObject)JSONObject.toJSON(resultContent)).toJSONString();
		}
		
		// 生成UUID
     	String uuid = StringUtil.createUUID();
     	
     	JSONObject resultJson = apiProductService.batchCheckMobile(mobiles, userID, uuid, localApi);
     	
     	String resultObject = resultJson.getString("result");
     	
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
 		systemLog.setLocalApiID(String.valueOf(localApi.getId()));
 		// 参数
 		Map<String, String> param = new HashMap<String, String>();
 		param.put("mobiles", mobiles);
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
 		systemLog.setReturnData(resultObject);
 		systemLogService.addLog(systemLog);
		
		return JSONObject.toJSON(resultContent);
	}
}
