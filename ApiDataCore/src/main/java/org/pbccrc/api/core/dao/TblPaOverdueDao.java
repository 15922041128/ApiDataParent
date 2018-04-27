package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblPaOverdue;
import org.pbccrc.api.core.mapper.TblPaOverdueMapper;
import org.springframework.stereotype.Service;

@Service
public class TblPaOverdueDao {
	
	@Resource
	private TblPaOverdueMapper tblPaOverdueMapper;
	
	public void addPaOverdue(TblPaOverdue tblPaOverdue){
		tblPaOverdueMapper.addPaOverdue(tblPaOverdue);
	}

	
}
