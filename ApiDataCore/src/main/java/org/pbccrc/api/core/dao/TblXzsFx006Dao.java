package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblXzsFx006;
import org.pbccrc.api.core.mapper.TblXzsFx006Mapper;
import org.springframework.stereotype.Service;

@Service
public class TblXzsFx006Dao {
	
	@Resource
	private TblXzsFx006Mapper tblXzsFx006Mapper;
	
	public void addFx006(TblXzsFx006 tblXzsFx006){
		tblXzsFx006Mapper.addFx006(tblXzsFx006);
	}
	
}
