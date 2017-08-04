package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.ScoreMapper;
import org.springframework.stereotype.Service;

@Service
public class ScoreDao {

	@Resource
	private ScoreMapper scoreMapper;
	
	/***
	 * 根据内码获取信用分
	 * @return
	 */
	public List<Map<String, Object>> getScore(String innerID) {
		return scoreMapper.getScore(innerID);
	}
	
	/***
	 * 根据内码获取信用分(Score201705)
	 * @return
	 */
	public List<Map<String, Object>> getScore201705(String innerID) {
		return scoreMapper.getScore201705(innerID);
	}
}
