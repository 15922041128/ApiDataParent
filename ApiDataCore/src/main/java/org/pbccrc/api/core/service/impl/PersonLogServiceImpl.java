package org.pbccrc.api.core.service.impl;

import java.util.List;

import org.pbccrc.api.base.bean.PersonLog;
import org.pbccrc.api.base.service.PersonLogService;
import org.pbccrc.api.core.dao.PersonLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonLogServiceImpl implements PersonLogService {

	@Autowired
	private PersonLogDao personLogDao;
	
	public List<PersonLog> queryLog(PersonLog personLog) {
		return personLogDao.queryLog(personLog);
	}
	
	public void addLog(PersonLog personLog) {
		personLogDao.addLog(personLog);
	}
}
