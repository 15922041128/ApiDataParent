package org.pbccrc.api.core.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.GradeMapper;
import org.springframework.stereotype.Service;

@Service
public class GradeDao {
	
	@Resource
	private GradeMapper gradeMapper;
	
	public Map<String, Object> getGrade(String userID, String grade){
		
		Map<String, String> gradeMap = new HashMap<String, String>();
		gradeMap.put("userID", userID);
		gradeMap.put("grade", grade);
		
		return gradeMapper.getGrade(gradeMap);
	}
	
	
}
