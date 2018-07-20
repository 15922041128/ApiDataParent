package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblPaPhoneTagDetail;
import org.pbccrc.api.core.mapper.TblPaPhoneTagDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class TblPaPhoneTagDetailDao {
	
	@Resource
	private TblPaPhoneTagDetailMapper tblPaPhoneTagDetailMapper;
	
	public void addPaPhoneTagDetail(TblPaPhoneTagDetail tblPaPhoneTagDetail){
		tblPaPhoneTagDetailMapper.addPaPhoneTagDetail(tblPaPhoneTagDetail);
	}

}
