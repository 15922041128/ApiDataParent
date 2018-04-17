package org.pbccrc.api.web.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.base.service.BorrowDetailService;
import org.pbccrc.api.base.service.BorrowService;
import org.pbccrc.api.base.service.CostService;
import org.pbccrc.api.base.service.CreditModelService;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 信贷模型
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/creditModel")
public class CreditModelController {
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private CreditModelService creditModelService;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private BorrowService borrowService;
	
	@Autowired
	private CostService costService;
	
	@Autowired
	private BorrowDetailService borrowDetailService;
	
	/** 
	 * 非现金贷
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getResult", produces={"application/json;charset=UTF-8"})
	public JSONObject getResult(String requestStr, HttpServletRequest request) throws Exception{
		
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
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_YINGZE_SCORE, ipAddress, resultContent)) {
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证realName是否为空
		String realName = json.getString("realName");
		if (StringUtil.isNull(realName)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "realName");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证idCard是否为空
		String idCard = json.getString("idCard");
		if (StringUtil.isNull(idCard)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "idCard");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证loanInfos是否为空
		String loanInfos = json.getString("loanInfos");
		if (StringUtil.isNull(loanInfos)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "loanInfos");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		String trxNo = json.getString("trxNo");
		
		// 验证loanInfos格式是否正确
		try {
			JSONArray.parseArray(loanInfos);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		
		JSONObject returnJson = borrowService.getResult(realName, idCard, trxNo, loanInfos);
		
		String retMsg = Constants.CODE_ERR_SUCCESS_MSG;
		String code = Constants.CODE_ERR_SUCCESS;
		
		JSONObject retData = (JSONObject) returnJson.get("retData");
		int score = retData.getIntValue("score");
		
		boolean isSuccess = false;
		
		if (score <= 0) {
			retMsg = Constants.CODE_ERR_FAIL_MSG;
			code = Constants.CODE_ERR_FAIL;
		} else {
			isSuccess = true;
		}
		
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
		systemLog.setLocalApiID(Constants.API_ID_YINGZE_SCORE);
		// 参数
		JSONObject paramJson = new JSONObject();
		paramJson.put("realName", realName);
		paramJson.put("idCard", idCard);
		paramJson.put("trxNo", trxNo);
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
		if (score == -90) {
			systemLog.setReturnData(returnJson.getString("errorMessage"));
			retData.put("score", Constants.BLANK);
			returnJson.put("retData", retData);
		} else {
			systemLog.setReturnData(String.valueOf(score));
		}
		systemLogService.addLog(systemLog);
		
		returnJson.remove("errorMessage");
		return returnJson;
	}
	
	/**
	 * 现金贷
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getResultTri", produces={"application/json;charset=UTF-8"})
	public JSONObject getResultTri(String requestStr, HttpServletRequest request) throws Exception{
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
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_YINGZE_SCORE_TRI, ipAddress, resultContent)) {
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证realName是否为空
		String realName = json.getString("realName");
		if (StringUtil.isNull(realName)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "realName");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证idCard是否为空
		String idCard = json.getString("idCard");
		if (StringUtil.isNull(idCard)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "idCard");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证loanInfos是否为空
		String loanInfos = json.getString("loanInfos");
		if (StringUtil.isNull(loanInfos)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "loanInfos");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		String trxNo = json.getString("trxNo");
		
		// 验证loanInfos格式是否正确
		try {
			JSONArray.parseArray(loanInfos);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		
		JSONObject returnJson = borrowService.getResult(realName, idCard, trxNo, loanInfos);
		
		String retMsg = Constants.CODE_ERR_SUCCESS_MSG;
		String code = Constants.CODE_ERR_SUCCESS;
		
		JSONObject retData = (JSONObject) returnJson.get("retData");
		int score = retData.getIntValue("score");
		
		boolean isSuccess = false;
		
		if (score <= 0) {
			retMsg = Constants.CODE_ERR_FAIL_MSG;
			code = Constants.CODE_ERR_FAIL;
		} else {
			isSuccess = true;
		}
		
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
		systemLog.setLocalApiID(Constants.API_ID_YINGZE_SCORE_TRI);
		// 参数
		JSONObject paramJson = new JSONObject();
		paramJson.put("realName", realName);
		paramJson.put("idCard", idCard);
		paramJson.put("trxNo", trxNo);
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
		if (score == -90) {
			systemLog.setReturnData(returnJson.getString("errorMessage"));
			retData.put("score", Constants.BLANK);
			returnJson.put("retData", retData);
		} else {
			systemLog.setReturnData(String.valueOf(score));
		}
		systemLogService.addLog(systemLog);
		
		returnJson.remove("errorMessage");
		return returnJson;
	}
	
	/**
	 * 
	 * @param dataCnt		记录条数
	 * @param amount		额度
	 * @param amountFlag	额度之下或之上(1以下,2以上,默认3000以下)
	 * @param status		状态(好，坏，灰)
	 * @param count			借贷次数 (默认为1)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/queryResult", produces={"application/json;charset=UTF-8"})
	public JSONObject queryResult(String dataCnt, String amount, String amountFlag, String status, String count, HttpServletRequest request) throws Exception{
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		// 获取IP地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		if (StringUtil.isNull(dataCnt)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "dataCnt");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		if (StringUtil.isNull(status)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "status");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		if (!StringUtil.isNull(amount)) {
			// 当额度不为空时,额度标记不允许为空
			if (StringUtil.isNull(amountFlag)) {
				resultContent.setCode(Constants.CODE_ERR_REQ_PARAM);
				resultContent.setRetMsg(Constants.CODE_ERR_REQ_PARAM_MSG + " : 当额度不为空时,额度标记不允许为空");
				return (JSONObject)JSONObject.toJSON(resultContent);
			}
		}
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_YINGZE_DATA_QUERY, ipAddress, resultContent)) {
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		JSONObject returnJson = borrowService.queryResult(dataCnt, amount, amountFlag, status, count);
		
		// TODO
		
		return null;
	}

	// 暂时没用
	@SuppressWarnings("unchecked")
	@GET
	@ResponseBody
	@RequestMapping(value="/get",produces={"application/json;charset=UTF-8"})
	public String auth(
			@QueryParam("name") String name, 
			@QueryParam("idCardNo") String idCardNo, 
			@QueryParam("accountNo") String accountNo, 
			@QueryParam("mobile") String mobile,
			@Context HttpServletRequest request) throws Exception {
		
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
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_CREDIT_MODEL, ipAddress, resultContent)) {
			return JSONObject.toJSONString(resultContent);
		}
		
		Map<String, Object> map = creditModelService.creditModel(userID, name, idCardNo, accountNo, mobile);
		Map<String, Object> gradeMap = (Map<String, Object>) map.get("gradeMap");
		if (null != gradeMap && gradeMap.size() > 0) {
			// 删除ID、用户ID和评级
			gradeMap.remove("id");
			gradeMap.remove("userID");
			gradeMap.remove("grade");
		} else {
			gradeMap = new HashMap<String, Object>();
		}
		// 增加查询条件 
		gradeMap.put("name", name);
		gradeMap.put("idCardNo", idCardNo);
		gradeMap.put("accountNo", accountNo);
		gradeMap.put("mobile", mobile);
		// 是否通过
		gradeMap.put("isPass", map.get("isPass"));
		resultContent.setRetData(gradeMap);
		
		// 记录日志
		SystemLog systemLog = new SystemLog();
		// uuid
		systemLog.setUuid(StringUtil.createUUID());
		// ip地址
		systemLog.setIpAddress(ipAddress);
		// apiKey
		systemLog.setApiKey(apiKey);
		// 产品ID
		// 从缓存中获取relation对象
		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
		systemLog.setProductID(relation.getString("productID"));
		// localApiID
		systemLog.setLocalApiID(Constants.API_ID_CREDIT_MODEL);
		// 参数
		JSONObject params = new JSONObject();
		params.put("name", name);
		params.put("idCardNo", idCardNo);
		params.put("accountNo", accountNo);
		params.put("mobile", mobile);
		systemLog.setParams(params.toJSONString());
		// 用户ID
		systemLog.setUserID(userID);
		// 是否成功
		systemLog.setIsSuccess(String.valueOf(map.get("isSuccess")));
		// 是否计费
		systemLog.setIsCount(String.valueOf(map.get("isSuccess")));
		// 查询时间
		systemLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FORMAT_SYSTEMLOG).format(new Date()));
		systemLogService.addLog(systemLog);
		
		return JSONObject.toJSONString(resultContent);
	}
	
	
	/** 
	 * 非现金贷(返回参数)
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getResultParam", produces={"application/json;charset=UTF-8"})
	public JSONObject getResultParam(String requestStr, HttpServletRequest request) throws Exception{
		
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
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_YINGZE_SCORE_PARAM, ipAddress, resultContent)) {
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证realName是否为空
		String realName = json.getString("realName");
		if (StringUtil.isNull(realName)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "realName");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证idCard是否为空
		String idCard = json.getString("idCard");
		if (StringUtil.isNull(idCard)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "idCard");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证loanInfos是否为空
		String loanInfos = json.getString("loanInfos");
		if (StringUtil.isNull(loanInfos)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "loanInfos");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		// 验证json格式
		try {
			JSONArray loanInfoArray = JSONArray.parseArray(loanInfos);
			if (loanInfoArray.size() == 0) {
				resultContent.setCode(Constants.ERR_URL_INVALID);
				resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "loanInfos");
				return (JSONObject)JSONObject.toJSON(resultContent);
			}
		} catch (Exception e) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "loanInfos");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		String trxNo = json.getString("trxNo");
		
		// 验证loanInfos格式是否正确
		try {
			JSONArray.parseArray(loanInfos);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		JSONObject returnJson = borrowService.getResultParam(realName, idCard, trxNo, loanInfos);
		
		String retMsg = Constants.CODE_ERR_SUCCESS_MSG;
		String code = Constants.CODE_ERR_SUCCESS;
		
		JSONObject retData = (JSONObject) returnJson.get("retData");
		int returnState = retData.getIntValue("returnState");
		
		boolean isSuccess = false;
		
		if (returnState < 0) {
			retMsg = Constants.CODE_ERR_FAIL_MSG;
			code = Constants.CODE_ERR_FAIL;
		} else {
			isSuccess = true;
		}
		
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
		systemLog.setLocalApiID(Constants.API_ID_YINGZE_SCORE_PARAM);
		// 参数
		JSONObject paramJson = new JSONObject();
		paramJson.put("realName", realName);
		paramJson.put("idCard", idCard);
		paramJson.put("trxNo", trxNo);
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
		if (returnState == -90) {
			systemLog.setReturnData(returnJson.getString("errorMessage"));
		} else {
			systemLog.setReturnData(retData.toJSONString());
		}
		systemLogService.addLog(systemLog);
		
		returnJson.remove("errorMessage");
		return returnJson;
	}
	
	/** 
	 * 白名单(955钱包)
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getWhiteList", produces={"application/json;charset=UTF-8"})
	public JSONObject getWhiteList(String requestStr, HttpServletRequest request) throws Exception{
		
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
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_GET_WHITE_LIST, ipAddress, resultContent)) {
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证realName是否为空
		String realName = json.getString("realName");
		if (StringUtil.isNull(realName)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "realName");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证idCard是否为空
		String idCard = json.getString("idCard");
		if (StringUtil.isNull(idCard)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "idCard");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 生成UUID
		String uuid = StringUtil.createUUID();
		
		JSONObject returnJson = borrowDetailService.getBorrowDetail(realName, idCard, userID, uuid);
		
		String retMsg = Constants.CODE_ERR_SUCCESS_MSG;
		String code = Constants.CODE_ERR_SUCCESS;
		
		boolean isSuccess = returnJson.getBooleanValue("isSuccess");
		returnJson.remove("isSuccess");
		
		if (!isSuccess) {
			retMsg = Constants.CODE_ERR_FAIL_MSG;
			code = Constants.CODE_ERR_FAIL;
		}
		
		returnJson.put("retMsg", retMsg);
		returnJson.put("code", code);
		
		if (isSuccess) {
			costService.cost(userID, apiKey);
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
		systemLog.setLocalApiID(Constants.API_ID_GET_WHITE_LIST);
		// 参数
		JSONObject paramJson = new JSONObject();
		paramJson.put("realName", realName);
		paramJson.put("idCard", idCard);
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
		systemLog.setReturnData(returnJson.getString("retData"));
		systemLogService.addLog(systemLog);
		
		return returnJson;
	}
}
