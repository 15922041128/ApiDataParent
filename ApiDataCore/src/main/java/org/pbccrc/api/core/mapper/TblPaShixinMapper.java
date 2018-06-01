package org.pbccrc.api.core.mapper;

import org.pbccrc.api.base.bean.TblPaShixin;
import org.springframework.stereotype.Repository;

@Repository
public interface TblPaShixinMapper {
	
	void addPaShixin(TblPaShixin tblPaShixin);
	
	void addPaShixinZh(TblPaShixin tblPaShixin);

}
