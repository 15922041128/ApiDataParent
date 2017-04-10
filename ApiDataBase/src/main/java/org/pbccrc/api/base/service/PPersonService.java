package org.pbccrc.api.base.service;

import java.util.Map;

public interface PPersonService {
	
	Map<String, Object> selectOne(Map<String, String> person);
	
	Integer isExist(Map<String, String> person);
	
	Integer addPerson(Map<String, String> person);

}
