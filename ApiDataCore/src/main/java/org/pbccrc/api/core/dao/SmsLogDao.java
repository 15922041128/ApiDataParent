package org.pbccrc.api.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.SmsLog;
import org.pbccrc.api.core.mapper.SmsLogMapper;
import org.springframework.stereotype.Service;

@Service
public class SmsLogDao{
	
	@Resource
	private SmsLogMapper smsLogMapper;
	
	public List<SmsLog> queryLog(SmsLog smsLog) {
		return smsLogMapper.queryLog(smsLog);
	}
	
	public void addLog(SmsLog smsLog) {
		smsLogMapper.addLog(smsLog);
	}
	
}
