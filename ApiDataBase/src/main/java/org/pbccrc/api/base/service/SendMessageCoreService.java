package org.pbccrc.api.base.service;

import java.util.Map;

public interface SendMessageCoreService {
	
	/**
	 * 
	 * @param telNos		电话号码用","分割
	 * @param msgContent	短信内容
	 * @param sign			签名
	 */
	Map<String, Object> sendMessage(String telNos, String msgContent, String sign) throws Exception;
	
	/**
	 * 获取平台发送号码数量限制
	 * @return
	 */
	Integer getSendSize();

}
