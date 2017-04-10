package org.pbccrc.api.core.mapper;

import java.util.List;

import org.pbccrc.api.base.bean.PersonLog;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonLogMapper {

	public List<PersonLog> queryLog(PersonLog personLog);
	
	public void addLog(PersonLog personLog);
}
