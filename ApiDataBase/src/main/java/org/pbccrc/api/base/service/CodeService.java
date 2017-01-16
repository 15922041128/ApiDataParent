package org.pbccrc.api.base.service;

import java.util.List;

import org.pbccrc.api.base.bean.Code;

import com.alibaba.fastjson.JSONArray;

public interface CodeService {

	List<Code> queryAll();
	
	JSONArray queryAllInMemory();
}
