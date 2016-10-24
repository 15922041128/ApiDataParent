package org.pbccrc.api.core.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.ZhInsideCodeMapper;
import org.springframework.stereotype.Service;

@Service
public class ZhInsideCodeDao {
	
	@Resource
	private ZhInsideCodeMapper zhInsideCodeMapper;
	
	public Map<String, Object> queryByCode(String identifier){
		return zhInsideCodeMapper.queryByIdentifier(identifier);
	}
	
}
