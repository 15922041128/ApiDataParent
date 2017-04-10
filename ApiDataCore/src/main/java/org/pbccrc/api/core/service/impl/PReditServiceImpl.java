package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.service.PReditService;
import org.pbccrc.api.core.dao.PReditDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("pReditServiceImpl")
public class PReditServiceImpl implements PReditService{

	@Autowired
	private PReditDao pReditDao;
	
	public List<Map<String, Object>> queryAll(String personID) {
		return pReditDao.queryAll(personID);
	}
	
	public Integer addPRedit(Map<String, Object> pRedit) {
		return pReditDao.addPRedit(pRedit);
	}

}
