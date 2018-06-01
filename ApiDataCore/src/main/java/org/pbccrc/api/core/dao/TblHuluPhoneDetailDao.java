package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblHuluPhoneDetail;
import org.pbccrc.api.core.mapper.TblHuluPhoneDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class TblHuluPhoneDetailDao {
	
	@Resource
	private TblHuluPhoneDetailMapper tblHuluPhoneDetailMapper;
	
	public void addPhoneDetail(TblHuluPhoneDetail tblHuluPhoneDetail){
		tblHuluPhoneDetailMapper.addPhoneDetail(tblHuluPhoneDetail);
	}
	
}
