package org.pbccrc.api.core.service.impl;

import java.util.Map;

import org.pbccrc.api.base.service.PPersonService;
import org.pbccrc.api.core.dao.PPersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("pPersonServiceImpl")
public class PPersonServiceImpl implements PPersonService{
	
	@Autowired
	private PPersonDao pPersonDao;

	public Map<String, Object> selectOne(Map<String, String> person) {
		return pPersonDao.selectOne(person);
	}
	
	public Integer isExist(Map<String, String> person) {
		return pPersonDao.isExist(person);
	}
	
	public Integer addPerson(Map<String, String> person) {
		return pPersonDao.addPerson(person);
	}

}
