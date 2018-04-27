package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblPaShixin;
import org.pbccrc.api.core.mapper.TblPaShixinMapper;
import org.springframework.stereotype.Service;

@Service
public class TblPaShixinDao {
	
	@Resource
	private TblPaShixinMapper tblPaShixinMapper;
	
	public void addPaShixin(TblPaShixin tblPaShixin){
		tblPaShixinMapper.addPaShixin(tblPaShixin);
	}

	
}
