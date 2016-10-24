package org.pbccrc.api.base.service;

import java.util.Map;

public interface QueryApiService {

	@SuppressWarnings("rawtypes")
	public Map<String, Object> query(String service, Map urlParams) throws Exception;
}
