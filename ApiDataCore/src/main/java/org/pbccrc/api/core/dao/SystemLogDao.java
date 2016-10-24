package org.pbccrc.api.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.core.mapper.SystemLogMapper;
import org.springframework.stereotype.Service;

@Service
public class SystemLogDao{
	
	@Resource
	private SystemLogMapper systemLogMapper;
	
	public List<SystemLog> queryLog(SystemLog systemLog) {
		return systemLogMapper.queryLog(systemLog);
	}
	
	public void addLog(SystemLog systemLog) {
		systemLogMapper.addLog(systemLog);
	}
	
}
