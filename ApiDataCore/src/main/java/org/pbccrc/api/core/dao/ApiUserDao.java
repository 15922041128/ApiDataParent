package org.pbccrc.api.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.ApiUser;
import org.pbccrc.api.core.mapper.ApiUserMapper;
import org.springframework.stereotype.Service;

@Service
public class ApiUserDao {

	@Resource
	private ApiUserMapper apiUserMapper;
	
	public List<ApiUser> queryAll() {
		return apiUserMapper.queryAll();
	}
	
	public void addApiUser(ApiUser apiUser) {
		apiUserMapper.addApiUser(apiUser);
	}
}
