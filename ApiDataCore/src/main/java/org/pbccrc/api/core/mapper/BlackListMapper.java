package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface BlackListMapper {

	Map<String, Object> getList(String innerID);
}
