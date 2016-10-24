package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.ZhGuaranteeMapper;
import org.springframework.stereotype.Service;

@Service
public class ZhGuaranteeDao {
	
	@Resource
	private ZhGuaranteeMapper zhGuaranteeMapper;
	
	public List<Map<String, Object>> query(String insideCode){
		return zhGuaranteeMapper.query(insideCode);
	}
	
}
