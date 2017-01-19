package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.Code;
import org.pbccrc.api.base.service.CodeService;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.core.dao.CodeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class CodeServiceImpl implements CodeService {
	
	@Autowired
	private CodeDao codeDao;

	public List<Code> queryAll() {
		return codeDao.queryAll();
	}
	
	public JSONArray queryAllInMemory() {
		
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
		
		return codeArray;
	}
}
