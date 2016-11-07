package org.pbccrc.api.core.service.impl;

import java.math.BigDecimal;

import org.pbccrc.api.base.service.CostService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class CostServiceImpl implements CostService {
	
	/**
	 * 计费
	 * @param userID
	 * @param apiKey
	 * @param localApiID
	 */
	public void cost(String userID, String apiKey, String localApiID) {
		
		StringBuilder relationKey = new StringBuilder("relation");
		relationKey.append(Constants.UNDERLINE + userID);
		relationKey.append(Constants.UNDERLINE + apiKey);
		relationKey.append(Constants.UNDERLINE + localApiID);
		
		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get(relationKey.toString())));
		String costType = relation.getString("costType");
		
		// 判断计费类型
		if (Constants.COST_TYPE_COUNT.equals(costType)) {
			// 按次数计费
			int count = Integer.parseInt(String.valueOf(relation.get("count")));
			// count为-1免费查询
			if (-1 != count) {
				relation.put("count", --count);
				RedisClient.set(relationKey.toString(), relation);
			}
		} else if (Constants.COST_TYPE_PRICE.equals(costType)) {
			// 按金额计费
			// 获取apiUser
			String apiUserKey = "apiUser" + Constants.UNDERLINE + userID;
			JSONObject apiUser = JSONObject.parseObject(String.valueOf(RedisClient.get(apiUserKey).toString()));
			// 余额
			BigDecimal blance = new BigDecimal(apiUser.getString("blance"));
			// 获取用户价格
			BigDecimal price = new BigDecimal(relation.getString("price"));
			// 从余额中扣除
			blance = blance.subtract(price);
			apiUser.put("blance", blance);
			
			RedisClient.set(apiUserKey.toString(), apiUser);
		} else {
			// to be extended
		}
		
	}
}
