package org.pbccrc.api.core.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface GradeMapper {

	Map<String, Object> getGrade(Map<String, String> gradeMap);
}
