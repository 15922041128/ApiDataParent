package org.pbccrc.api.web.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/vip/webapp/queryBlackList", produces={"application/json;charset=UTF-8"})
	public String vipQueryBlackList(String requestStr, HttpServletRequest request) throws Exception{
		
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
    
		return ((JSONObject) JSONObject.toJSON(resultContent)).toJSONString().replace("\\", "");
		
	}
}
