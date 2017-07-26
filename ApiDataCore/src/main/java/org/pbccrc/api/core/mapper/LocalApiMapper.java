package org.pbccrc.api.core.mapper;

import java.util.List;

import org.pbccrc.api.base.bean.LocalApi;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalApiMapper {

	/**
	 * 根据serivce名称查询api
	 * @param service
	 * @return
	 */
//	Map<String, Object> queryByService(String service);
	LocalApi queryByService(String service);
	
	/***
	 * 查询所有api
	 * @return
	 */
//	List<Map<String, Object>> queryAll();
	List<LocalApi> queryAll();
	
	/***
	 * 分页条件查询所有api
	 * @return
	 */
	List<LocalApi> queryApiByPage(LocalApi localApi);
	
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
	 * 添加api
	 * @param LocalApi
	 */
	void updateLocalApi(LocalApi localApi);
}
