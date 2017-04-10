package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.PReditMapper;
import org.springframework.stereotype.Service;

@Service
public class PReditDao {
	
	@Resource
	private PReditMapper pReditMapper;
	
	public Integer addPRedit(Map<String, Object> pRedit) {
		return pReditMapper.addPRedit(pRedit);
	}
	
	public List<Map<String, Object>> queryAll(String personID) {
		return pReditMapper.queryAll(personID);
	}

}
