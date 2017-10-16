package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.Borrow;
import org.pbccrc.api.base.service.BorrowService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.dao.BorrowDao;
import org.pbccrc.api.core.dao.ScoreDao;
import org.pbccrc.api.core.dao.ZhIdentificationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class BorrowServiceImpl implements BorrowService{

	@Autowired
	private BorrowDao borrowDao;
	
	@Autowired
	private ScoreDao scoreDao;
	
	@Autowired
	private ZhIdentificationDao zhIdentificationDao;
	
	/**
	 * 
	 * @param realName    姓名
	 * @param idCard	    身份证号
	 * @param trxNo       查询编码
	 * @param loanInfos   查询数据 
	 * @return
	 * @throws Exception
	 */
	public JSONObject getResult(String realName, String idCard, String trxNo, String loanInfos) throws Exception {
		
		// borrow对象集合
		List<Borrow> borrows = new ArrayList<Borrow>();
		
		// 返回分数
		int score = 0;
		// TODO
		// 评分建议
		String suggest = "";
		// seq
		String seq = Constants.BLANK;
		// errorMessage
		String errorMessage = Constants.BLANK;
		
		// 用户以json形式传入的borrow对象
		JSONArray loanInfoArray = JSONArray.parseArray(loanInfos);
		// 判断loanInfoArray是否为空 
		if (null == loanInfoArray || 0 == loanInfoArray.size()) {
			// 若为空则从表【SCORE201705】中查询分数
			// 根据身份证号获取内码信息
			Map<String, Object> insideCodeMap = zhIdentificationDao.getInnerID(realName, idCard);
			// 判断是否获取成功
			if (null != insideCodeMap && 0 != insideCodeMap.size()) {
				// 内码
				String innerID = String.valueOf(insideCodeMap.get("INNERID"));
				// 分数
				String score201705 = Constants.BLANK;
				List<Map<String, Object>> scoreList = scoreDao.getScore201705(innerID);
				if (null != scoreList && 0 != scoreList.size()) {
					Map<String, Object> scoreMap = scoreList.get(0);
					score201705 = String.valueOf(scoreMap.get("SCORE"));
				}
				
				// 判断是否查询成功
				if (!StringUtil.isNull(score201705)) {
					score = Integer.parseInt(score201705);
					
					// 分数处理
					if (300 > score) {
						score = 300;
					}
					
					if (850 < score) {
						score = 850;
					}
					
				}
			} 
		} else {
			
			// 获取下一个sequences
			seq = borrowDao.getNexSeq();
			
			// 将json对象转为borrow对象
			for (Object object : loanInfoArray) {
				
				JSONObject json = (JSONObject)object;
				
				Borrow borrow = new Borrow();
				// ID
				borrow.setId(Integer.parseInt(seq));
				// 借款类型
				borrow.setBorrowType(json.getString("borrowType"));
				// 借款状态
				borrow.setBorrowState(json.getString("borrowState"));
				// 合同金额
				borrow.setBorrowAmount(json.getIntValue("borrowAmount"));
				// 合同日期
				String contractDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date(Long.parseLong(json.getString("contractDate"))));
				borrow.setContractDate(contractDate);
				// 批贷期数
				borrow.setLoanPeriod(json.getString("loanPeriod"));
				// 还款状态
				borrow.setRepayState(json.getString("repayState"));
				// 欠款金额
				borrow.setArrearsAmount(json.getString("arrearsAmount"));
				// 公司代码
				borrow.setCompanyCode(json.getString("companyCode"));
				// 姓名
				borrow.setRealName(realName);
				// 身份证号
				borrow.setIdCard(idCard);
				
				// 将borrow对象加入到集合中
				 borrows.add(borrow);
			}
			
			// 向borrow表中插入数据
			borrowDao.addBorrows(borrows);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idNo", seq);
			map.put("idCardNo", idCard);
			map.put("realName", realName);
			map.put("score", score);
			map.put("errorMessage", errorMessage);
			
			map = borrowDao.getCreditModel(map);
			score = Integer.parseInt(String.valueOf(map.get("score")));
			errorMessage = String.valueOf(map.get("errorMessage"));
		}
		
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("realName", realName);
		returnJson.put("idCard", idCard);
		returnJson.put("trxNo", trxNo);
		returnJson.put("seq", seq);
		
		JSONObject retData = new JSONObject();
		retData.put("score", score);
		retData.put("suggest", suggest);
		
		returnJson.put("retData", retData);
		returnJson.put("seq", seq);
		returnJson.put("errorMessage", errorMessage);
		
		
		return returnJson;
	}
	
	/**
	 * 
	 * @param realName    姓名
	 * @param idCard	    身份证号
	 * @param trxNo       查询编码
	 * @param loanInfos   查询数据 
	 * @return
	 * @throws Exception
	 */
	public JSONObject getResultTri(String realName, String idCard, String trxNo, String loanInfos) throws Exception {
		
		// borrow对象集合
		List<Borrow> borrows = new ArrayList<Borrow>();
		
		// 返回分数
		int score = 0;
		// TODO
		// 评分建议
		String suggest = "";
		// seq
		String seq = Constants.BLANK;
		// errorMessage
		String errorMessage = Constants.BLANK;
		
		// 用户以json形式传入的borrow对象
		JSONArray loanInfoArray = JSONArray.parseArray(loanInfos);
		// 判断loanInfoArray是否为空 
		if (null == loanInfoArray || 0 == loanInfoArray.size()) {
			// 若为空则从表【SCORE201705】中查询分数
			// 根据身份证号获取内码信息
			Map<String, Object> insideCodeMap = zhIdentificationDao.getInnerID(realName, idCard);
			// 判断是否获取成功
			if (null != insideCodeMap && 0 != insideCodeMap.size()) {
				// 内码
				String innerID = String.valueOf(insideCodeMap.get("INNERID"));
				// 分数
				String score201705 = Constants.BLANK;
				List<Map<String, Object>> scoreList = scoreDao.getScore201705(innerID);
				if (null != scoreList && 0 != scoreList.size()) {
					Map<String, Object> scoreMap = scoreList.get(0);
					score201705 = String.valueOf(scoreMap.get("SCORE"));
				}
				
				// 判断是否查询成功
				if (!StringUtil.isNull(score201705)) {
					score = Integer.parseInt(score201705);
					
					// 分数处理
					if (300 > score) {
						score = 300;
					}
					
					if (850 < score) {
						score = 850;
					}
					
				}
			} 
		} else {
			
			// 获取下一个sequences
			seq = borrowDao.getNexSeq();
			
			// 将json对象转为borrow对象
			for (Object object : loanInfoArray) {
				
				JSONObject json = (JSONObject)object;
				
				Borrow borrow = new Borrow();
				// ID
				borrow.setId(Integer.parseInt(seq));
				// 借款类型
				borrow.setBorrowType(json.getString("borrowType"));
				// 借款状态
				borrow.setBorrowState(json.getString("borrowState"));
				// 合同金额
				borrow.setBorrowAmount(json.getIntValue("borrowAmount"));
				// 合同日期
				String contractDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date(Long.parseLong(json.getString("contractDate"))));
				borrow.setContractDate(contractDate);
				// 批贷期数
				borrow.setLoanPeriod(json.getString("loanPeriod"));
				// 还款状态
				borrow.setRepayState(json.getString("repayState"));
				// 欠款金额
				borrow.setArrearsAmount(json.getString("arrearsAmount"));
				// 公司代码
				borrow.setCompanyCode(json.getString("companyCode"));
				// 姓名
				borrow.setRealName(realName);
				// 身份证号
				borrow.setIdCard(idCard);
				
				// 将borrow对象加入到集合中
				 borrows.add(borrow);
			}
			
			// 向borrow表中插入数据
			borrowDao.addBorrows(borrows);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idNo", seq);
			map.put("idCardNo", idCard);
			map.put("realName", realName);
			map.put("score", score);
			map.put("errorMessage", errorMessage);
			
			map = borrowDao.getCreditModelTri(map);
			score = Integer.parseInt(String.valueOf(map.get("score")));
			errorMessage = String.valueOf(map.get("errorMessage"));
		}
		
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("realName", realName);
		returnJson.put("idCard", idCard);
		returnJson.put("trxNo", trxNo);
		returnJson.put("seq", seq);
		
		JSONObject retData = new JSONObject();
		retData.put("score", score);
		retData.put("suggest", suggest);
		
		returnJson.put("retData", retData);
		returnJson.put("seq", seq);
		returnJson.put("errorMessage", errorMessage);
		
		
		return returnJson;
	}
	
	/**
	 * 
	 * @param dataCnt		记录条数
	 * @param amount		额度
	 * @param amountFlag	额度之下或之上 (1以下,2以上,默认3000以下)
	 * @param status		状态(好，坏，灰)
	 * @param count			借贷次数 (默认为1)
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryResult(String dataCnt, String amount, String amountFlag, String status, String count) throws Exception {

		if (StringUtil.isNull(amount)) {
			// 默认3000以下
			amount = "3000";
			amountFlag = "1";
		}
		
		if (StringUtil.isNull(count)) {
			// 默认
			count = "1";
		}
		
		// TODO
		
		JSONObject returnJson = new JSONObject();
		
		return returnJson;
	}

}
