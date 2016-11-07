package org.pbccrc.api.base.service;

public interface CostService {
	
	/**
	 * 计费
	 * @param userID
	 * @param apiKey
	 * @param localApiID
	 */
	public void cost(String userID, String apiKey, String localApiID);

}
