package org.pbccrc.api.base.service;

import java.util.Map;

public interface CreditModelService {

	/**
	 * 信贷模型
	 * @param userID
	 * @param name
	 * @param idCardNo
	 * @param accountNo
	 * @param mobile
	 * @return
	 */
	Map<String, Object> creditModel(String userID, String name, String idCardNo, String accountNo, String mobile) throws Exception;
}
