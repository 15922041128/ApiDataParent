package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface TelPersonMapper {

	List<Map<String, Object>> getInnerID(String telNum);
	
	List<Map<String, Object>> getTelPerson(String innerID);
}
