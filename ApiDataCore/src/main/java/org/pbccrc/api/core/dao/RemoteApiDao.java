package org.pbccrc.api.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.RemoteApiMapper;
import org.springframework.stereotype.Service;

@Service
public class RemoteApiDao {
	
	@Resource
	private RemoteApiMapper remoteApiMapper;
	
	public List<Map<String, Object>> getRemoteApiByLocal(int localApiID){
		return remoteApiMapper.getRemoteApiByLocal(localApiID);
	}
	
	public int updateCnt(int id, int count){
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ID", id);
		param.put("count", count);
		
		return remoteApiMapper.updateCnt(id, count);
	}
	
	public Map<String, Object> getKey(){
		
		return remoteApiMapper.getKey();
	}

}
