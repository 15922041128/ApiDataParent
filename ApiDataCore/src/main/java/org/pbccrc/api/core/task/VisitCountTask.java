package org.pbccrc.api.core.task;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pbccrc.api.base.util.RedisClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * 每日重置relation.dailyQueryCount
 * set relation.dailyQueryCount = relation.visitCount
 * @author Administrator
 *
 */
@Service
public class VisitCountTask {
	
	@Scheduled(cron = "59 59 23 * * ?")
	public void excute() {      
		// 重置缓存
		// 获取所有relation
		List<Map<String, Object>> relationList = RedisClient.fuzzyQuery("relation_");
		for (Map<String, Object> relation : relationList) {
			
			Set<String> keySet = relation.keySet();
			
			for (String key : keySet) {
				JSONObject relationObj = JSONObject.parseObject(String.valueOf(relation.get(key)));
				relationObj.put("dailyQueryCount", relationObj.get("visitCount"));
				RedisClient.set(key, relationObj);
			}
		}
	}      
}
