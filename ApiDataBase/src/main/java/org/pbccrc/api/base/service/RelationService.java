package org.pbccrc.api.base.service;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.Relation;

import com.alibaba.fastjson.JSONObject;

public interface RelationService {

	List<Map<String, Object>> queryAll();
	
	int addRelation(Relation relation);
	
	/**
	 * 根据用户ID和产品ID返回relation对象
	 * @param userID
	 * @param productID
	 * @return
	 */
	JSONObject getRelation(String userID, String productID);
}
