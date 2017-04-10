package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.service.DataBaseService;
import org.pbccrc.api.core.dao.DataBaseDao;
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
		
		List<Map<String, Object>> columns = null;
		
		try {
			columns = dataBaseDao.queryColumnByTable(tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
		
		List<Map<String, Object>> tables = null;
		
		try {
			tables = dataBaseDao.queryAllTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Map<String, Object> table : tables) {
			jsonArray.add(JSONObject.toJSON(table));
		}
		
		return jsonArray;
	}
	
	
}
