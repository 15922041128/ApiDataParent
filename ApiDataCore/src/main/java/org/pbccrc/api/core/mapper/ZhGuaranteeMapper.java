package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ZhGuaranteeMapper {

	List<Map<String, Object>> query(String insideCode);
}
