package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface PPersonMapper {

	public Integer isExist(Map<String, String> person);
	
	public Integer addPerson(Map<String, String> person);
	
	public Map<String, Object> selectOne(Map<String, String> person);
}
