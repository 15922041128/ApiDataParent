package org.pbccrc.api.core.service.impl;

import java.util.List;

import org.pbccrc.api.base.bean.Marketee;
import org.pbccrc.api.base.bean.SmsCondition;
import org.pbccrc.api.base.service.MarketingService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.dao.MarketeeDao;
import org.pbccrc.api.core.dao.ZhIdentificationDao;
import org.pbccrc.api.core.dao.util.SmsWorkQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;

@Service
public class MarketingServiceImpl implements MarketingService{

	@Autowired
	private MarketeeDao marketeeDao;
	
	@Autowired
	private ZhIdentificationDao zhIdentificationDao;
	
	public JSONObject getMarketeeCount(SmsCondition smsCondition) throws Exception {
		
		String errorMessage = Constants.BLANK;
		Integer marketeeNum = marketeeDao.getMarketeeCount(smsCondition);
		JSONObject returnJson = new JSONObject();
		JSONObject retData = new JSONObject();
		retData.put("marketeeNum", marketeeNum);
		returnJson.put("retData", retData);
		returnJson.put("errorMessage", errorMessage);
		return returnJson;
	}
	
	@Override
	public JSONObject sendMesg(SmsCondition smsCondition) throws Exception {
		String errorMessage = Constants.BLANK;
		int sendNum = smsCondition.getSendNum();
		int batch = (int) Math.ceil(sendNum/smsCondition.getPageSize());
		SmsWorkQueue wq = new SmsWorkQueue(2);// 2个工作线程  
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
		returnJson.put("errorMessage", errorMessage);
		
		
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
	        	System.out.println("：我开始执行啦");
	        	List<Marketee> telNums = marketeeDao.getMarketeeTelNums(condition);
	        	for (Marketee marketee : telNums) {
	        		//查询号码是否在缓存中
	        		if(RedisClient.exists("marketee_" + marketee.getTelNum())){
	        			telNums.remove(marketee);
	        		}
				}
	        	String numString = Joiner.on(",").join(telNums);
	        	String content = condition.getContent();
	        	//TODO 调用短信发送接口
	            Thread.sleep(10000);// 模拟任务执行的时间  
	            for (Marketee marketee : telNums) {
	            	//将发送过的号码记录缓存
	            	RedisClient.set("marketee_" + marketee.getTelNum(), condition.getProductType());
	            	//设置缓存过期时间为7天
	            	RedisClient.setExpireTime("marketee_" + marketee.getTelNum(), 604800);
	            }
	        } catch (InterruptedException e) {  
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
