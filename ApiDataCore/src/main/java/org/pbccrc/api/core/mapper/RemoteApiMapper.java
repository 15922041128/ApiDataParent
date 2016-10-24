package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface RemoteApiMapper {

	public List<Map<String, Object>> getRemoteApiByLocal(int localApiID);
	
	public int updateCnt(int id, int count);
	
	public Map<String, Object> getKey();
}
