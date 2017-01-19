package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.ZhPersonMapper;
import org.springframework.stereotype.Service;

@Service
public class ZhPersonDao {
	
	@Resource
	private ZhPersonMapper zhPersonMapper;
	
	public List<Map<String, Object>> query(String innerID){
		return zhPersonMapper.query(innerID);
	}
	
}
