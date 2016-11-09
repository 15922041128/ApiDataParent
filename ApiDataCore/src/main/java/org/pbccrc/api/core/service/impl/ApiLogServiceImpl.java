package org.pbccrc.api.core.service.impl;

import java.util.List;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.service.ApiLogService;
import org.pbccrc.api.core.dao.ApiLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiLogServiceImpl implements ApiLogService{

	@Autowired
	private ApiLogDao apiLogDao;
	
	public List<ApiLog> queryLog(ApiLog apiLog) {
		return apiLogDao.queryLog(apiLog);
	}

	public void addLog(ApiLog apiLog) {
		apiLogDao.addLog(apiLog);
	}

}
