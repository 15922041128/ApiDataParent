package org.pbccrc.api.core.dao;

import java.util.Map;

import org.pbccrc.api.core.mapper.PPersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PPersonDao {
	
	@Autowired
	private PPersonMapper pPersonMapper;
	
	public Integer isExist(Map<String, String> person) {
		return pPersonMapper.isExist(person);
	}
	
	public Integer addPerson(Map<String, String> person) {
		return pPersonMapper.addPerson(person);
	}
	
	public Map<String, Object> selectOne(Map<String, String> person) {
		return pPersonMapper.selectOne(person);
	}

}
