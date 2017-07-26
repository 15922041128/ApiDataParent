package org.pbccrc.api.base.service;

import java.util.List;

import org.pbccrc.api.base.bean.ApiUser;

public interface ApiUserService {

	List<ApiUser> queryAll();
}
