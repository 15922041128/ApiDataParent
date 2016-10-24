package org.pbccrc.api.core.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.ZhCreditCardMapper;
import org.springframework.stereotype.Service;

@Service
public class ZhCreditCardDao {
	
	@Resource
	private ZhCreditCardMapper zhCreditCardMapper;
	
	public Map<String, Object> query(String insideCode){
		return zhCreditCardMapper.query(insideCode);
	}
	
}
