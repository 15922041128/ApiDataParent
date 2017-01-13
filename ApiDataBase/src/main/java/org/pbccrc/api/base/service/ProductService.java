package org.pbccrc.api.base.service;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.bean.Product;

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
	
	/**
	 * 更新产品
	 * @param product
	 */
	public void updateProduct(Product product);
	
	/**
	 * 条件分页查询产品
	 * @param product
	 * @return
	 */
	Pagination queryProductByPage(Product product, Pagination pagination);
	
	/**
	 * 新增产品
	 * @param product
	 */
	void addProduct(Product product);
	
	/**
	 * 查询该产品所有 api
	 * @param productID
	 * @return
	 */
	JSONArray getApiArray(String productID);
}
