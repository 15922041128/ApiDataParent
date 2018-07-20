package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblPaBlackListDetail;
import org.pbccrc.api.core.mapper.TblPaBlackListDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class TblPaBlackListDetailDao {
	
	@Resource
	private TblPaBlackListDetailMapper tblPaBlackListDetailMapper;
	
	public void addPaBlackListDetail(TblPaBlackListDetail tblPaBlackListDetail){
		tblPaBlackListDetailMapper.addPaBlackListDetail(tblPaBlackListDetail);
	}
	

	
}
