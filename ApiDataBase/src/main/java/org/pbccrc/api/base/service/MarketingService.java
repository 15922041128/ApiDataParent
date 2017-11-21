package org.pbccrc.api.base.service;

import org.pbccrc.api.base.bean.SmsCondition;

import com.alibaba.fastjson.JSONObject;
/**
 * 短信营销service
 * @author charles
 *
 */
public interface MarketingService {
	
	/**
	 * 获取符合条件的营销对象数量
	 * @param smsCondition   短信接口查询条件
	 * @return
	 * @throws Exception
	 */
	public JSONObject getMarketeeCount(SmsCondition smsCondition) throws Exception;
	
	/**
	 * 发送营销短信
	 * @param smsCondition   短信接口查询条件
	 * @return
	 * @throws Exception
	 */
	public JSONObject sendMesg(SmsCondition smsCondition) throws Exception;
	
	/**
	 * 获取短信状态结果
	 * @param seq       查询编码
	 * @return
	 * @throws Exception
	 */
	public JSONObject getSmsResult(String seq) throws Exception;
	
}
