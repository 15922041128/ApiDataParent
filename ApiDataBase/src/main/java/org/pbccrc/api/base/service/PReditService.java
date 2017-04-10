package org.pbccrc.api.base.service;

import java.util.List;
import java.util.Map;

public interface PReditService {

	List<Map<String, Object>> queryAll(String personID);
	
	Integer addPRedit(Map<String, Object> pRedit);
}
