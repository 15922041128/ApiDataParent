package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.service.SendMessageCoreService;
import org.pbccrc.api.base.service.SendMessageService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.core.dao.ApiLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class SendMessageServiceImpl implements SendMessageService{

	private SendMessageCoreService sendMessageCoreService;
	
	@Autowired
	private ApiLogDao apiLogDao;

	@Override
	public Map<String, Object> sendMessage(String telNos, String msgContent, String type, String sign, String trxNo, String uuid, String userID) throws Exception {
		
		JSONObject object = JSONObject.parseObject(String.valueOf(RedisClient.get("sendMsgRef_" + type)));
		
		String className = object.getString("className");
		
		sendMessageCoreService = (SendMessageCoreService) Class.forName(className).newInstance();
		
		Map<String, Object> returnMap = sendMessageCoreService.sendMessage(telNos, msgContent, sign);
		
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userID);
		apiLog.setLocalApiID(Constants.API_ID_SEND_MESSAGE);
		// 参数
		JSONObject params = new JSONObject();
		params.put("telNos", telNos);
		params.put("msgContent", msgContent);
		params.put("type", type);
		params.put("sign", sign);
		params.put("trxNo", trxNo);
		apiLog.setParams(params.toJSONString());
		apiLog.setDataFrom(Constants.DATA_FROM_LOCAL);
		apiLog.setIsSuccess(String.valueOf(returnMap.get("isSuccess")));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);
		
		return returnMap;
	}
	

}
