package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblPaBlackList;
import org.pbccrc.api.core.mapper.TblPaBlackListMapper;
import org.springframework.stereotype.Service;

@Service
public class TblPaBlackListDao {
	
	@Resource
	private TblPaBlackListMapper tblPaBlackListMapper;
	
	public void addPaBlackList(TblPaBlackList tblPaBlackList){
		tblPaBlackListMapper.addPaBlackList(tblPaBlackList);
	}

	
}
