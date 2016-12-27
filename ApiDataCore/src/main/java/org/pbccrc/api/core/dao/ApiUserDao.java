package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.ApiUser;
import org.pbccrc.api.core.mapper.ApiUserMapper;
import org.springframework.stereotype.Service;

@Service
public class ApiUserDao {

	@Resource
	private ApiUserMapper apiUserMapper;
	
	public List<Map<String, Object>> queryAll() {
		return apiUserMapper.queryAll();
	}
	
	public void addApiUser(ApiUser apiUser) {
		apiUserMapper.addApiUser(apiUser);
	}
}
