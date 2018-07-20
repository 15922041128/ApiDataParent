package org.pbccrc.api.core.mapper;

import org.pbccrc.api.base.bean.TblPaLoanDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface TblPaLoanDetailMapper {
	
	void addPaLoanDetail(TblPaLoanDetail tblPaLoanDetail);
}
