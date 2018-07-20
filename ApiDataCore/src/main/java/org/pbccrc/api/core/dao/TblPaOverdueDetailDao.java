package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblPaOverdueDetail;
import org.pbccrc.api.core.mapper.TblPaOverdueDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class TblPaOverdueDetailDao {
	
	@Resource
	private TblPaOverdueDetailMapper tblPaOverdueDetailMapper;
	
	public void addPaOverdueDetail(TblPaOverdueDetail tblPaOverdueDetail){
		tblPaOverdueDetailMapper.addPaOverdueDetail(tblPaOverdueDetail);
	}

}
