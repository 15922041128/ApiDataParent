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
import com.sun.jersey.core.spi.scanning.Scanner;

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
	 * @param realName    姓名
	 * @param idCard	    身份证号
	 * @param trxNo       查询编码
	 * @param loanInfos   查询数据 
	 * @return
	 * @throws Exception
	 */
	public JSONObject getResultParam(String realName, String idCard, String trxNo, String loanInfos) throws Exception {
		
		// borrow对象集合
		List<Borrow> borrows = new ArrayList<Borrow>();
		
		// seq
		String seq = Constants.BLANK;
		/** 返回参数 */
		// 总申请次数
		String total_Apply_Times = Constants.BLANK;
		// 贷款机构数
		String total_Loan_Company = Constants.BLANK;
		// 总贷金额
		String total_Borrow_Amount = Constants.BLANK;
		// 最大贷款额
		String max_Borrow_Amount = Constants.BLANK;
		// 最小贷款额
		String mix_Borrow_Amount = Constants.BLANK;
		// 平均贷款额
		String avg_Borrow_Amount = Constants.BLANK;
		// 申请成功次数
		String apply_Success_Times = Constants.BLANK;
		// 成功申请总额
		String total_Success_Borrow_Amount = Constants.BLANK;
		// 最大成功申请贷款额
		String max_Success_Borrow_Amount = Constants.BLANK;
		// 最小成功申请贷款额
		String min_Success_Borrow_Amount = Constants.BLANK;
		// 平均成功申请贷款额
		String avg_Success_Borrow_Amount = Constants.BLANK;
		// 申请失败次数
		String apply_Failed_Times = Constants.BLANK;
		// 申请取消次数
		String apply_Cancel_Times = Constants.BLANK;
		// 12个月申请次数
		String apply_Times_12m = Constants.BLANK;
		// 12个月申请机构数
		String loan_Company_12m = Constants.BLANK;
		// 12个月申请成功次数
		String apply_Success_Times_12m = Constants.BLANK;
		// 12个月成功申请额
		String apply_Success_Amount_12m = Constants.BLANK;
		// 12个月申请失败次数
		String apply_Failed_Times_12m = Constants.BLANK;
		// 12个月申请取消次数
		String apply_Cancel_Times_12m = Constants.BLANK;
		// 总逾期金额
		String total_arrears_Amount = Constants.BLANK;
		// 最大逾期金额
		String max_arrears_Amount = Constants.BLANK;
		// 总正常次数
		String total_good_times = Constants.BLANK;
		// 总灰色次数
		String total_slightbad_times = Constants.BLANK;
		// 总灰色期限
		String total_slightbad_loanperiod = Constants.BLANK;
		// 最早贷款时间
		String first_loan_time = Constants.BLANK;
		// 成功机构比例
		String success_apply_company_ratio = Constants.BLANK;
		// 成功率
		String success_apply_ratio = Constants.BLANK;
		// 灰的比例
		String slightbad_ratio = Constants.BLANK;
		// 好的比例
		String good_ratio = Constants.BLANK;
		// 灰色逾期金额率
		String slightbad_arrear_ratio = Constants.BLANK;
		// 灰色逾期期限率
		String slightbad_peroid_ratio = Constants.BLANK;
		// 申请成功率
		String success_amount_ratio = Constants.BLANK;
		// 年龄
		String age = Constants.BLANK;
		// 性别 
		String gender = Constants.BLANK;
		// 是否为好
		String is_good = Constants.BLANK;
		// 是否为坏
		String is_bad = Constants.BLANK;
		// errorMessage
		String errorMessage = Constants.BLANK;
		
		// 用户以json形式传入的borrow对象
		JSONArray loanInfoArray = JSONArray.parseArray(loanInfos);
			
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
		
		map.put("total_Apply_Times", total_Apply_Times);
		map.put("total_Loan_Company", total_Loan_Company);
		map.put("total_Borrow_Amount", total_Borrow_Amount);
		map.put("max_Borrow_Amount", max_Borrow_Amount);
		map.put("mix_Borrow_Amount", mix_Borrow_Amount);
		map.put("avg_Borrow_Amount", avg_Borrow_Amount);
		map.put("apply_Success_Times", apply_Success_Times);
		map.put("total_Success_Borrow_Amount", total_Success_Borrow_Amount);
		map.put("max_Success_Borrow_Amount", max_Success_Borrow_Amount);
		map.put("min_Success_Borrow_Amount", min_Success_Borrow_Amount);
		
		map.put("avg_Success_Borrow_Amount", avg_Success_Borrow_Amount);
		map.put("apply_Failed_Times", apply_Failed_Times);
		map.put("apply_Cancel_Times", apply_Cancel_Times);
		map.put("apply_Times_12m", apply_Times_12m);
		map.put("loan_Company_12m", loan_Company_12m);
		map.put("apply_Success_Times_12m", apply_Success_Times_12m);
		map.put("apply_Success_Amount_12m", apply_Success_Amount_12m);
		map.put("apply_Failed_Times_12m", apply_Failed_Times_12m);
		map.put("apply_Cancel_Times_12m", apply_Cancel_Times_12m);
		map.put("total_arrears_Amount", total_arrears_Amount);
		
		map.put("max_arrears_Amount", max_arrears_Amount);
		map.put("total_good_times", total_good_times);
		map.put("total_slightbad_times", total_slightbad_times);
		map.put("total_slightbad_loanperiod", total_slightbad_loanperiod);
		map.put("first_loan_time", first_loan_time);
		map.put("success_apply_company_ratio", success_apply_company_ratio);
		map.put("success_apply_ratio", success_apply_ratio);
		map.put("slightbad_ratio", slightbad_ratio);
		map.put("good_ratio", good_ratio);
		map.put("slightbad_arrear_ratio", slightbad_arrear_ratio);
		
		map.put("slightbad_peroid_ratio", slightbad_peroid_ratio);
		map.put("success_amount_ratio", success_amount_ratio);
		map.put("age", age);
		map.put("gender", gender);
		map.put("is_good", is_good);
		map.put("is_bad", is_bad);
		
		map.put("returnState", 0);
		map.put("errorMessage", errorMessage);
		
		map = borrowDao.getCreditModelParam(map);
		errorMessage = String.valueOf(map.get("errorMessage"));
		
		total_Apply_Times = String.valueOf(map.get("total_Apply_Times"));
		total_Loan_Company = String.valueOf(map.get("total_Loan_Company"));
		total_Borrow_Amount = String.valueOf(map.get("total_Borrow_Amount"));
		max_Borrow_Amount = String.valueOf(map.get("max_Borrow_Amount"));
		mix_Borrow_Amount = String.valueOf(map.get("mix_Borrow_Amount"));
		avg_Borrow_Amount = String.valueOf(map.get("avg_Borrow_Amount"));
		apply_Success_Times = String.valueOf(map.get("apply_Success_Times"));
		total_Success_Borrow_Amount = String.valueOf(map.get("total_Success_Borrow_Amount"));
		max_Success_Borrow_Amount = String.valueOf(map.get("max_Success_Borrow_Amount"));
		min_Success_Borrow_Amount = String.valueOf(map.get("min_Success_Borrow_Amount"));
		
		avg_Success_Borrow_Amount = String.valueOf(map.get("avg_Success_Borrow_Amount"));
		apply_Failed_Times = String.valueOf(map.get("apply_Failed_Times"));
		apply_Cancel_Times = String.valueOf(map.get("apply_Cancel_Times"));
		apply_Times_12m = String.valueOf(map.get("apply_Times_12m"));
		loan_Company_12m = String.valueOf(map.get("loan_Company_12m"));
		apply_Success_Times_12m = String.valueOf(map.get("apply_Success_Times_12m"));
		apply_Success_Amount_12m = String.valueOf(map.get("apply_Success_Amount_12m"));
		apply_Failed_Times_12m = String.valueOf(map.get("apply_Failed_Times_12m"));
		apply_Cancel_Times_12m = String.valueOf(map.get("apply_Cancel_Times_12m"));
		total_arrears_Amount = String.valueOf(map.get("total_arrears_Amount"));
		
		max_arrears_Amount = String.valueOf(map.get("max_arrears_Amount"));
		total_good_times = String.valueOf(map.get("total_good_times"));
		total_slightbad_times = String.valueOf(map.get("total_slightbad_times"));
		total_slightbad_loanperiod = String.valueOf(map.get("total_slightbad_loanperiod"));
		first_loan_time = String.valueOf(map.get("first_loan_time"));
		success_apply_company_ratio = String.valueOf(map.get("success_apply_company_ratio"));
		success_apply_ratio = String.valueOf(map.get("success_apply_ratio"));
		slightbad_ratio = String.valueOf(map.get("slightbad_ratio"));
		good_ratio = String.valueOf(map.get("good_ratio"));
		slightbad_arrear_ratio = String.valueOf(map.get("slightbad_arrear_ratio"));
		
		slightbad_peroid_ratio = String.valueOf(map.get("slightbad_peroid_ratio"));
		success_amount_ratio = String.valueOf(map.get("success_amount_ratio"));
		age = String.valueOf(map.get("age"));
		gender = String.valueOf(map.get("gender"));
		is_good = String.valueOf(map.get("is_good"));
		is_bad = String.valueOf(map.get("is_bad"));
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("realName", realName);
		returnJson.put("idCard", idCard);
		returnJson.put("trxNo", trxNo);
		returnJson.put("seq", seq);
		
		JSONObject retData = new JSONObject();
		retData.put("total_Apply_Times", total_Apply_Times);
		retData.put("total_Loan_Company", total_Loan_Company);
		retData.put("total_Borrow_Amount", total_Borrow_Amount);
		retData.put("max_Borrow_Amount", max_Borrow_Amount);
		retData.put("mix_Borrow_Amount", mix_Borrow_Amount);
		retData.put("avg_Borrow_Amount", avg_Borrow_Amount);
		retData.put("apply_Success_Times", apply_Success_Times);
		retData.put("total_Success_Borrow_Amount", total_Success_Borrow_Amount);
		retData.put("max_Success_Borrow_Amount", max_Success_Borrow_Amount);
		retData.put("min_Success_Borrow_Amount", min_Success_Borrow_Amount);
		
		retData.put("avg_Success_Borrow_Amount", avg_Success_Borrow_Amount);
		retData.put("apply_Failed_Times", apply_Failed_Times);
		retData.put("apply_Cancel_Times", apply_Cancel_Times);
		retData.put("apply_Times_12m", apply_Times_12m);
		retData.put("loan_Company_12m", loan_Company_12m);
		retData.put("apply_Success_Times_12m", apply_Success_Times_12m);
		retData.put("apply_Success_Amount_12m", apply_Success_Amount_12m);
		retData.put("apply_Failed_Times_12m", apply_Failed_Times_12m);
		retData.put("apply_Cancel_Times_12m", apply_Cancel_Times_12m);
		retData.put("total_arrears_Amount", total_arrears_Amount);
		
		retData.put("max_arrears_Amount", max_arrears_Amount);
		retData.put("total_good_times", total_good_times);
		retData.put("total_slightbad_times", total_slightbad_times);
		retData.put("total_slightbad_loanperiod", total_slightbad_loanperiod);
		retData.put("first_loan_time", first_loan_time);
		retData.put("success_apply_company_ratio", success_apply_company_ratio);
		retData.put("success_apply_ratio", success_apply_ratio);
		retData.put("slightbad_ratio", slightbad_ratio);
		retData.put("good_ratio", good_ratio);
		retData.put("slightbad_arrear_ratio", slightbad_arrear_ratio);
		
		retData.put("slightbad_peroid_ratio", slightbad_peroid_ratio);
		retData.put("success_amount_ratio", success_amount_ratio);
		retData.put("age", age);
		retData.put("gender", gender);
		retData.put("is_good", is_good);
		retData.put("is_bad", is_bad);
		
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
