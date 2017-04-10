package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface PBaseInfoMapper {

	public Integer isExist(String personID);
	
	public Integer addPBaseInfo(Map<String, Object> pBaseInfo);
	
	public Integer updatePBaseInfo(Map<String, Object> pBaseInfo);
	
	public List<Map<String, Object>> queryByPersonID(String personID);
}
