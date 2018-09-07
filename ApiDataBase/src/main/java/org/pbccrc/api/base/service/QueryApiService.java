package org.pbccrc.api.base.service;

import java.util.Map;

import org.pbccrc.api.base.bean.LocalApi;

public interface QueryApiService {

	@SuppressWarnings("rawtypes")
	public Map<String, Object> query(String uuid, String userID, String service, Map urlParams) throws Exception;
	
	public Map<String, Object> querySfz(String uuid, String userID, String name, String idCardNo, LocalApi localApi) throws Exception;
}
