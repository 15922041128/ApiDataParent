package org.pbccrc.api.base.service;

import java.util.Map;

public interface CostService {
	
	/**
	 * 计费
	 * @param userID
	 * @param apiKey
	 * @param localApi
	 */
	public void cost(String userID, String apiKey, Map<String, Object> localApi);

}
