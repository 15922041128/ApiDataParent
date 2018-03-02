package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.ResultMap;
import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.bean.SmsCondition;
import org.pbccrc.api.base.bean.SmsLog;
import org.pbccrc.api.base.service.MarketingService;
import org.pbccrc.api.base.service.SendMessageCoreService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.dao.ApiLogDao;
import org.pbccrc.api.core.dao.MarketeeDao;
import org.pbccrc.api.core.dao.SmsLogDao;
import org.pbccrc.api.core.dao.ZhIdentificationDao;
import org.pbccrc.api.core.dao.util.SmsUtil;
import org.pbccrc.api.core.dao.util.SmsWorkQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;

@Service
public class MarketingServiceImpl implements MarketingService{
	
	private SendMessageCoreService sendMessageCoreService;
	
	@Autowired
	private ApiLogDao apiLogDao;
	
	@Autowired
	private SmsLogDao smsLogDao;

	@Autowired
	private MarketeeDao marketeeDao;
	
	@Autowired
	private ZhIdentificationDao zhIdentificationDao;
	
	public JSONObject getMarketeeCount(SmsCondition smsCondition) throws Exception {
		
		Integer marketeeNum = marketeeDao.getMarketeeCount(smsCondition);
		JSONObject returnJson = new JSONObject();
		JSONObject retData = new JSONObject();
		retData.put("marketeeNum", marketeeNum);
		returnJson.put("retData", retData);
		returnJson.put("seq", StringUtil.createUUID());
		return returnJson;
	}
	
	public JSONObject sendMesg1(SmsCondition smsCondition) throws Exception {
		
		List<String> telNums = marketeeDao.getMarketeeTelNums(smsCondition);
		String seq = StringUtil.createRandomID();
		smsCondition.setSeq(seq);
		JSONObject returnJson = new JSONObject();
		returnJson.put("seq", seq);
		for (int i = 0; i < telNums.size(); i+=500) {
			List<String> newList = telNums.subList(i, i+499);
			//调用短信接口发送
			JSONObject object = JSONObject.parseObject(String.valueOf(RedisClient.get("sendMsgRef_" + smsCondition.getSmsTunnel())));
			String className = object.getString("className");
			sendMessageCoreService = (SendMessageCoreService) Class.forName(className).newInstance();
			String numStr = Joiner.on(",").join(newList);
			Map<String, Object> returnMap = sendMessageCoreService.sendMessage(numStr, smsCondition.getContent());
			
			
			SmsLog smsLog = new SmsLog();
			smsLog.setContent(smsCondition.getContent());
			smsLog.setNumbers(numStr);
			JSONObject params = new JSONObject();
    		params.put("productType", smsCondition.getProductType());
    		params.put("age_max", smsCondition.getAge_max());
    		params.put("age_min", smsCondition.getAge_min());
    		params.put("operator", smsCondition.getOperator());
    		params.put("province", smsCondition.getProvince());
    		params.put("city", smsCondition.getCity());
			smsLog.setParams(params.toJSONString());
			smsLog.setFeedBack(String.valueOf(returnMap.get("feedBack")));
			smsLog.setSmsTunnel(smsCondition.getSmsTunnel());
			smsLog.setSendDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
			smsLogDao.addLog(smsLog);
			
			// 记录日志
			ApiLog apiLog = new ApiLog();
			// uuid
			apiLog.setUuid(seq);
			apiLog.setUserID("短信");
			apiLog.setLocalApiID(Constants.API_ID_SEND_MESSAGE);
			// 参数
//			JSONObject params = new JSONObject();
			params.put("telNus", numStr);
			params.put("msgContent", smsCondition.getContent());
			params.put("type", smsCondition.getOperator());
			apiLog.setParams(params.toJSONString());
			apiLog.setDataFrom(Constants.DATA_FROM_LOCAL);
			apiLog.setIsSuccess(String.valueOf(returnMap.get("isSuccess")));
			apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
			apiLogDao.addLog(apiLog);
		}
		
		
		return returnJson;
	}
	
	@Override
	public JSONObject sendMesg(SmsCondition smsCondition) throws Exception {
		SmsWorkQueue wq = SmsUtil.wq;// 获取工作线程  
		String seq = StringUtil.createRandomID();
		smsCondition.setSeq(seq);
		wq.execute(new Mytask(smsCondition));  
		JSONObject returnJson = new JSONObject();
		returnJson.put("seq", seq);
		return returnJson;
	}
	
	class Mytask implements Runnable {
		private SmsCondition condition = null;
	
		public Mytask(SmsCondition smsCondition) {
			this.condition = smsCondition;
		}

	// 任务接口  
	    public void run() {  
	        String name = Thread.currentThread().getName();  
	        try {
	        	System.out.println(condition.getSeq()+"：短信发送任务开始执行");
	        	//选择短信接口
	        	JSONObject object = JSONObject.parseObject(String.valueOf(RedisClient.get("sendMsgRef_" + condition.getSmsTunnel())));
	        	String className = object.getString("className");
	        	sendMessageCoreService = (SendMessageCoreService) Class.forName(className).newInstance();
	        	//获取待发送电话号码
	        	Integer sendNum = condition.getSendNum();
	        	Integer batchNum = (int) Math.ceil(sendNum/sendMessageCoreService.getSendSize());
	        	for (int i = 0; i < batchNum; i++) {
	        		List<String> telNums = marketeeDao.getMarketeeTelNums(condition);
	        		if (telNums.size() == 0){
	        			break;
	        		}
	    			String numStr = Joiner.on(",").join(telNums);
	    			//调用短信接口发送
	    			Map<String, Object> returnMap = sendMessageCoreService.sendMessage(numStr, condition.getContent());
	    			System.out.println(condition.getSmsTunnel() + "Seq" + condition.getSeq() + "第" + i + "批次：" + returnMap.get("feedBack"));
	    			SmsLog smsLog = new SmsLog();
	    			smsLog.setContent(condition.getContent());
	    			smsLog.setNumbers(numStr);
	    			smsLog.setNumCount(telNums.size());
	    			JSONObject params = new JSONObject();
	        		params.put("productType", condition.getProductType());
	        		params.put("age_max", condition.getAge_max());
	        		params.put("age_min", condition.getAge_min());
	        		params.put("operator", condition.getOperator());
	        		params.put("province", condition.getProvince());
	        		params.put("city", condition.getCity());
	    			smsLog.setParams(params.toJSONString());
	    			smsLog.setFeedBack(String.valueOf(returnMap.get("feedBack")));
	    			smsLog.setSendState(returnMap.get("isSuccess") == "true" ? 1 : 0);
	    			smsLog.setSmsTunnel(condition.getSmsTunnel());
	    			smsLog.setSendDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
	    			smsLogDao.addLog(smsLog);
	    			
	    		}
	        	// 记录日志
	        	ApiLog apiLog = new ApiLog();
	        	// uuid
	        	apiLog.setUuid(condition.getSeq());
	        	apiLog.setUserID("短信");
	        	apiLog.setLocalApiID(Constants.API_ID_SEND_MESSAGE);
	        	// 参数
	    		JSONObject params = new JSONObject();
	        	params.put("type", JSON.toJSONString(condition));
	        	apiLog.setParams(params.toJSONString());
	        	apiLog.setDataFrom(Constants.DATA_FROM_LOCAL);
	        	apiLog.setIsSuccess("true");
	        	apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
	        	apiLogDao.addLog(apiLog);
	        	
	        	
//	        	List<String> romoveList = new ArrayList<String>();
//	        	for (String telNum : telNums) {
//	        		//查询号码是否在缓存中
//	        		if(RedisClient.exists("marketee_" + telNum)){
//	        			romoveList.add(telNum);
//	        			System.out.println("号码已经发送过"+ telNum);
//	        		}
//				}

	        	
//	            for (String telNum : telNums) {
//	            	//将发送过的号码记录缓存
//	            	RedisClient.set("marketee_" + telNum, condition.getProductType());
//	            	//设置缓存过期时间为7天
//	            	RedisClient.setExpireTime("marketee_" + telNum, 30);
//	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }  
	        System.out.println(name + " executed OK");  
	    }  
	}


	@Override
	public JSONObject getSmsResult(String seq) throws Exception {
		String errorMessage = Constants.BLANK;
		
		JSONObject returnJson = new JSONObject();
		JSONObject retData = new JSONObject();
		retData.put("sendNum", "5000");
		retData.put("RealNum", "4910");
		retData.put("successNum", "3820");
		retData.put("failNum", "1090");
		returnJson.put("retData", retData);
		returnJson.put("errorMessage", errorMessage);
		
		
		return returnJson;
	}

}
