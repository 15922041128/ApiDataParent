package org.pbccrc.api.core.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.BorrowDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class BorrowDetailDao {
	
	@Resource
	private BorrowDetailMapper borrowDetailMapper;

	public Map<String, Object> getBorrowDetail(Map<String, Object> param) {
		return borrowDetailMapper.getBorrowDetail(param);
	}
}
