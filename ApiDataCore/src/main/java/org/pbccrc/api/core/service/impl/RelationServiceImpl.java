package org.pbccrc.api.core.service.impl;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.core.dao.RelationDao;
import org.pbccrc.api.base.bean.Relation;
import org.pbccrc.api.base.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationServiceImpl implements RelationService{

	@Autowired
	private RelationDao relationDao;
	
	public List<Map<String, Object>> queryAll() {
		return relationDao.queryAll();
	}
	
	public int addRelation(Relation relation) {
		return relationDao.addRelation(relation);
	}

}
