package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface DataBaseMapper {

	List<Map<String, Object>> queryColumnByTable(String tableName);
	
	List<Map<String, Object>> queryAllTable();
}
