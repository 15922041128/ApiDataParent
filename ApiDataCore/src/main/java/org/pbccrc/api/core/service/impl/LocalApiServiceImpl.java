package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.core.dao.LocalApiDao;
import org.pbccrc.api.base.service.LocalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalApiServiceImpl implements LocalApiService{

	@Autowired
	private LocalApiDao localApiDao;
	
	public Map<String, Object> queryByService(String service) {
		return localApiDao.queryByService(service);
	}

	public List<Map<String, Object>> queryAll() {
		return localApiDao.queryAll();
	}

}
