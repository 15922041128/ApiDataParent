package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.service.DataBaseService;
import org.pbccrc.api.core.dao.DataBaseDao;
import org.pbccrc.api.core.dao.datasource.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class DataBaseServiceImpl implements DataBaseService {

	@Autowired
	private DataBaseDao dataBaseDao;
	
	/**
	 * 根据表名查询该表字段信息
	 * @param tableName
	 * @return
	 */
	public JSONArray queryColumnByTable(String tableName) {
		
		JSONArray jsonArray = new JSONArray();
		
		DynamicDataSourceHolder.change2oracle();
		List<Map<String, Object>> columns = dataBaseDao.queryColumnByTable(tableName);
		DynamicDataSourceHolder.change2mysql();
		
		for (Map<String, Object> column : columns) {
			jsonArray.add(JSONObject.toJSON(column));
		}
		
		return jsonArray;
	}

	/**
	 * 查询所有表
	 * @return
	 */
	public JSONArray queryAllTable() {
		
		JSONArray jsonArray = new JSONArray();
		
		DynamicDataSourceHolder.change2oracle();
		List<Map<String, Object>> tables = dataBaseDao.queryAllTable();
		DynamicDataSourceHolder.change2mysql();
		
		for (Map<String, Object> table : tables) {
			jsonArray.add(JSONObject.toJSON(table));
		}
		
		return jsonArray;
	}
	
	
}
