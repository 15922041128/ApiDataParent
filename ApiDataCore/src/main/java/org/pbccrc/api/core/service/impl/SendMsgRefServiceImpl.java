package org.pbccrc.api.core.service.impl;

import java.util.List;

import org.pbccrc.api.base.bean.SendMsgRef;
import org.pbccrc.api.base.service.SendMsgRefService;
import org.pbccrc.api.core.dao.SendMsgRefDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMsgRefServiceImpl implements SendMsgRefService {
	
	@Autowired
	private SendMsgRefDao sendMsgRefDao;

	public List<SendMsgRef> queryAll() {
		return sendMsgRefDao.queryAll();
	}
	
}
