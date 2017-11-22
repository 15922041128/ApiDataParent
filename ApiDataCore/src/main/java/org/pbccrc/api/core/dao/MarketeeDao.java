package org.pbccrc.api.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.Marketee;
import org.pbccrc.api.base.bean.SmsCondition;
import org.pbccrc.api.core.mapper.MarketeeMapper;
import org.springframework.stereotype.Service;
/**
 * 营销对象Dao
 * @author charles
 *
 */
@Service
public class MarketeeDao {
	
	@Resource
	private MarketeeMapper marketeeMapper;
	
	/**
	 * 返回符合条件营销对象数量
	 * @param map
	 * @return
	 */
	public Integer getMarketeeCount(SmsCondition smsCondition) {
		return marketeeMapper.getMarketeeCount(smsCondition);
		
	}
	
	/**
	 * 返回符合条件营销对象TelNum
	 * @param map
	 * @return
	 */
	public List<String> getMarketeeTelNums(SmsCondition smsCondition) {
		return marketeeMapper.getMarketeeTelNums(smsCondition);
		
	}
	
}
