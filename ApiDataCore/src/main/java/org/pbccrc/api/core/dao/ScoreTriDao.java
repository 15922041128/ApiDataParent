package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.ScoreTriMapper;
import org.springframework.stereotype.Service;

@Service
public class ScoreTriDao {

	@Resource
	private ScoreTriMapper scoreTriMapper;
	
	/***
	 * 根据内码获取信用分
	 * @return
	 */
	public List<Map<String, Object>> getScore(String innerID) {
		return scoreTriMapper.getScore(innerID);
	}
}
