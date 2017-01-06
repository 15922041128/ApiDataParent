package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ScoreMapper {

	/***
	 * 根据内码获取信用分数
	 * @return
	 */
	List<Map<String, Object>> getScore(String innerID);
	
	
}
