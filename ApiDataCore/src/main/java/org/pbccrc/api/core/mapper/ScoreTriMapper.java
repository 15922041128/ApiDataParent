package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ScoreTriMapper {

	/***
	 * 根据内码获取信用分数(TRI)
	 * @return
	 */
	List<Map<String, Object>> getScore(String innerID);
	
	
}
