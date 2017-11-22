package org.pbccrc.api.web.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;

import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.base.bean.SmsCondition;
import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.base.service.BorrowDetailService;
import org.pbccrc.api.base.service.CostService;
import org.pbccrc.api.base.service.CreditModelService;
import org.pbccrc.api.base.service.MarketingService;
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
 * 信贷模型
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/marketing")
public class MarketingController {
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private CreditModelService creditModelService;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private MarketingService marketingService;
	
	@Autowired
	private CostService costService;
	
	@Autowired
	private BorrowDetailService borrowDetailService;
	
	/** 
	 * 筛选营销对象
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getMarketeeCount", produces={"application/json;charset=UTF-8"})
	public JSONObject getMarketeeCount(String requestStr, HttpServletRequest request) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		// 获取IP地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_GET_MARKETEE_COUNT, ipAddress, resultContent)) {
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		SmsCondition smsCondition = null;
		// 验证json格式
		try {
			smsCondition = JSONObject.parseObject(requestStr, SmsCondition.class);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证productType是否为空
		String productType = smsCondition.getProductType();
		if (StringUtil.isNull(productType)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "productType");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证年龄是否符合要求
		Integer age_max = smsCondition.getAge_max();
		Integer age_min = smsCondition.getAge_min();
		if (age_max != null && age_min != null && age_max < age_min) {
			resultContent.setCode(Constants.CODE_ERR_REQ_PARAM);
			resultContent.setRetMsg(Constants.CODE_ERR_REQ_PARAM_MSG + "age_max应大于等于age_min");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		String province = smsCondition.getProvince();
		String city = smsCondition.getCity();
		String operator = smsCondition.getOperator();
		if (!StringUtil.isNull(province)) {
			smsCondition.setProvinces(province.split(","));
		}
		if (!StringUtil.isNull(city)) {
			smsCondition.setCitys(city.split(","));
		}
		if (!StringUtil.isNull(operator)) {
			smsCondition.setOperators(operator.split(","));
		}
		
		JSONObject returnJson = marketingService.getMarketeeCount(smsCondition);
		
		String retMsg = Constants.CODE_ERR_SUCCESS_MSG;
		String code = Constants.CODE_ERR_SUCCESS;
		
		JSONObject retData = (JSONObject) returnJson.get("retData");
		int marketeeNum = retData.getIntValue("marketeeNum");
		
		boolean isSuccess = true;
		
		returnJson.put("retMsg", retMsg);
		returnJson.put("code", code);
		
		if (isSuccess) {
			costService.cost(userID, apiKey);
		}
		
		long endTime = System.currentTimeMillis();
		
		// 记录日志
		SystemLog systemLog = new SystemLog();
		// 该APIuuid为borrow.seq
		systemLog.setUuid(returnJson.getString("seq"));
		returnJson.remove("seq");
		// ip地址
		systemLog.setIpAddress(ipAddress);
		// apiKey
		systemLog.setApiKey(apiKey);
		// 产品ID
		// 从缓存中获取relation对象
		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
		systemLog.setProductID(relation.getString("productID"));
		// localApiID
		systemLog.setLocalApiID(Constants.API_ID_GET_MARKETEE_COUNT);
		// 参数
		JSONObject paramJson = new JSONObject();
		paramJson.put("seq", returnJson.get("seq"));
		returnJson.remove("seq");
		systemLog.setParams(paramJson.toJSONString());
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
		systemLog.setReturnData(String.valueOf(marketeeNum));
		systemLogService.addLog(systemLog);
		
		return returnJson;
	}
	
	/** 
	 * 发送营销信息
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/sendMesg", produces={"application/json;charset=UTF-8"})
	public JSONObject sendMesg(String requestStr, HttpServletRequest request) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SEND_MESSAGE_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SEND_MESSAGE_SUCCESS_MSG);
		
		// 获取IP地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_SEND_MESG, ipAddress, resultContent)) {
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		SmsCondition smsCondition = null;
		// 验证json格式
		try {
			smsCondition = JSONObject.parseObject(requestStr, SmsCondition.class);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证productType是否为空
		String productType = smsCondition.getProductType();
		if (StringUtil.isNull(productType)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "productType");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证content是否为空
		String content = smsCondition.getContent();
		if (StringUtil.isNull(content)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "content");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证sendNum是否为空
		Integer sendNum = smsCondition.getSendNum();
		if (sendNum == null ) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "sendNum");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证年龄是否符合要求
		Integer age_max = smsCondition.getAge_max();
		Integer age_min = smsCondition.getAge_min();
		if (age_max != null && age_min != null && age_max < age_min) {
			resultContent.setCode(Constants.CODE_ERR_REQ_PARAM);
			resultContent.setRetMsg(Constants.CODE_ERR_REQ_PARAM_MSG + "age_max应大于等于age_min");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		String province = smsCondition.getProvince();
		String city = smsCondition.getCity();
		String operator = smsCondition.getOperator();
		if (!StringUtil.isNull(province)) {
			smsCondition.setProvinces(province.split(","));
		}
		if (!StringUtil.isNull(city)) {
			smsCondition.setCitys(city.split(","));
		}
		if (!StringUtil.isNull(operator)) {
			smsCondition.setOperators(operator.split(","));
		}
		
		JSONObject returnJson = marketingService.sendMesg(smsCondition);
		
		String retMsg = Constants.CODE_ERR_SEND_MESSAGE_SUCCESS_MSG;
		String code = Constants.CODE_ERR_SEND_MESSAGE_SUCCESS;
		
		boolean isSuccess = true;
		
		returnJson.put("retMsg", retMsg);
		returnJson.put("code", code);
		
		if (isSuccess) {
			costService.cost(userID, apiKey);
		}
		
		long endTime = System.currentTimeMillis();
		
		// 记录日志
		SystemLog systemLog = new SystemLog();
		// 该APIuuid为seq
		systemLog.setUuid(returnJson.getString("seq"));
		// ip地址
		systemLog.setIpAddress(ipAddress);
		// apiKey
		systemLog.setApiKey(apiKey);
		// 产品ID
		// 从缓存中获取relation对象
		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
		systemLog.setProductID(relation.getString("productID"));
		// localApiID
		systemLog.setLocalApiID(Constants.API_ID_SEND_MESG);
		// 参数
		systemLog.setParams(JSON.toJSONString(smsCondition));
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
		systemLog.setNote1(Constants.BLANK);
		// 返回数据
		systemLogService.addLog(systemLog);
		
		return returnJson;
	}
}
