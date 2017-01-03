package org.pbccrc.api.base.service;

public interface ApplyService {

	/**
	 * 申请试用
	 * @param userID
	 * @param productID
	 * @return
	 */
	boolean doApply(String userID, String productID);
}
