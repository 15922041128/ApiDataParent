package org.pbccrc.api.core.mapper;

import java.util.List;

import org.pbccrc.api.base.bean.CheckMobile;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckMobileMapper {

	public List<CheckMobile> queryCheckMobile(List<String> list);
	
	public void addCheckMobile(CheckMobile checkMobile);
	
	public CheckMobile queryCheckMobileOne(String mobile);
	
}
