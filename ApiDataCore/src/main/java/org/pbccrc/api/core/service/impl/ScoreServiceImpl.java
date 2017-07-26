package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.service.ScoreService;
import org.pbccrc.api.core.dao.ScoreDao;
//import org.pbccrc.api.core.dao.datasource.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreServiceImpl implements ScoreService{

	@Autowired
	private ScoreDao scoreDao;
	
	public List<Map<String, Object>> getScore(String innerID) {
		
//		try {
//			DynamicDataSourceHolder.change2oracle();
//			List<Map<String, Object>> scoreList = scoreDao.getScore(innerID);
//			DynamicDataSourceHolder.change2mysql();
//			return scoreList;
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			DynamicDataSourceHolder.change2mysql();
//		}
		
		return scoreDao.getScore(innerID);
		
	}

}
