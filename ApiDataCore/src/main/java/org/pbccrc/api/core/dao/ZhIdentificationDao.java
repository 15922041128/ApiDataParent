package org.pbccrc.api.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.mapper.ZhIdentificationMapper;
import org.springframework.stereotype.Service;

@Service
public class ZhIdentificationDao {
	
	@Resource
	private ZhIdentificationMapper zhIdentificationMapper;
	
	public Map<String, Object> getInnerID(String name, String identifier){
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", StringUtil.MD5Encoder(name).toUpperCase());
		map.put("identifier", StringUtil.MD5Encoder(identifier).toUpperCase());
		
		List<Map<String, Object>> mapList = zhIdentificationMapper.getInnerID(map);
		if (null == mapList || mapList.size() == 0) {
			return null;
		}
		return mapList.get(0);
	}
	
	public Map<String, Object> getInnerID(String identifier){
		Map<String, String> map = new HashMap<String, String>();
		map.put("identifier", StringUtil.MD5Encoder(identifier).toUpperCase());
		List<Map<String, Object>> mapList = zhIdentificationMapper.getInnerID(map);
		if (null == mapList || mapList.size() == 0) {
			return null;
		}
		return mapList.get(0);
	}
	
}
