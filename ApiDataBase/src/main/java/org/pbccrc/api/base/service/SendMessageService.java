package org.pbccrc.api.base.service;

import java.util.Map;

public interface SendMessageService {
	
	
	Map<String, Object> sendMessage(String telNos, String msgContent, String type, String trxNo, String uuid, String userID) throws Exception;

}
