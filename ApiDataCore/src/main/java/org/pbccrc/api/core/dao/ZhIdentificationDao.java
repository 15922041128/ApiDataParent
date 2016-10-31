package org.pbccrc.api.core.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.ZhIdentificationMapper;
import org.springframework.stereotype.Service;

@Service
public class ZhIdentificationDao {
	
	@Resource
	private ZhIdentificationMapper zhIdentificationMapper;
	
	public Map<String, Object> queryByCode(String identifier){
		return zhIdentificationMapper.queryByIdentifier(identifier);
	}
	
}
