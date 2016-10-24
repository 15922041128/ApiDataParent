package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface LocalApiMapper {

	/**
	 * 根据serivce名称查询api
	 * @param service
	 * @return
	 */
	Map<String, Object> queryByService(String service);
	
	/***
	 * 查询所有api
	 * @return
	 */
	List<Map<String, Object>> queryAll();
}
