package org.pbccrc.api.base.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface ProductService {

	/***
	 * 查询所有产品
	 * @return
	 */
	List<Map<String, Object>> queryAll();
	
	/***
	 * 根据用户ID查询产品信息
	 * @return
	 */
	JSONArray queryByUser(String userID);
	
	/***
	 * 根据产品ID获取产品信息
	 * @return
	 */
	JSONObject getProductInfo(String productID);
	
	/**
	 * 根据产品类型获取产品信息
	 * @param productType
	 * @return
	 */
	JSONArray getProductByType(String productType);
}
