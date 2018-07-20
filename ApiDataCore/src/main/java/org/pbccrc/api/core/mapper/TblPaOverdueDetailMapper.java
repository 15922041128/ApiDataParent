package org.pbccrc.api.core.mapper;

import org.pbccrc.api.base.bean.TblPaOverdueDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface TblPaOverdueDetailMapper {
	
	void addPaOverdueDetail(TblPaOverdueDetail tblPaOverdueDetail);

}
