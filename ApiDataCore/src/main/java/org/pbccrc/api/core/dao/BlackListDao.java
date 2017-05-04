package org.pbccrc.api.core.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.BlackListMapper;
import org.springframework.stereotype.Service;

@Service
public class BlackListDao {
	
	@Resource
	private BlackListMapper blackListMapper;
	
	public Map<String, Object> getList(String innerId){
		
		return blackListMapper.getList(innerId);
	}
	
	
}
