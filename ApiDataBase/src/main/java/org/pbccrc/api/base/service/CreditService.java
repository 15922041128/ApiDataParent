package org.pbccrc.api.base.service;

import java.io.File;

import org.pbccrc.api.base.bean.User;

import com.alibaba.fastjson.JSONObject;

public interface CreditService {
	
	/**
	 * 信用风险信息报送step1
	 * @param name
	 * @param idCardNo
	 * @param tel
	 * @param address
	 * @param photo
	 * @param currentUser
	 * @return personID
	 */
	JSONObject addPerson(String name, String idCardNo, String tel, String address, File photo, String currentUser) throws Exception;
	
	/**
	 * 信用风险信息报送step2
	 * @param personID 			personID
	 * @param contactDate 		合同日期
	 * @param hireDate			起租日
	 * @param expireDate		到期日
	 * @param type				业务类型
	 * @param loanUsed			贷款用途
	 * @param totalAmount		总金额
	 * @param balance			余额
	 * @param status			状态
	 * @param user				当前用户
	 * @return
	 */
	JSONObject addPersonRedit(String personID, String contactDate, String hireDate, String expireDate, 
			String type, String loanUsed, String totalAmount, String balance, String status, User user) throws Exception;
	
	/**
	 * 信用风险信息查询
	 * @param name
	 * @param idCardNo
	 * @return
	 * @throws Exception
	 */
	JSONObject getReditList(String name, String idCardNo) throws Exception;

}
