package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.core.dao.LocalApiDao;
import org.pbccrc.api.base.bean.LocalApi;
import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.service.LocalApiService;
import org.pbccrc.api.base.util.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public void addLocalApi(LocalApi localApi){
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDD");
		localApi.setCreateDate(dateFormat.format(new Date()));
		localApiDao.addLocalApi(localApi);
		RedisClient.setObject("localApi_" + localApi.getId(), localApi);
	}
	
	/**
	 * 修改api
	 * @param LocalApi
	 */
	@Transactional
	public void updateLocalApi(LocalApi localApi){
		localApiDao.updateLocalApi(localApi);
		RedisClient.setObject("localApi_" + localApi.getId(), localApi);
	}

}
