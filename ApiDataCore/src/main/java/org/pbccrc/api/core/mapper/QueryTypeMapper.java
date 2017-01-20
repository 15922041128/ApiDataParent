package org.pbccrc.api.core.mapper;

import java.util.List;

import org.pbccrc.api.base.bean.QueryType;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryTypeMapper {

	List<QueryType> queryAll();
}
