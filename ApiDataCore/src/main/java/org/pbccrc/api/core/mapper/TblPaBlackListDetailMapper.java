package org.pbccrc.api.core.mapper;

import org.pbccrc.api.base.bean.TblPaBlackListDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface TblPaBlackListDetailMapper {
	
	void addPaBlackListDetail(TblPaBlackListDetail tblPaBlackListDetail);
	
}
