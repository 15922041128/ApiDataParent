package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface CodeMapper {

	Map<String, Object> queryByCode(String codeName);
}
