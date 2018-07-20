package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblPaScoreDetail;
import org.pbccrc.api.core.mapper.TblPaScoreDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class TblPaScoreDetailDao {
	
	@Resource
	private TblPaScoreDetailMapper tblPaScoreDetailMapper;
	
	public void addPaScoreDetail(TblPaScoreDetail tblPaScoreDetail){
		tblPaScoreDetailMapper.addPaScoreDetail(tblPaScoreDetail);
	}

}
