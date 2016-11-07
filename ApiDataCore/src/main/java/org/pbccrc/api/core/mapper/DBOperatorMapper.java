package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.DBEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DBOperatorMapper {

	int insertData(DBEntity entity);
	
	Map<String, Object> queryData(DBEntity entity);
	
	List<Map<String, Object>> queryDatas(DBEntity entity);
	
	void updateData(DBEntity entity);
	
}
