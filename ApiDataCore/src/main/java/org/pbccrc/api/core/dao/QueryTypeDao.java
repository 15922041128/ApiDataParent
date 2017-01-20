package org.pbccrc.api.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.QueryType;
import org.pbccrc.api.core.mapper.QueryTypeMapper;
import org.springframework.stereotype.Service;

@Service
public class QueryTypeDao {
	
	@Resource
	private QueryTypeMapper queryTypeMapper;
	
	public List<QueryType> queryAll() {
		return queryTypeMapper.queryAll();
	}
}
