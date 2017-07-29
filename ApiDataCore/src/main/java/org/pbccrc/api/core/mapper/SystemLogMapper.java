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
	 * 查询汇总日志（用户、产品、时间维度）
	 * @param systemLog
	 */
	List<SystemLog> sumLog(Map<String, String> queryMap);
	
	/**
	 * 查询汇总日志（用户、产品、时间、API维度）
	 * @param systemLog
	 */
	List<SystemLog> sumApiLog(Map<String, String> queryMap);
	
	/**
	 * 查询日志详情
	 * @param systemLog
	 */
	List<SystemLog> queryLogDetail(Map<String, String> queryMap);
}
