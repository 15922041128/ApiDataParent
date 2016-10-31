package org.pbccrc.api.core.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.ZhEmploymentMapper;
import org.springframework.stereotype.Service;

@Service
public class ZhEmploymentDao {
	
	@Resource
	private ZhEmploymentMapper zhEmploymentMapper;
	
	public Map<String, Object> query(String innerID){
		return zhEmploymentMapper.query(innerID);
	}
	
}
