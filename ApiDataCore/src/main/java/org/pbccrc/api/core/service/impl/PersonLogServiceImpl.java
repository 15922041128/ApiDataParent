package org.pbccrc.api.core.service.impl;

import java.util.List;

import org.pbccrc.api.base.bean.PersonLog;
import org.pbccrc.api.base.service.PersonLogService;
import org.pbccrc.api.core.dao.PersonLogDao;
//import org.pbccrc.api.core.dao.datasource.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonLogServiceImpl implements PersonLogService {

	@Autowired
	private PersonLogDao personLogDao;
	
	public List<PersonLog> queryLog(PersonLog personLog) {
//		List<PersonLog> list = null;
//		try {
//			DynamicDataSourceHolder.change2oracle();
//			list = personLogDao.queryLog(personLog);
//			DynamicDataSourceHolder.change2mysql();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DynamicDataSourceHolder.change2mysql();
//		}
		
		return personLogDao.queryLog(personLog);
	}
	
	public void addLog(PersonLog personLog) {
//		try {
//			DynamicDataSourceHolder.change2oracle();
//			personLogDao.addLog(personLog);
//			DynamicDataSourceHolder.change2mysql();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DynamicDataSourceHolder.change2mysql();
//		}
		
		personLogDao.addLog(personLog);
		
	}
}
