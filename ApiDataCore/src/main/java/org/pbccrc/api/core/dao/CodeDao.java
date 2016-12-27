package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.Code;
import org.pbccrc.api.core.mapper.CodeMapper;
import org.springframework.stereotype.Service;

@Service
public class CodeDao {
	
	@Resource
	private CodeMapper codeMapper;
	
	public Map<String, Object> queryByCode(String codeName){
		return codeMapper.queryByCode(codeName);
	}
	
	public List<Code> queryAll() {
		return codeMapper.queryAll();
	}
}
