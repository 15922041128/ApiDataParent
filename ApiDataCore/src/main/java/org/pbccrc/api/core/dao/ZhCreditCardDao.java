package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.ZhCreditCardMapper;
import org.springframework.stereotype.Service;

@Service
public class ZhCreditCardDao {
	
	@Resource
	private ZhCreditCardMapper zhCreditCardMapper;
	
	public List<Map<String, Object>> query(String innerID){
		return zhCreditCardMapper.query(innerID);
	}
	
}
