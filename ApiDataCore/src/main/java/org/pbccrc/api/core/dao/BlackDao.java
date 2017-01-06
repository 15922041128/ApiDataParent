package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.BlackMapper;
import org.springframework.stereotype.Service;

@Service
public class BlackDao {

	@Resource
	private BlackMapper blackMapper;
	
	/***
	 * 根据内码获取黑名单
	 * @return
	 */
	public List<Map<String, Object>> getBlack(String innerID) {
		return blackMapper.getBlack(innerID);
	}
}
