package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.DataBaseMapper;
import org.springframework.stereotype.Service;

@Service
public class DataBaseDao {
	
	@Resource
	private DataBaseMapper dataBaseMapper;
	
	public List<Map<String, Object>> queryColumnByTable(String tableName) {
		return dataBaseMapper.queryColumnByTable(tableName.toUpperCase());
	}
	
	public List<Map<String, Object>> queryAllTable() {
		return dataBaseMapper.queryAllTable();
	}
}
