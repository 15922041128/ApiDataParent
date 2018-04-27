package org.pbccrc.api.core.mapper;

import org.pbccrc.api.base.bean.TblPaLoan;
import org.springframework.stereotype.Repository;

@Repository
public interface TblPaLoanMapper {
	
	void addPaLoan(TblPaLoan tblPaLoan);

}
