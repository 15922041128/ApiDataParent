package org.pbccrc.api.base.service;

public interface CostService {
	
	/**
	 * 计费
	 * @param userID
	 * @param apiKey
	 */
	public void cost(String userID, String apiKey);

}
