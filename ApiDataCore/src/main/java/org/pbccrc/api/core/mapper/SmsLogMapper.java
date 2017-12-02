package org.pbccrc.api.core.mapper;

import java.util.List;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.bean.SmsLog;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsLogMapper {

	/**
	 * 查询日志
	 * @param apiLog
	 * @return
	 */
	List<SmsLog> queryLog(SmsLog smsLog);
	
	/**
	 * 添加日志
	 * @param apiLog
	 */
	void addLog(SmsLog smsLog);
}
