package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.Code;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeMapper {

	Map<String, Object> queryByCode(String codeName);
	
	List<Code> queryAll();
}
