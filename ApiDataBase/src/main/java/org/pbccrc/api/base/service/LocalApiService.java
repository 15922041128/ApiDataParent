package org.pbccrc.api.base.service;

import java.util.List;
import java.util.Map;

public interface LocalApiService {
	
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
