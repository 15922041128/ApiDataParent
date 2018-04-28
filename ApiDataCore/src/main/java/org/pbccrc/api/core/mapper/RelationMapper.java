package org.pbccrc.api.core.mapper;

import java.util.List;

import org.pbccrc.api.base.bean.Relation;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationMapper {

	List<Relation> queryAll();
	
	void addRelation(Relation relation);
	
}
