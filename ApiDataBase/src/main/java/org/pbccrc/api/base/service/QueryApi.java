package org.pbccrc.api.base.service;

import java.util.Map;

import org.pbccrc.api.base.bean.LocalApi;

public interface QueryApi {

	/**
	 * @param localApi		  本地api	
	 * @param urlParams		  请求参数
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> query(LocalApi localApi, Map urlParams) throws Exception;
}
