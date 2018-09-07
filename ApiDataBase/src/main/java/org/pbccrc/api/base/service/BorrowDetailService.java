package org.pbccrc.api.base.service;

import org.pbccrc.api.base.bean.LocalApi;

import com.alibaba.fastjson.JSONObject;

public interface BorrowDetailService {
	
	/**
	 * 查询白名单
	 * @param realName    姓名
	 * @param idCard	    身份证号
	 * @param userID	   用户ID
	 * @param uuid	      uuid
	 * @param localApi	  localApi
	 * @return
	 * @throws Exception
	 */
	public JSONObject getBorrowDetail(String realName, String idCard, String userID, String uuid, LocalApi localApi) throws Exception;
	

}
