package org.pbccrc.api.base.service;

import java.util.List;

import org.pbccrc.api.base.bean.SystemLog;

public interface SystemLogService {
	
	List<SystemLog> queryLog(SystemLog systemLog);
	
	void addLog(SystemLog systemLog);

}
