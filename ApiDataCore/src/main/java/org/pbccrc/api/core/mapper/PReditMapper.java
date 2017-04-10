package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface PReditMapper {

	public Integer addPRedit(Map<String, Object> pRedit);
	
	public List<Map<String, Object>> queryAll(String personID);
	
}
