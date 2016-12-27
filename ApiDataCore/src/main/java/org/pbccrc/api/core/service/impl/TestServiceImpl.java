package org.pbccrc.api.core.service.impl;

import org.pbccrc.api.base.service.TestService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class TestServiceImpl implements TestService{

	/***
	 * 根据产品ID查询产品下所有api
	 * @return
	 */
	public JSONArray queryAllApi(String productID) {
		
		JSONArray jsonArray = new JSONArray();
		
		// 根据产品ID获取产品信息
		JSONObject product = JSONObject.parseObject(String.valueOf(RedisClient.get("product_" + productID)));
		// 获取产品apiID
		String[] apiIDArray = product.getString("apis").split(Constants.COMMA);
		// 根据apiID获取所有api
		for (String apiID : apiIDArray) {
			JSONObject localApi = JSONObject.parseObject(String.valueOf(RedisClient.get("localApi_" + apiID)));
			jsonArray.add(localApi);
		}
		
		return jsonArray;
	}
	
}
