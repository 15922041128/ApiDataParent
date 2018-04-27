package org.pbccrc.api.core.mapper;

import org.pbccrc.api.base.bean.TblPaOverdue;
import org.springframework.stereotype.Repository;

@Repository
public interface TblPaOverdueMapper {
	
	void addPaOverdue(TblPaOverdue tblPaOverdue);

}
