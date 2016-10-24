package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.LocalApiMapper;
import org.springframework.stereotype.Service;

@Service
public class LocalApiDao {
	
	@Resource
	private LocalApiMapper localApiMapper;
	
	public Map<String, Object> queryByService(String service){
		return localApiMapper.queryByService(service);
	}
	
	public List<Map<String, Object>> queryAll() {
		return localApiMapper.queryAll();
	}
	
}
