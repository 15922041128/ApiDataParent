package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface BorrowDetailMapper {

	Map<String, Object> getBorrowDetail(Map<String, Object> param);
}
