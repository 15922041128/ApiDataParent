package org.pbccrc.api.core.task;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pbccrc.api.base.service.RelationService;
import org.pbccrc.api.base.util.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * 每日重置relation.visitCount
 * @author Administrator
 *
 */
@Service
public class VisitCountTask {
	
	@Autowired
	private RelationService relationService;

	@Scheduled(cron = "59 59 23 * * ?")
	public void excute() {      
		// 重置数据库
		relationService.resetVisitCount();
		// 重置缓存
		// 获取所有relation
		List<Map<String, Object>> relationList = RedisClient.fuzzyQuery("relation_");
		for (Map<String, Object> relation : relationList) {
			
			Set<String> keySet = relation.keySet();
			
			for (String key : keySet) {
				JSONObject relationObj = JSONObject.parseObject(String.valueOf(relation.get(key)));
				relationObj.put("visitCount", 1000);
				RedisClient.set(key, relationObj);
			}
		}
	}      
}
