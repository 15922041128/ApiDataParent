package org.pbccrc.api.core.service.impl;

import java.util.List;

import org.pbccrc.api.base.bean.ApiUser;
import org.pbccrc.api.base.service.ApiUserService;
import org.pbccrc.api.core.dao.ApiUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiUserServiceImpl implements ApiUserService {

	@Autowired
	private ApiUserDao apiUserDao;
	
	public List<ApiUser> queryAll() {
		return apiUserDao.queryAll();
	}

}
