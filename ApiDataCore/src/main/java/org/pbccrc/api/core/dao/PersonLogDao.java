package org.pbccrc.api.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.PersonLog;
import org.pbccrc.api.core.mapper.PersonLogMapper;
import org.springframework.stereotype.Service;

@Service
public class PersonLogDao{
	
	@Resource
	private PersonLogMapper personLogMapper;
	
	public List<PersonLog> queryLog(PersonLog personLog) {
		return personLogMapper.queryLog(personLog);
	}
	
	public void addLog(PersonLog personLog) {
		personLogMapper.addLog(personLog);
	}
	
}
