package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.TelPersonMapper;
import org.springframework.stereotype.Service;

@Service
public class TelPersonDao {
	
	@Resource
	private TelPersonMapper telPersonMapper;
	
	public Map<String, Object> getInnerID(String telNum){
		
		List<Map<String, Object>> mapList = telPersonMapper.getInnerID(telNum);
		if (null == mapList || mapList.size() == 0) {
			return null;
		}
		return mapList.get(0);
	}
	
	public Map<String, Object> getTelPerson(String innerID){
		
		List<Map<String, Object>> mapList = telPersonMapper.getTelPerson(innerID);
		if (null == mapList || mapList.size() == 0) {
			return null;
		}
		return mapList.get(0);
	}
}
