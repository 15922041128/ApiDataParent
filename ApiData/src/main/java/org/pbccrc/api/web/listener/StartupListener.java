package org.pbccrc.api.web.listener;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pbccrc.api.base.bean.ApiUser;
import org.pbccrc.api.base.bean.Code;
import org.pbccrc.api.base.bean.DBEntity;
import org.pbccrc.api.base.bean.LocalApi;
import org.pbccrc.api.base.bean.Product;
import org.pbccrc.api.base.bean.Relation;
import org.pbccrc.api.base.bean.SendMsgRef;
import org.pbccrc.api.base.service.ApiUserService;
import org.pbccrc.api.base.service.CodeService;
import org.pbccrc.api.base.service.DBOperatorService;
import org.pbccrc.api.base.service.LocalApiService;
import org.pbccrc.api.base.service.ProductService;
import org.pbccrc.api.base.service.RelationService;
import org.pbccrc.api.base.service.SendMsgRefService;
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
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private SendMsgRefService sendMsgRefService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event){

		// 将缓存中数据加载到数据库
		sync2DB("apiUser");
		sync2DB("relation");
		
		// 清空缓存中所有数据
		RedisClient.flushAll();
		
		// 将数据库中数据加载到缓存
		// product
		List<Product> productList = productService.queryAll();
		for (Product product : productList) {
			String key = "product" + Constants.UNDERLINE + String.valueOf(product.getId());
			RedisClient.set(key, product);
		}
		
		// localApi
		List<LocalApi> localApiList = localApiService.queryAll();
		for (LocalApi localApi : localApiList) {
			String key = "localApi" + Constants.UNDERLINE + String.valueOf(localApi.getId());
			RedisClient.set(key, localApi);
		}
		
		// apiUser
		List<ApiUser> apiUserList = apiUserService.queryAll();
		for (ApiUser apiUser : apiUserList) {
			String key = "apiUser" + Constants.UNDERLINE + String.valueOf(apiUser.getId());
			RedisClient.set(key, apiUser);
		}
		
		// relation
		List<Relation> relationList = relationService.queryAll();
		for (Relation relation : relationList) {
			StringBuilder key = new StringBuilder("relation");
			key.append(Constants.UNDERLINE + String.valueOf(relation.getUserID()));
			key.append(Constants.UNDERLINE + String.valueOf(relation.getApiKey()));
			RedisClient.set(key.toString(), relation);
		}
		
		// code
		List<Code> codeList = codeService.queryAll();
		for (Code code : codeList) {
			RedisClient.set("code_" + code.getId(), code);
		}
		
		// send_msg_ref
		List<SendMsgRef> sendMsgRefList = sendMsgRefService.queryAll();
		for (SendMsgRef sendMsgRef : sendMsgRefList) {
			RedisClient.set("sendMsgRef_" + sendMsgRef.getType(), sendMsgRef);
		}
		
		// temp xinyan count 
		RedisClient.set("xinyanCount", 2000);
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
			String whereSql = " where id = '" + jsonObject.getString("id") + "'";
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
