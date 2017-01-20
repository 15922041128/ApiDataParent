package org.pbccrc.api.core.service.impl;

import java.util.List;

import org.pbccrc.api.base.bean.QueryType;
import org.pbccrc.api.base.service.QueryTypeService;
import org.pbccrc.api.core.dao.QueryTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryTypeServiceImpl implements QueryTypeService {
	
	@Autowired
	private QueryTypeDao queryTypeDao;

	public List<QueryType> queryAll() {
		return queryTypeDao.queryAll();
	}
	
}
