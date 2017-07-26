package org.pbccrc.api.core.mapper;

import java.util.List;

import org.pbccrc.api.base.bean.ApiUser;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUserMapper {

	List<ApiUser> queryAll();
	
	void addApiUser(ApiUser apiUser);
}
