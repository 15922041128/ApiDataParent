package org.pbccrc.api.base.service;

import com.alibaba.fastjson.JSONObject;

public interface ApiProductService {

	/**
	 * 反欺诈服务
	 * @param phone    电话号码
	 * @param name     姓名 
	 * @param idCard   身份证号码
	 * @param userID   用户ID
	 * @param uuid     UUID
	 * @return
	 * @throws Exception
	 */
	public JSONObject fraud(String phone, String name, String idCard, String userID, String uuid) throws Exception;
}
