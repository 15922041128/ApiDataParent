package org.pbccrc.api.core.mapper;

import java.util.List;

import org.pbccrc.api.base.bean.Marketee;
import org.pbccrc.api.base.bean.SmsCondition;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketeeMapper {

	/**
	 * @param map
	 */
	Integer getMarketeeCount(SmsCondition smsCondition);
	
	/**
	 * @param map
	 */
	List<Marketee> getMarketeeTelNums(SmsCondition smsCondition);
	
}
