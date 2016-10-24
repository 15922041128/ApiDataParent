package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.DBEntity;
import org.pbccrc.api.core.mapper.DBOperatorMapper;
import org.pbccrc.api.base.util.Constants;
import org.springframework.stereotype.Service;

@Service("dbOperatorDao")
public class DBOperatorDao {

	@Resource
	private DBOperatorMapper dbOperatorMapper;
	
	public int insertData(DBEntity entity) {
		return dbOperatorMapper.insertData(entity);
	}
	
	public Map<String, Object> queryData(DBEntity entity) {
		
		StringBuffer sql = new StringBuffer();
		
		List<String> fields = entity.getFields();
		List<String> values = entity.getValues();
		
		for (int i = 0; i < fields.size(); i++) {
			sql.append(Constants.AND);
			sql.append("t." + fields.get(i));
			sql.append(Constants.EQUAL);
			sql.append(Constants.SINGLE_QUOTES + values.get(i) + Constants.SINGLE_QUOTES);
			sql.append(Constants.SPACE);
		}
		
		entity.setSql(sql.toString());
		
		return dbOperatorMapper.queryData(entity);
	}
	
	public List<Map<String, Object>> queryDatas(DBEntity entity) {
		
		StringBuffer sql = new StringBuffer();
		
		List<String> fields = entity.getFields();
		List<String> values = entity.getValues();
		
		for (int i = 0; i < fields.size(); i++) {
			sql.append(Constants.AND);
			sql.append("t." + fields.get(i));
			sql.append(Constants.EQUAL);
			sql.append(Constants.SINGLE_QUOTES + values.get(i) + Constants.SINGLE_QUOTES);
			sql.append(Constants.SPACE);
		}
		
		entity.setSql(sql.toString());
		
		return dbOperatorMapper.queryDatas(entity);
	}
	
	public void updateData(DBEntity entity) {
		dbOperatorMapper.updateData(entity);
	}
}
