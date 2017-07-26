package org.pbccrc.api.core.service.impl;

import java.util.Map;

import org.pbccrc.api.base.service.ZhIdentificationService;
import org.pbccrc.api.core.dao.ZhIdentificationDao;
//import org.pbccrc.api.core.dao.datasource.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZhIdentificationServiceImpl implements ZhIdentificationService{
	
	@Autowired
	private ZhIdentificationDao zhIdentificationDao;

	@Override
	public Map<String, Object> getInnerID(String name, String identifier) {
		
//		Map<String, Object> insideCodeMap = null;
//		try {
//			DynamicDataSourceHolder.change2oracle();
//			insideCodeMap = zhIdentificationDao.getInnerID(name, identifier);
//			DynamicDataSourceHolder.change2mysql();
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			DynamicDataSourceHolder.change2mysql();
//		}
		
		return zhIdentificationDao.getInnerID(name, identifier);
	}

}
