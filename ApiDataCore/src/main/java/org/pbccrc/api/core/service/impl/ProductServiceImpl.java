package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.bean.Product;
import org.pbccrc.api.base.service.ProductService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.core.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductDao productDao;

	/***
	 * 查询所有产品
	 * @return
	 */
	public List<Map<String, Object>> queryAll() {
		return productDao.queryAll();
	}
	
	/***
	 * 根据用户ID查询产品信息
	 * @return
	 */
	public JSONArray queryByUser(String userID) {
		
		JSONArray array = new JSONArray();
		
		// 根据用户ID获取当前用户已购买的产品关系
		List<Map<String, Object>> relationList = RedisClient.fuzzyQuery("relation_" + userID + "_");
		for (Map<String, Object> relation : relationList) {
			// 获取当前用户产品ID
			Set<String> keySet = relation.keySet();
			for (String key : keySet) {
				JSONObject object = JSONObject.parseObject(String.valueOf(relation.get(key)));
				// 获取产品ID
				String productID = String.valueOf(object.get("productID"));
				// 根据产品ID获取产品信息
				JSONObject product = JSONObject.parseObject(String.valueOf(RedisClient.get("product_" + productID)));
				// 将relation信息添加到产品信息中
				product.put("relation", object);
				array.add(product);
			}
		}
		
		return array;  
	}
	
	/***
	 * 根据产品ID获取产品信息
	 * @return
	 */
	public JSONObject getProductInfo(String productID) {
		
		JSONObject productInfo = new JSONObject();
		
		// 根据产品ID获取产品信息
		JSONObject product = JSONObject.parseObject(String.valueOf(RedisClient.get("product_" + productID)));
		productInfo.put("product", product);
		
		// 获取该产品下所有api
		JSONArray apiArray = new JSONArray();
		String[] apiIDArray = product.getString("apis").split(Constants.COMMA);
		for (String apiID : apiIDArray) {
			JSONObject localApi = JSONObject.parseObject(String.valueOf(RedisClient.get("localApi_" + apiID)));
			apiArray.add(localApi);
		}
		productInfo.put("apiArray", apiArray);
		
		// 获取code信息
		JSONArray codeArray = new JSONArray();
		List<Map<String, Object>> codeList = RedisClient.fuzzyQuery("code_");
		
		for (Map<String, Object> code : codeList) {
			
			JSONObject codeJson = new JSONObject();
			
			for (String key : code.keySet()) {
				codeJson.put(key, code.get(key));
			}
			
			codeArray.add(codeJson);
		}
		
		productInfo.put("codeArray", codeArray);
		
		return productInfo;
	}
	
	/**
	 * 根据产品类型获取产品信息
	 * @param productType
	 * @return
	 */
	public JSONArray getProductByType(String productType) {
		
		JSONArray jsonArray = new JSONArray();
		
		List<Product> productList = productDao.getProductByType(productType);
		
		for (Product product : productList) {
			jsonArray.add(product);
		}
		
		return jsonArray;
	}
	
	/**
	 * 更新产品
	 * @param product
	 */
	public void updateProduct(Product product){
		productDao.updateProduct(product);
	}
	
	/**
	 * 条件分页查询产品
	 * @param product
	 * @return
	 */
	public Pagination queryProductByPage(Product product, Pagination pagination){
		return productDao.queryProductByPage(product, pagination);
	}
	
	/**
	 * 新增产品
	 * @param product
	 */
	public void addProduct(Product product){
		productDao.addProduct(product);
	}
	
	/**
	 * 查询该产品所有 api
	 * @param productID
	 * @return
	 */
	public JSONArray getApiArray(String productID) {
		
		JSONArray apiArray = new JSONArray();
		
		// 根据产品ID获取产品信息
		JSONObject product = JSONObject.parseObject(String.valueOf(RedisClient.get("product_" + productID)));
		String[] apis = product.getString("apis").split(Constants.COMMA);
		for (String apiID : apis) {
			apiArray.add(JSONObject.parseObject(String.valueOf(RedisClient.get("localApi_" + apiID))));
		}
		
		return apiArray;
	}
}
