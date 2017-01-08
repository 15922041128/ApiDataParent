package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface BlackMapper {

	/***
	 * 根据内码获取黑名单
	 * @return
	 */
	List<Map<String, Object>> getBlack(String innerID);
	
	
}
