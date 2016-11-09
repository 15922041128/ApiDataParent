package org.pbccrc.api.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.core.mapper.ApiLogMapper;
import org.springframework.stereotype.Service;

@Service
public class ApiLogDao{
	
	@Resource
	private ApiLogMapper apiLogMapper;
	
	public List<ApiLog> queryLog(ApiLog apiLog) {
		return apiLogMapper.queryLog(apiLog);
	}
	
	public void addLog(ApiLog apiLog) {
		apiLogMapper.addLog(apiLog);
	}
	
}
