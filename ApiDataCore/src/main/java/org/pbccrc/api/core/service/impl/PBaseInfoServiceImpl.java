package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.service.PBaseInfoService;
import org.pbccrc.api.core.dao.PBaseInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("pBaseInfoServiceImpl")
public class PBaseInfoServiceImpl implements PBaseInfoService{

	@Autowired
	private PBaseInfoDao pBaseInfoDao;
	
	public List<Map<String, Object>> queryByPersonID(String personID) {
		return pBaseInfoDao.queryByPersonID(personID);
	}
	
	public Integer addPBaseInfo(Map<String, Object> pBaseInfo) {
		return pBaseInfoDao.addPBaseInfo(pBaseInfo);
	}

}
