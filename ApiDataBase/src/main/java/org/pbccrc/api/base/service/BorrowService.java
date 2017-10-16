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
	
	/**
	 * 
	 * @param realName    姓名
	 * @param idCard	    身份证号
	 * @param trxNo       查询编码
	 * @param loanInfos   查询数据 
	 * @return
	 * @throws Exception
	 */
	public JSONObject getResultTri(String realName, String idCard, String trxNo, String loanInfos) throws Exception;
	
	/**
	 * 
	 * @param dataCnt		记录条数
	 * @param amount		额度
	 * @param amountFlag	额度之下或之上 (1以下,2以上,默认3000以下)
	 * @param status		状态(好，坏，灰)
	 * @param count			借贷次数 (默认为1)
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryResult(String dataCnt, String amount, String amountFlag, String status, String count) throws Exception;

}
