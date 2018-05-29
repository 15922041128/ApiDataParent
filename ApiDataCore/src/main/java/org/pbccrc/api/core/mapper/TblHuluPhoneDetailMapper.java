package org.pbccrc.api.core.mapper;

import org.pbccrc.api.base.bean.TblHuluPhoneDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface TblHuluPhoneDetailMapper {
	
	void addPhoneDetail(TblHuluPhoneDetail tblHuluPhoneDetail);
}
