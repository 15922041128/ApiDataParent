package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductMapper {

	/***
	 * 查询所有产品
	 * @return
	 */
	List<Map<String, Object>> queryAll();
	
}
