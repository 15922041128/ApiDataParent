package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblPaScore;
import org.pbccrc.api.core.mapper.TblPaScoreMapper;
import org.springframework.stereotype.Service;

@Service
public class TblPaScoreDao {
	
	@Resource
	private TblPaScoreMapper tblPaScoreMapper;
	
	public void addPaScore(TblPaScore tblPaScore){
		tblPaScoreMapper.addPaScore(tblPaScore);
	}

	public void addPaScoreZh(TblPaScore tblPaScore){
		tblPaScoreMapper.addPaScoreZh(tblPaScore);
	}
}
