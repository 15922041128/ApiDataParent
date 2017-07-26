package org.pbccrc.api.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.pbccrc.api.base.service.CreditModelService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.core.dao.BlackListDao;
import org.pbccrc.api.core.dao.GradeDao;
import org.pbccrc.api.core.dao.WhiteListDao;
import org.pbccrc.api.core.dao.ZhIdentificationDao;
//import org.pbccrc.api.core.dao.datasource.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditModelServiceImpl implements CreditModelService {
	
	@Autowired
	private ZhIdentificationDao zhIdentificationDao;
	
	@Autowired
	private BlackListDao blackListDao;
	
	@Autowired
	private WhiteListDao whiteListDao;
	
	@Autowired
	private GradeDao gradeDao;

	
	/**
	 * 信贷模型
	 * @param userID
	 * @param name
	 * @param idCardNo
	 * @param accountNo
	 * @param mobile
	 * @return
	 */
	public Map<String, Object> creditModel(String userID, String name, String idCardNo, String accountNo, String mobile) throws Exception{
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 查询是否成功
		boolean isSuccess = true;
		// 是否通过
		boolean isPass = true;
		
		// 查询内码是否存在
		Map<String, Object> insideCodeMap = null;
		// 根据两标进行查询
		insideCodeMap =  zhIdentificationDao.getInnerID(name, idCardNo);
//		try {
//			DynamicDataSourceHolder.change2oracle();
//			insideCodeMap =  zhIdentificationDao.getInnerID(name, idCardNo);
//			DynamicDataSourceHolder.change2mysql();
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			DynamicDataSourceHolder.change2mysql();
//		}
		
		// 如果查询内码失败则返回
		if (null == insideCodeMap) {
			// TODO 查询其他源(京东)
			isSuccess = false;
			returnMap.put("isSuccess", isSuccess);
			return returnMap;
		}
		
		// 获取内码
		String innerId = String.valueOf(insideCodeMap.get("INNERID"));
		
		// 白名单
		Map<String, Object> whiteList = null;
		// 黑名单
		Map<String, Object> blackList = null;
		// 查询白名单
		whiteList = whiteListDao.getList(innerId);
//		try {
//			DynamicDataSourceHolder.change2oracle();
//			whiteList = whiteListDao.getList(innerId);
//			DynamicDataSourceHolder.change2mysql();
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			DynamicDataSourceHolder.change2mysql();
//		}
		
		// 评级
		String grade = Constants.BLANK;
		// 判断白名单是否为空
		if (null != whiteList && null != whiteList.get("INNERID")) {
			// 不为空获取评级
			grade = String.valueOf(whiteList.get("GRADE"));
		} else {
			// 为空查询黑名单
//			try {
//				DynamicDataSourceHolder.change2oracle();
//				blackList = blackListDao.getList(innerId);
//				DynamicDataSourceHolder.change2mysql();
//			} catch (Exception e) {
//				throw e;
//			} finally {
//				DynamicDataSourceHolder.change2mysql();
//			}
			
			blackList = blackListDao.getList(innerId);
			
			
			// 判断黑名单是否为空
			if (null != blackList && null != blackList.get("INNERID")) {
				// 不为空直接返回
				isPass = false;
				returnMap.put("isPass", isPass);
				returnMap.put("isSuccess", isSuccess);
				return returnMap;
			} else {
				// 为空则进行决策 
				// TODO
			}
		}
		
		// 根据当前用户和评级获取评级信息
		Map<String, Object> gradeMap = gradeDao.getGrade(userID, grade);
		
		returnMap.put("gradeMap", gradeMap);
		returnMap.put("isPass", isPass);
		returnMap.put("isSuccess", isSuccess);
		
		return returnMap;
	}
	
}
