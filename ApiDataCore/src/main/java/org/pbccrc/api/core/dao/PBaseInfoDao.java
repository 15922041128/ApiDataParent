package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.core.mapper.PBaseInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PBaseInfoDao {
	
	@Autowired
	private PBaseInfoMapper pBaseInfoMapper;

	public Integer isExist(String personID) {
		return pBaseInfoMapper.isExist(personID);
	}
	
	public Integer addPBaseInfo(Map<String, Object> pBaseInfo) {
		return pBaseInfoMapper.addPBaseInfo(pBaseInfo);
	}
	
	public Integer updatePBaseInfo(Map<String, Object> pBaseInfo) {
		return pBaseInfoMapper.updatePBaseInfo(pBaseInfo);
	}
	
	public List<Map<String, Object>> queryByPersonID(String personID) {
		return pBaseInfoMapper.queryByPersonID(personID);
	}
}
