package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface BhyhMapper {

	Map<String, Object> getBhyh(String innerID);
}
