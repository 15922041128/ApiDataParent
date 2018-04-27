package org.pbccrc.api.core.mapper;

import org.pbccrc.api.base.bean.TblPaScore;
import org.springframework.stereotype.Repository;

@Repository
public interface TblPaScoreMapper {
	
	void addPaScore(TblPaScore tblPaScore);

}
