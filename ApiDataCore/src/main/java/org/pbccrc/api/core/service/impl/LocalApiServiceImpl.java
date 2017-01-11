package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.core.dao.LocalApiDao;
import org.pbccrc.api.base.bean.LocalApi;
import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.service.LocalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalApiServiceImpl implements LocalApiService{

	@Autowired
	private LocalApiDao localApiDao;
	
	public Map<String, Object> queryByService(String service) {
		return localApiDao.queryByService(service);
	}

	public List<Map<String, Object>> queryAll() {
		return localApiDao.queryAll();
	}
	
	/***
	 * 分页条件查询所有api
	 * @return
	 */
	public Pagination queryApiByPage(LocalApi localApi, Pagination pagination){
		return localApiDao.queryApiByPage(localApi, pagination);
	}
	
	/**
	 * 查询api服务地址是否存在
	 * @param service
	 * @return
	 */
	public Integer isExist(String service){
		return localApiDao.isExist(service);
	}
	
	/**
	 * 添加api
	 * @param LocalApi
	 */
	public void addLocalApi(LocalApi localApi){
		localApiDao.addLocalApi(localApi);
	}
	
	/**
	 * 修改api
	 * @param LocalApi
	 */
	public void updateLocalApi(LocalApi localApi){
		localApiDao.updateLocalApi(localApi);
	}

}
