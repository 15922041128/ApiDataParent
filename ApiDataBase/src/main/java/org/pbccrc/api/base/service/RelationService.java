package org.pbccrc.api.base.service;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.Relation;

public interface RelationService {

	List<Map<String, Object>> queryAll();
	
	int addRelation(Relation relation);
}
