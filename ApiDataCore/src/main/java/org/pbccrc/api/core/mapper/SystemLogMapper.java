package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.SystemLog;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemLogMapper {

	/**
	 * 查询日志
	 * @param systemLog
	 * @return
	 */
	List<SystemLog> queryLog(SystemLog systemLog);
	
	/**
	 * 添加日志
	 * @param systemLog
	 */
	void addLog(SystemLog systemLog);
	
	/**
	 * 添加日志
	 * @param systemLog
	 */
	List<Map<String, Object>> sumLog(Map<String, String> queryMap);
	
	/**
	 * 查询日志详情
	 * @param systemLog
	 */
	List<Map<String, Object>> queryLogDetail(Map<String, String> queryMap);
}
