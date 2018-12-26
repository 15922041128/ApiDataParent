package org.pbccrc.api.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.CheckMobile;
import org.pbccrc.api.core.mapper.CheckMobileMapper;
import org.springframework.stereotype.Service;

@Service
public class CheckMobileDao{
	
	@Resource
	private CheckMobileMapper checkMobileMapper;
	
	public List<CheckMobile> queryLog(List<String> list) {
		return checkMobileMapper.queryCheckMobile(list);
	}
	
	public void addCheckMobile(CheckMobile checkMobile) {
		checkMobileMapper.addCheckMobile(checkMobile);;
	}
	
	public CheckMobile queryCheckMobileOne(String mobile) {
		return checkMobileMapper.queryCheckMobileOne(mobile);
	}
	
}
