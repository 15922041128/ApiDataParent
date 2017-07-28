package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.Borrow;
import org.pbccrc.api.core.mapper.BorrowMapper;
import org.springframework.stereotype.Service;

@Service
public class BorrowDao {
	
	@Resource
	private BorrowMapper borrowMapper;
	
	public String getNexSeq() {
		return borrowMapper.getNexSeq();
	}
	
	public void addBorrows(List<Borrow> borrows) {
		borrowMapper.addBorrows(borrows);
	}
	
	public int getCreditModel(Map<String, Object> map) {
		borrowMapper.getCreditModel(map);
		return Integer.parseInt(String.valueOf(map.get("score")));
	}
	
}
