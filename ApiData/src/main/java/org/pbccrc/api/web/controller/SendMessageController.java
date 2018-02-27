package org.pbccrc.api.web.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;

import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.base.service.CostService;
import org.pbccrc.api.base.service.SendMessageService;
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

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/sendMessage")
public class SendMessageController {
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private SendMessageService sendMessageService;
	
	@Autowired
	private CostService costService;

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
	@RequestMapping(value="/send", produces={"application/json;charset=UTF-8"})
	public JSONObject getResult(String requestStr, HttpServletRequest request) throws Exception{
		
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
		if (!validator.validateRequest(userID, apiKey, Constants.API_ID_SEND_MESSAGE, ipAddress, resultContent)) {
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
		
		// 验证telNos是否为空
		String telNos = json.getString("telNos");
		if (StringUtil.isNull(telNos)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "telNos");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证msgContent是否为空
		String msgContent = json.getString("msgContent");
		if (StringUtil.isNull(msgContent)) {
			resultContent.setCode(Constants.ERR_URL_INVALID);
			resultContent.setRetMsg(Constants.RET_MSG_URL_INVALID + "msgContent");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		String trxNo = json.getString("trxNo");
		String sign = json.getString("sign");
		String type = json.getString("type");
		// 验证type是否正确
		String className = String.valueOf(RedisClient.get("sendMsgRef_" + type));
		if (StringUtil.isNull(className)) {
			resultContent.setCode(Constants.CODE_ERR_SEND_MESSAGE_ERR);
			resultContent.setRetMsg(Constants.CODE_ERR_SEND_MESSAGE_ERR_MSG);
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 验证telNos格式是否正确
		String[] telNoArray = telNos.split(Constants.COMMA);
		for (String telNoStr : telNoArray) {
			if (!Constants.BLANK.equals(validator.validateMobile(telNoStr))) {
				resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
				resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
				return (JSONObject)JSONObject.toJSON(resultContent);
			}
		}
		
		// 生成UUID
		String uuid = StringUtil.createUUID();
		
		Map<String, Object> returnMap = sendMessageService.sendMessage(telNos, msgContent, type, sign, trxNo, uuid, userID);
		
		String retMsg = Constants.CODE_ERR_SEND_MESSAGE_SUCCESS;
		String code = Constants.CODE_ERR_SEND_MESSAGE_SUCCESS_MSG;
		
		boolean isSuccess = (boolean) returnMap.get("isSuccess");
		
		if (!isSuccess) {
			retMsg = Constants.CODE_ERR_SEND_MESSAGE_FAIL;
			code = Constants.CODE_ERR_SEND_MESSAGE_FAIL_MSG;
		}
		
		JSONObject returnJson = new JSONObject();
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
		systemLog.setLocalApiID(Constants.API_ID_SEND_MESSAGE);
		// 参数
		JSONObject paramJson = new JSONObject();
		paramJson.put("telNos", telNos);
		paramJson.put("msgContent", msgContent);
		paramJson.put("trxNo", trxNo);
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
		systemLog.setNote1(Constants.BLANK);
		// 返回数据 TODO 返回什么?
		// systemLog.setReturnData();
		systemLogService.addLog(systemLog);
		
		return returnJson;
	}
}
