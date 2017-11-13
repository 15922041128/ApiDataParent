package org.pbccrc.api.base.service;

import com.alibaba.fastjson.JSONObject;

public interface BorrowDetailService {
	
	/**
	 * 查询白名单
	 * @param realName    姓名
	 * @param idCard	    身份证号
	 * @param userID	   用户ID
	 * @param uuid	      uuid
	 * @return
	 * @throws Exception
	 */
	public JSONObject getBorrowDetail(String realName, String idCard, String userID, String uuid) throws Exception;
	

}
