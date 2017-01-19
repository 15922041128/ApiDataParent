package org.pbccrc.api.base.service;

import java.util.Map;

public interface CostService {
	
	/**
	 * 计费
	 * @param userID
	 * @param apiKey
	 */
	public Map<String, Object> cost(String userID, String apiKey);

}
