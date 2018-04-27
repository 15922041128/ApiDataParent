package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblPaLoan;
import org.pbccrc.api.core.mapper.TblPaLoanMapper;
import org.springframework.stereotype.Service;

@Service
public class TblPaLoanDao {
	
	@Resource
	private TblPaLoanMapper tblPaLoanMapper;
	
	public void addPaLoan(TblPaLoan tblPaLoan){
		tblPaLoanMapper.addPaLoan(tblPaLoan);
	}

	
}
