package org.pbccrc.api.core.service.impl;

import java.util.List;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.service.ApiLogService;
import org.pbccrc.api.core.dao.ApiLogDao;
import org.pbccrc.api.core.dao.datasource.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiLogServiceImpl implements ApiLogService{

	@Autowired
	private ApiLogDao apiLogDao;
	
	public List<ApiLog> queryLog(ApiLog apiLog) {
		List<ApiLog> list = null;
		try {
			DynamicDataSourceHolder.change2oracle();
			list = apiLogDao.queryLog(apiLog);
			DynamicDataSourceHolder.change2mysql();
		} catch (Exception e) {
			DynamicDataSourceHolder.change2mysql();
		}
		
		return list;
	}

	public void addLog(ApiLog apiLog) {
		try {
			DynamicDataSourceHolder.change2oracle();
			apiLogDao.addLog(apiLog);
			DynamicDataSourceHolder.change2mysql();
		} catch (Exception e) {
			DynamicDataSourceHolder.change2mysql();
		}
		
	}

}
