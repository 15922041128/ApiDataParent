package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pbccrc.api.core.dao.RelationDao;
import org.pbccrc.api.base.bean.Relation;
import org.pbccrc.api.base.service.RelationService;
import org.pbccrc.api.base.util.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class RelationServiceImpl implements RelationService{

	@Autowired
	private RelationDao relationDao;
	
	public List<Relation> queryAll() {
		return relationDao.queryAll();
	}
	
	public int addRelation(Relation relation) {
		return relationDao.addRelation(relation);
	}
	
	/**
	 * 根据用户ID和产品ID返回relation对象
	 * @param userID
	 * @param productID
	 * @return
	 */
	public JSONObject getRelation(String userID, String productID) {
		
		// 根据用户ID获取当前用户已购买的产品关系
		List<Map<String, Object>> relationList = RedisClient.fuzzyQuery("relation_" + userID + "_");
		for (Map<String, Object> relation : relationList) {
			// 获取当前用户产品ID
			Set<String> keySet = relation.keySet();
			for (String key : keySet) {
				JSONObject object = JSONObject.parseObject(String.valueOf(relation.get(key)));
				// 获取产品ID
				if (String.valueOf(object.get("productID")).equals(productID)) {
					return object;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 根据用户ID查询该用户所有relation对象
	 * @param userID
	 * @return
	 */
	public JSONArray getRelation(String userID) {
		
		JSONArray jsonArray = new JSONArray();
		
		// 根据用户ID获取当前用户已购买的产品关系
		List<Map<String, Object>> relationList = RedisClient.fuzzyQuery("relation_" + userID + "_");
		
		for (Map<String, Object> relation : relationList) {
			// 获取当前用户产品ID
			Set<String> keySet = relation.keySet();
			for (String key : keySet) {
				JSONObject object = JSONObject.parseObject(String.valueOf(relation.get(key)));
				// 根据产品ID获取产品对象
				String productID = String.valueOf(object.get("productID"));
				JSONObject product = JSONObject.parseObject(String.valueOf(RedisClient.get("product_" + productID)));
				// 将product置入返回对象中
				object.put("product", product);
				jsonArray.add(object);
			}
		}
		
		return jsonArray;
	}
	
	/**
	 * 重置每日访问次数
	 */
	public void resetVisitCount() {
		relationDao.resetVisitCount();
	}

}
