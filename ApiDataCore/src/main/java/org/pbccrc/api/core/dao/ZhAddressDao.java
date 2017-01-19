package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.ZhAddressMapper;
import org.springframework.stereotype.Service;

@Service
public class ZhAddressDao {
	
	@Resource
	private ZhAddressMapper zhAddressMapper;
	
	public List<Map<String, Object>> query(String innerID){
		return zhAddressMapper.query(innerID);
	}
	
}
