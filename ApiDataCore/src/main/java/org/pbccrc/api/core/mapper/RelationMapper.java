package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.Relation;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationMapper {

	List<Map<String, Object>> queryAll();
	
	void addRelation(Relation relation);
}
