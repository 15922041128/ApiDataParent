package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ZhIdentificationMapper {

	List<Map<String, Object>> getInnerID(Map<String, String> map);
}
