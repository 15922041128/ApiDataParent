package org.pbccrc.api.base.service;

import java.util.List;
import java.util.Map;

public interface ScoreService {

	List<Map<String, Object>> getScore(String innerID);
}
