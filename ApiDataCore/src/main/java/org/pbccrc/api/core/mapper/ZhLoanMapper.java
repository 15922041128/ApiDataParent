package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ZhLoanMapper {

	Map<String, Object> query(String insideCode);
}
