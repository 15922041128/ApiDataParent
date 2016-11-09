package org.pbccrc.api.base.service;

import java.util.List;

import org.pbccrc.api.base.bean.ApiLog;

public interface ApiLogService {

	List<ApiLog> queryLog(ApiLog apiLog);
	
	void addLog(ApiLog apiLog);
}
