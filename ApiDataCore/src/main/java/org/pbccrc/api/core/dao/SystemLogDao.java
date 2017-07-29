package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.core.mapper.SystemLogMapper;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class SystemLogDao{
	
	@Resource
	private SystemLogMapper systemLogMapper;
	
	public List<SystemLog> queryLog(SystemLog systemLog) {
		return systemLogMapper.queryLog(systemLog);
	}
	
	public void addLog(SystemLog systemLog) {
		systemLogMapper.addLog(systemLog);
	}
	
	public Pagination sumLog(Map<String, String> queryMap, Pagination pagination) {
		PageHelper.startPage(pagination.getCurrentPage(), pagination.getPageSize());
		Page<SystemLog> logs = (Page<SystemLog>) systemLogMapper.sumLog(queryMap);
		Pagination logsPagination = new Pagination();
		logsPagination.setResult(logs.getResult());
		logsPagination.setTotalCount(logs.getTotal());
		return logsPagination;
	}
	
	public Pagination sumApiLog(Map<String, String> queryMap, Pagination pagination) {
		PageHelper.startPage(pagination.getCurrentPage(), pagination.getPageSize());
		Page<SystemLog> logs = (Page<SystemLog>) systemLogMapper.sumApiLog(queryMap);
		Pagination logsPagination = new Pagination();
		logsPagination.setResult(logs.getResult());
		logsPagination.setTotalCount(logs.getTotal());
		return logsPagination;
	}
	
	public Pagination queryLogDetail(Map<String, String> queryMap, Pagination pagination) {
		PageHelper.startPage(pagination.getCurrentPage(), pagination.getPageSize());
		Page<SystemLog> logs = (Page<SystemLog>) systemLogMapper.queryLogDetail(queryMap);
		Pagination logsPagination = new Pagination();
		logsPagination.setResult(logs.getResult());
		logsPagination.setTotalCount(logs.getTotal());
		return logsPagination;
	}
	
}
