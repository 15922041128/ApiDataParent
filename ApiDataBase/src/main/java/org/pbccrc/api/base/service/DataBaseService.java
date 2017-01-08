package org.pbccrc.api.base.service;

import com.alibaba.fastjson.JSONArray;

public interface DataBaseService {

	/**
	 * 根据表名查询该表字段信息
	 * @param tableName
	 * @return
	 */
	JSONArray queryColumnByTable(String tableName);
	
	/**
	 * 查询所有表
	 * @return
	 */
	JSONArray queryAllTable();
}
