package org.pbccrc.api.web.listener;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pbccrc.api.base.bean.DBEntity;
import org.pbccrc.api.base.service.ApiUserService;
import org.pbccrc.api.base.service.DBOperatorService;
import org.pbccrc.api.base.service.LocalApiService;
import org.pbccrc.api.base.service.RelationService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private LocalApiService localApiService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@Autowired
	private DBOperatorService dbOperatorService;
	
	@Autowired
	private RelationService relationService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		// 将缓存中数据加载到数据库
		sync2DB("apiUser");
		sync2DB("relation");
		
		// 清空缓存中所有数据
		RedisClient.flushAll();
		
		// 将数据库中数据加载到缓存
		// localApi
		List<Map<String, Object>> localApiList = localApiService.queryAll();
		for (Map<String, Object> localApi : localApiList) {
			String key = "localApi" + Constants.UNDERLINE + String.valueOf(localApi.get("ID"));
			RedisClient.set(key, localApi);
		}
		
		// apiUser
		List<Map<String, Object>> apiUserList = apiUserService.queryAll();
		for (Map<String, Object> apiUser : apiUserList) {
			String key = "apiUser" + Constants.UNDERLINE + String.valueOf(apiUser.get("ID"));
			RedisClient.set(key, apiUser);
		}
		
		// relation
		List<Map<String, Object>> relationList = relationService.queryAll();
		for (Map<String, Object> relation : relationList) {
			StringBuilder key = new StringBuilder("relation");
			key.append(Constants.UNDERLINE + String.valueOf(relation.get("userID")));
			key.append(Constants.UNDERLINE + String.valueOf(relation.get("apiKey")));
			key.append(Constants.UNDERLINE + String.valueOf(relation.get("localApiID")));
			RedisClient.set(key.toString(), relation);
		}
	}
	
	// 将缓存中数据加载到数据库
	private void sync2DB(String tableName) {
		// 获取缓存中当前表所有数据
		List<Map<String, Object>> list = RedisClient.fuzzyQuery(tableName);
		// 遍历缓存中所有数据
		for (Map<String, Object> map : list) {
			// 获取key value
			String key = map.keySet().iterator().next();
			String value = String.valueOf(map.get(key));
			JSONObject jsonObject = JSONObject.parseObject(value);
			// 设置DBEntity属性
			DBEntity entity = new DBEntity();
			// 表名
			entity.setTableName(tableName);
			// where条件
			String whereSql = " where ID = " + jsonObject.getString("ID");
			// 更新字段
			StringBuffer sb = new StringBuffer();
			Set<String> keySet = jsonObject.keySet();
			for (String k : keySet) {
				sb.append(k);
				sb.append(Constants.EQUAL);
				sb.append("'" + jsonObject.get(k) + "'");
				sb.append(Constants.COMMA);
			}
			String sql = sb.toString();
			// 删除最后一个逗号
			if (keySet.size() > 1) {
				sql = sql.substring(0, sql.length() - 1);
			}
			entity.setSql(sql + whereSql);
			// 更新数据库
			dbOperatorService.updateData(entity);
		}
	}

}
