package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.DBEntity;
import org.pbccrc.api.core.dao.DBOperatorDao;
import org.pbccrc.api.base.service.DBOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dbOperatorService")
public class DBOperatorServiceImpl implements DBOperatorService {
	
	@Autowired
	private DBOperatorDao dbOperatorDao;

	public int insertData(DBEntity entity) {
		return dbOperatorDao.insertData(entity);
	}

	public Map<String, Object> queryData(DBEntity entity) {
		return dbOperatorDao.queryData(entity);
	}

	public List<Map<String, Object>> queryDatas(DBEntity entity) {
		return dbOperatorDao.queryDatas(entity);
	}

	public void updateData(DBEntity entity) {
		dbOperatorDao.updateData(entity);
	}
	
	public Map<String, Object> queryBySql(DBEntity entity) {
		
		return dbOperatorDao.queryBySql(entity);
	}

}
