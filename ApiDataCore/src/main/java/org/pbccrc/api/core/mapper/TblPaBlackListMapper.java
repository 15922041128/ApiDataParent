package org.pbccrc.api.core.mapper;

import org.pbccrc.api.base.bean.TblPaBlackList;
import org.springframework.stereotype.Repository;

@Repository
public interface TblPaBlackListMapper {
	
	void addPaBlackList(TblPaBlackList tblPaBlackList);

}
