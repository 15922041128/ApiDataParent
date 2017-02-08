package org.pbccrc.api.base.service;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.bean.SystemLog;

public interface SystemLogService {
	
	List<SystemLog> queryLog(SystemLog systemLog);
	
	void addLog(SystemLog systemLog);
	
	Pagination sumLog(Map<String, String> queryMap, Pagination pagination);
	
	Pagination sumApiLog(Map<String, String> queryMap, Pagination pagination);
	
	Pagination queryLogDetail(Map<String, String> queryMap, Pagination pagination);

}
