package org.pbccrc.api.base.service;

import java.util.List;
import java.util.Map;

public interface PBaseInfoService {

	List<Map<String, Object>> queryByPersonID(String personID);
	
	Integer addPBaseInfo(Map<String, Object> pBaseInfo);
}
