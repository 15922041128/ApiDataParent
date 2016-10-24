package org.pbccrc.api.base.service;

import java.util.Map;

public interface QueryApi {

	/**
	 * @param localApi		  本地api	
	 * @param urlParams		  请求参数
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> query(Map<String, Object> localApi, Map urlParams) throws Exception;
}
