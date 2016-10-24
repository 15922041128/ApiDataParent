package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.core.dao.ApiUserDao;
import org.pbccrc.api.base.service.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiUserServiceImpl implements ApiUserService {

	@Autowired
	private ApiUserDao apiUserDao;
	
	public List<Map<String, Object>> queryAll() {
		return apiUserDao.queryAll();
	}

}
