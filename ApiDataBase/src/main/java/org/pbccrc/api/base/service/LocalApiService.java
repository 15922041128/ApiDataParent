package org.pbccrc.api.base.service;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.LocalApi;
import org.pbccrc.api.base.bean.Pagination;

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
	
	/***
	 * 分页条件查询所有api
	 * @return
	 */
	Pagination queryApiByPage(LocalApi localApi, Pagination pagination);
	
	/**
	 * 查询api服务地址是否存在
	 * @param service
	 * @return
	 */
	Integer isExist(String service);
	
	/**
	 * 添加api
	 * @param LocalApi
	 */
	void addLocalApi(LocalApi localApi);
	
	/**
	 * 修改api
	 * @param LocalApi
	 */
	void updateLocalApi(LocalApi localApi);

}
