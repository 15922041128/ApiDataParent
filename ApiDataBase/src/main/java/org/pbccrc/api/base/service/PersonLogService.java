package org.pbccrc.api.base.service;

import java.util.List;

import org.pbccrc.api.base.bean.PersonLog;

public interface PersonLogService {
	
	public List<PersonLog> queryLog(PersonLog personLog);
	
	public void addLog(PersonLog personLog);

}
