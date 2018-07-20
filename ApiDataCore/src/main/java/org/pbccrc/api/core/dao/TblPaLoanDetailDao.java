package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblPaLoanDetail;
import org.pbccrc.api.core.mapper.TblPaLoanDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class TblPaLoanDetailDao {
	
	@Resource
	private TblPaLoanDetailMapper tblPaLoanDetailMapper;
	
	public void addPaLoanDetail(TblPaLoanDetail tblPaLoanDetail){
		tblPaLoanDetailMapper.addPaLoanDetail(tblPaLoanDetail);
	}

}
