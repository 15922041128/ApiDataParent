package org.pbccrc.api.core.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pbccrc.api.base.bean.Relation;
import org.pbccrc.api.base.service.ApplyService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.dao.RelationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class ApplyServiceImpl implements ApplyService {
	
	@Autowired
	private RelationDao relationDao;

	/**
	 * 申请试用
	 * @param userID
	 * @param productID
	 * @return
	 */
	public boolean doApply(String userID, String productID) {
		
		boolean isSuccess = true;
		
		// 根据用户ID获取当前用户已购买的产品关系
		List<Map<String, Object>> relationList = RedisClient.fuzzyQuery("relation_" + userID + "_");
		// 查看当前用户是否有当前产品
		for (Map<String, Object> relation : relationList) {
			Set<String> keySet = relation.keySet();
			for (String key : keySet) {
				JSONObject object = JSONObject.parseObject(String.valueOf(relation.get(key)));
				// 获取产品ID
				String currentProductID = String.valueOf(object.get("productID"));
				if (productID.equals(currentProductID)) {
					// 如果已有当前产品则返回失败
					isSuccess = false;
					return isSuccess;
				}
			}
		}
		
		// 根据产品ID获取产品信息
		JSONObject product = JSONObject.parseObject(String.valueOf(RedisClient.get("product_" + productID)));
		
		// 获取免费使用次数
		int initCount = product.getInteger("initCount");
		// 生成apiKey
		String apiKey = StringUtil.createApiKey();
		// createDate
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String createDate = format.format(new Date());
		
		// TODO 价格
		BigDecimal price = new BigDecimal("0.00");
		
		// DB relation对象
		Relation dbRelation = new Relation();
		dbRelation.setApiKey(apiKey);
		dbRelation.setUserID(userID);
		dbRelation.setProductID(productID);
		dbRelation.setCount(String.valueOf(initCount));
		dbRelation.setCostType(Constants.COST_TYPE_COUNT);
		dbRelation.setPrice(String.valueOf(price));
		dbRelation.setCreateDate(createDate);
		int relationID = relationDao.addRelation(dbRelation);
		
		// redis relation对象
		JSONObject relation = new JSONObject();
		relation.put("apiKey", apiKey);
		relation.put("productID", productID);
		relation.put("price", price);
		relation.put("costType", Constants.COST_TYPE_COUNT);
		relation.put("count", initCount);
		relation.put("ID", relationID);
		relation.put("userID", userID);
		relation.put("createDate", createDate);
		
		String redisKey = "relation_" + userID + "_" + apiKey;
		RedisClient.set(redisKey, relation);
		
		return isSuccess;
	}
}
