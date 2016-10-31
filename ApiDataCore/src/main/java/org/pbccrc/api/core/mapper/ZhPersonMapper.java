package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ZhPersonMapper {

	Map<String, Object> query(String innerID);
}
