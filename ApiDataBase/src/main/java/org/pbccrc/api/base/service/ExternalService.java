package org.pbccrc.api.base.service;

import com.alibaba.fastjson.JSONObject;

public interface ExternalService {

	/**
	 * 唯品会个人风险查询
	 * @param idNo
	 * @param cardName
	 * @param phone
	 * @param cardNo
	 * @param uuid
	 * @return
	 * @throws Exception
	 */
	public JSONObject vipQueryBlackList(String idNo, String cardName, String phone, String cardNo, String userId, String uuid) throws Exception;
}
