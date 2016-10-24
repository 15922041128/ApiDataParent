package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.RelationMapper;
import org.springframework.stereotype.Service;

@Service
public class RelationDao {

	@Resource
	private RelationMapper relationMapper;
	
	public List<Map<String, Object>> queryAll() {
		return relationMapper.queryAll();
	}
}
