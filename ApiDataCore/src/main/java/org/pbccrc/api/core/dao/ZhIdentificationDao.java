package org.pbccrc.api.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.mapper.ZhIdentificationMapper;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service
public class ZhIdentificationDao {
	
	@Resource
	private ZhIdentificationMapper zhIdentificationMapper;
	
	public Map<String, Object> getInnerIDWithoutMD5(String name, String identifier){
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("identifier", identifier);
		
		List<Map<String, Object>> mapList = zhIdentificationMapper.getInnerID(map);
		
		return getMaxInnerID(mapList);
	}
	
	public Map<String, Object> getInnerID(String name, String identifier){
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", StringUtil.MD5Encoder(name).toUpperCase());
		map.put("identifier", StringUtil.MD5Encoder(identifier).toUpperCase());
		
		List<Map<String, Object>> mapList = zhIdentificationMapper.getInnerID(map);
		return getMaxInnerID(mapList);
	}
	
	public List<Map<String, Object>> getInnerID(String identifier){
		Map<String, String> map = new HashMap<String, String>();
		map.put("identifier", StringUtil.MD5Encoder(identifier).toUpperCase());
		List<Map<String, Object>> mapList = zhIdentificationMapper.getInnerID(map);
		
		if (null == mapList || mapList.size() == 0) {
			return null;
		}
		return mapList;
	}
	
	public List<Map<String, Object>> getInnerIDByMd5(String identifier){
		Map<String, String> map = new HashMap<String, String>();
		map.put("identifier", identifier.toUpperCase());
		List<Map<String, Object>> mapList = zhIdentificationMapper.getInnerID(map);
		if (null == mapList || mapList.size() == 0) {
			return null;
		}
		return mapList;
	}
	
	/**
	 * 单挑内码的情况下,若身份证为空,则返回null,不为空则直接返回
	 * 多条内码的情况下,返回最innerID最大的内码
	 * 多条内码的情况下,若所有记录都无数据(身份证),则返回null
	 * @param mapList
	 * @return
	 */
	private Map<String, Object> getMaxInnerID(List<Map<String, Object>> mapList) {
		
		Map<String, Object> returnMap = null;
		
		if (null == mapList || mapList.size() == 0) {
			return returnMap;
		}
		
		// 判断是否返回多条数据
		if (1 == mapList.size()) {
			// 将该记录转为json,并判断身份证号码是否为空
			Map<String, Object> tMap = mapList.get(0);
			JSONObject json = JSONObject.parseObject(JSON.toJSONString(tMap));
			String v_identifier = json.getString("IDENTIFIER");
			// 判断身份证号是否为空
			if (!StringUtil.isNull(v_identifier)) {
				// 若不为空,则返回该记录
				returnMap = mapList.get(0);
			}
			
		} else {
			// 多条记录进行判断
			// 如果所有记录都有数据,则返回innerID最大的记录
			// 如果有没有数据的,则返回有数据的
			// 如果都没有数据,则返回null
			String maxInnerID = Constants.BLANK;
			Map<String, Object> maxMap = null;
			
			String currentInnerID = Constants.BLANK;
			
			for (int i = 0; i < mapList.size(); i++) {
				Map<String, Object> tMap = mapList.get(i);
				
				// 将map转为json
				JSONObject json = JSONObject.parseObject(JSON.toJSONString(tMap));
				String v_innerid = json.getString("INNERID");
				String v_identifier = json.getString("IDENTIFIER");
				
				// 判断身份证号是否为空
				if (StringUtil.isNull(v_identifier)) {
					continue;
				} else {
					
					currentInnerID = v_innerid;
					
					// 判断maxInnerID是否为空
					if (StringUtil.isNull(maxInnerID)) {
						// 如果为空则直接将当前innerID赋为maxInnerID
						maxInnerID = currentInnerID;
						maxMap = tMap;
					} else {
						// 如果不为空,则进行比较
						// 如果当前innerID大于maxInnerID,则将innerID赋为maxInnerID
						if (Integer.parseInt(currentInnerID) > Integer.parseInt(maxInnerID)) {
							maxInnerID = currentInnerID;
							maxMap = tMap;
						}
					}
				}
			}
			
			returnMap = maxMap;
		}
		
		return returnMap;
	}
}
