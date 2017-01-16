package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.ZhLoanMapper;
import org.springframework.stereotype.Service;

@Service
public class ZhLoanDao {
	
	@Resource
	private ZhLoanMapper zhLoanMapper;
	
	public List<Map<String, Object>> query(String innerID){
		return zhLoanMapper.query(innerID);
	}
	
}
