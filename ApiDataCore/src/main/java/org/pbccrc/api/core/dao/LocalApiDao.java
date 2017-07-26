package org.pbccrc.api.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.LocalApi;
import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.core.mapper.LocalApiMapper;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class LocalApiDao {
	
	@Resource
	private LocalApiMapper localApiMapper;
	
//	public Map<String, Object> queryByService(String service){
	public LocalApi queryByService(String service){
		return localApiMapper.queryByService(service);
	}
	
//	public List<Map<String, Object>> queryAll() {
	public List<LocalApi> queryAll() {
		return localApiMapper.queryAll();
	}
	
	/***
	 * 分页条件查询所有api
	 * @return
	 */
	public Pagination queryApiByPage(LocalApi localApi, Pagination pagination){
		PageHelper.startPage(pagination.getCurrentPage(), pagination.getPageSize());
		Page<LocalApi> apis = (Page<LocalApi>) localApiMapper.queryApiByPage(localApi);
		Pagination apiPagination = new Pagination();
		apiPagination.setResult(apis.getResult());
		apiPagination.setTotalCount(apis.getTotal());
		return apiPagination;
	}
	
	/**
	 * 查询api服务地址是否存在
	 * @param service
	 * @return
	 */
	public Integer isExist(String service){
		return localApiMapper.isExist(service);
	}
	
	/**
	 * 添加api
	 * @param LocalApi
	 */
	public void addLocalApi(LocalApi localApi){
		localApiMapper.addLocalApi(localApi);
	}

	/**
	 * 修改api
	 * @param LocalApi
	 */
	public void updateLocalApi(LocalApi localApi){
		localApiMapper.updateLocalApi(localApi);
	}
	
}
