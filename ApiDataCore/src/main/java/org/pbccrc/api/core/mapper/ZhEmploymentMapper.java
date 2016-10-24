package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ZhEmploymentMapper {

	Map<String, Object> query(String insideCode);
}
