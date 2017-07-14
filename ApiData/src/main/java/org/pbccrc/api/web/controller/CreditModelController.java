package org.pbccrc.api.web.controller;

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
import org.pbccrc.api.base.service.CreditModelService;
import org.pbccrc.api.base.service.SystemLogService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.base.util.SystemUtil;
import org.pbccrc.api.base.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
