package org.pbccrc.api.core.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.BhyhMapper;
import org.springframework.stereotype.Service;

@Service
public class BhyhDao {
	
	@Resource
	private BhyhMapper bhyhMapper;
	
	public Map<String, Object> getList(String innerId){
		
		return bhyhMapper.getBhyh(innerId);
	}
	
	
}
