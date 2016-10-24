package org.pbccrc.api.core.service.impl;

import java.util.List;

import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.core.dao.SystemLogDao;
import org.pbccrc.api.base.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemLogServiceImpl implements SystemLogService{

	@Autowired
	private SystemLogDao systemLogDao;
	
	public List<SystemLog> queryLog(SystemLog systemLog) {
		return systemLogDao.queryLog(systemLog);
	}

	public void addLog(SystemLog systemLog) {
		systemLogDao.addLog(systemLog);
	}

}
