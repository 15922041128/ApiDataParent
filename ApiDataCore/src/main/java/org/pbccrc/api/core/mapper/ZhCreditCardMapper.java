package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ZhCreditCardMapper {

	Map<String, Object> query(String insideCode);
}
