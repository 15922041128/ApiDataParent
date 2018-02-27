package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.bean.SmsCondition;
import org.pbccrc.api.base.service.MarketingService;
import org.pbccrc.api.base.service.SendMessageCoreService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.dao.ApiLogDao;
import org.pbccrc.api.core.dao.MarketeeDao;
import org.pbccrc.api.core.dao.ZhIdentificationDao;
import org.pbccrc.api.core.dao.util.SmsUtil;
import org.pbccrc.api.core.dao.util.SmsWorkQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;

@Service
public class MarketingServiceImpl implements MarketingService{
	
	private SendMessageCoreService sendMessageCoreService;
	
	@Autowired
	private ApiLogDao apiLogDao;

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
	
	@Override
	public JSONObject sendMesg(SmsCondition smsCondition) throws Exception {
		int sendNum = smsCondition.getSendNum();
		int batch = (int) Math.ceil(sendNum/smsCondition.getPageSize());
		SmsWorkQueue wq = SmsUtil.wq;// 2个工作线程  
		String seq = StringUtil.createRandomID();
		smsCondition.setSeq(seq);
		for(int i=0; i < batch; i++){
			smsCondition.setPageNum(i+1);
			smsCondition.getContent();
			// 将发送任务加入发送队列
		    wq.execute(new Mytask(smsCondition));  
		}
		
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
	        	//获取待发送电话号码
	        	System.out.println(condition.getSeq()+"：短信发送任务开始执行");
	        	List<String> telNums = marketeeDao.getMarketeeTelNums(condition);
	        	List<String> romoveList = new ArrayList<String>();
	        	for (String telNum : telNums) {
	        		//查询号码是否在缓存中
	        		if(RedisClient.exists("marketee_" + telNum)){
	        			romoveList.add(telNum);
	        			System.out.println("号码已经发送过"+ telNum);
	        		}
				}
	        	telNums.removeAll(romoveList);
	        	String numString = Joiner.on(",").join(telNums);
	        	String content = condition.getContent();
	        	//调用短信接口发送
	        	JSONObject object = JSONObject.parseObject(String.valueOf(RedisClient.get("sendMsgRef_" + condition.getSmsTunnel())));
	    		String className = object.getString("className");
	    		sendMessageCoreService = (SendMessageCoreService) Class.forName(className).newInstance();
	    		// TODO sign
	    		Map<String, Object> returnMap = sendMessageCoreService.sendMessage(numString, content, null);
	    		
	            for (String telNum : telNums) {
	            	//将发送过的号码记录缓存
	            	RedisClient.set("marketee_" + telNum, condition.getProductType());
	            	//设置缓存过期时间为7天
	            	RedisClient.setExpireTime("marketee_" + telNum, 30);
	            }
	            // 记录日志
	    		ApiLog apiLog = new ApiLog();
	    		// uuid
	    		apiLog.setUuid(condition.getSeq());
	    		apiLog.setUserID("短信");
	    		apiLog.setLocalApiID(Constants.API_ID_SEND_MESSAGE);
	    		// 参数
	    		JSONObject params = new JSONObject();
	    		params.put("telNos", numString);
	    		params.put("msgContent", content);
	    		params.put("type", condition.getOperator());
	    		apiLog.setParams(params.toJSONString());
	    		apiLog.setDataFrom(Constants.DATA_FROM_LOCAL);
	    		apiLog.setIsSuccess(String.valueOf(returnMap.get("isSuccess")));
	    		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
	    		apiLogDao.addLog(apiLog);
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
	public static void main(String[] args) {
		
	}

}
