package org.pbccrc.api.core.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.LdbMapper;
import org.springframework.stereotype.Service;

@Service
public class LdbApiDao {
	
	@Resource
	private LdbMapper ldbMapper;
	
	public Map<String, Object> queryByService(String service){
		return ldbMapper.queryByService(service);
	}
	
}
