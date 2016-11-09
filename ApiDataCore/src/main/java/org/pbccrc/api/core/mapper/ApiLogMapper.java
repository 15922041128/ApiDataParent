package org.pbccrc.api.core.mapper;

import java.util.List;

import org.pbccrc.api.base.bean.ApiLog;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLogMapper {

	/**
	 * 查询日志
	 * @param apiLog
	 * @return
	 */
	List<ApiLog> queryLog(ApiLog apiLog);
	
	/**
	 * 添加日志
	 * @param apiLog
	 */
	void addLog(ApiLog apiLog);
}
