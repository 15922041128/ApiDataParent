package org.pbccrc.api.core.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.WhiteListMapper;
import org.springframework.stereotype.Service;

@Service
public class WhiteListDao {
	
	@Resource
	private WhiteListMapper whiteListMapper;
	
	public Map<String, Object> getList(String innerId){
		
		return whiteListMapper.getList(innerId);
	}
	
	
}
