package org.pbccrc.api.base.service;

import com.alibaba.fastjson.JSONObject;

public interface BorrowService {
	
	/**
	 * 
	 * @param realName    姓名
	 * @param idCard	    身份证号
	 * @param trxNo       查询编码
	 * @param loanInfos   查询数据 
	 * @return
	 * @throws Exception
	 */
	public JSONObject getResult(String realName, String idCard, String trxNo, String loanInfos) throws Exception;

}
