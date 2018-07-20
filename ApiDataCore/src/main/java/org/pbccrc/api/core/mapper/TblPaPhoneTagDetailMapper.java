package org.pbccrc.api.core.mapper;

import org.pbccrc.api.base.bean.TblPaPhoneTagDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface TblPaPhoneTagDetailMapper {
	
	void addPaPhoneTagDetail(TblPaPhoneTagDetail tblPaPhoneTagDetail);
	
}
