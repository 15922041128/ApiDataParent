package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.TblPaPhoneTag;
import org.pbccrc.api.core.mapper.TblPaPhoneTagMapper;
import org.springframework.stereotype.Service;

@Service
public class TblPaPhoneTagDao {
	
	@Resource
	private TblPaPhoneTagMapper tblPaPhoneTagMapper;
	
	public void addPaPhoneTag(TblPaPhoneTag tblPaPhoneTag){
		tblPaPhoneTagMapper.addPaPhoneTag(tblPaPhoneTag);
	}

	
}
