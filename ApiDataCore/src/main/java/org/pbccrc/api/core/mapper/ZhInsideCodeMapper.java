package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ZhInsideCodeMapper {

	Map<String, Object> queryByIdentifier(String insideCode);
}
