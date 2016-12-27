package org.pbccrc.api.base.service;

import com.alibaba.fastjson.JSONArray;

public interface TestService {

	/***
	 * 根据产品ID查询产品下所有api
	 * @return
	 */
	JSONArray queryAllApi(String productID);
	
}
