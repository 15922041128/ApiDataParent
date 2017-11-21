package org.pbccrc.api.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.SendMsgRef;
import org.pbccrc.api.core.mapper.SendMsgRefMapper;
import org.springframework.stereotype.Service;

@Service
public class SendMsgRefDao {
	
	@Resource
	private SendMsgRefMapper sendMsgRefMapper;
	
	public List<SendMsgRef> queryAll() {
		return sendMsgRefMapper.queryAll();
	}
}
