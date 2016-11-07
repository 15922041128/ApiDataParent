package org.pbccrc.api.base.service;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.DBEntity;

public interface DBOperatorService {
	
	int insertData(DBEntity entity);
	
	Map<String, Object> queryData(DBEntity entity);
	
	List<Map<String, Object>> queryDatas(DBEntity entity);
	
	void updateData(DBEntity entity);

	public Map<String, Object> queryBySql(DBEntity entity);
}
