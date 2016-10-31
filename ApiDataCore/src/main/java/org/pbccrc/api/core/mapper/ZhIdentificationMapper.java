package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ZhIdentificationMapper {

	Map<String, Object> queryByIdentifier(String innerID);
}
