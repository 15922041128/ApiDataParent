package org.pbccrc.api.core.service.impl;

import java.util.List;

import org.pbccrc.api.base.bean.Code;
import org.pbccrc.api.base.service.CodeService;
import org.pbccrc.api.core.dao.CodeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeServiceImpl implements CodeService {
	
	@Autowired
	private CodeDao codeDao;

	public List<Code> queryAll() {
		return codeDao.queryAll();
	}
}
