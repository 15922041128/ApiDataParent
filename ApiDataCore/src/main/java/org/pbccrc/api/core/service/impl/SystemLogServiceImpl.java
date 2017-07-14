package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.core.dao.SystemLogDao;
import org.pbccrc.api.core.dao.datasource.DynamicDataSourceHolder;
import org.pbccrc.api.base.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemLogServiceImpl implements SystemLogService{

	@Autowired
	private SystemLogDao systemLogDao;
	
	public List<SystemLog> queryLog(SystemLog systemLog) {
		List<SystemLog> list = null;
		try {
			DynamicDataSourceHolder.change2oracle();
			list = systemLogDao.queryLog(systemLog);
			DynamicDataSourceHolder.change2mysql();	
		} catch (Exception e) {
			DynamicDataSourceHolder.change2mysql();
		}
		
		return list;
	}

	public void addLog(SystemLog systemLog) {
		try {
			DynamicDataSourceHolder.change2oracle();
			systemLogDao.addLog(systemLog);
			DynamicDataSourceHolder.change2mysql();
		} catch (Exception e) {
			DynamicDataSourceHolder.change2mysql();
		}
		
	}

	public Pagination sumLog(Map<String, String> queryMap, Pagination pagination) {
		Pagination result = null;
		try {
			DynamicDataSourceHolder.change2oracle();
			result = systemLogDao.sumLog(queryMap, pagination);
			DynamicDataSourceHolder.change2mysql();
		} catch (Exception e) {
			DynamicDataSourceHolder.change2mysql();
		}
		
		return result;
	}
	
	public Pagination sumApiLog(Map<String, String> queryMap, Pagination pagination) {
		Pagination result = null;
		try {
			DynamicDataSourceHolder.change2oracle();
			result =  systemLogDao.sumApiLog(queryMap, pagination);
			DynamicDataSourceHolder.change2mysql();
		} catch (Exception e) {
			DynamicDataSourceHolder.change2mysql();
		}
		
		return result;
	}
	
	public Pagination queryLogDetail(Map<String, String> queryMap, Pagination pagination) {
		Pagination result = null;
		try {
			DynamicDataSourceHolder.change2oracle();
			result = systemLogDao.queryLogDetail(queryMap, pagination);
			DynamicDataSourceHolder.change2mysql();
		} catch (Exception e) {
			DynamicDataSourceHolder.change2mysql();
		}
		
		return result;
	}

}
