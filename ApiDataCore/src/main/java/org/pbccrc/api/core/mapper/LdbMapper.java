package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface LdbMapper {

	Map<String, Object> queryByService(String service);
}
