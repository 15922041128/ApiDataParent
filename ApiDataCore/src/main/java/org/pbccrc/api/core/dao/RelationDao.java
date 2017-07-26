package org.pbccrc.api.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.Relation;
import org.pbccrc.api.core.mapper.RelationMapper;
import org.springframework.stereotype.Service;

@Service
public class RelationDao {

	@Resource
	private RelationMapper relationMapper;
	
	public List<Relation> queryAll() {
		return relationMapper.queryAll();
	}
	
	public int addRelation(Relation relation){
		relationMapper.addRelation(relation);
		return relation.getId();
	}
}
