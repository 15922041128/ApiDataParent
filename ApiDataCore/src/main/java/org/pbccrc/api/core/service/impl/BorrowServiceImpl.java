package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.Borrow;
import org.pbccrc.api.base.service.BorrowService;
import org.pbccrc.api.core.dao.BorrowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class BorrowServiceImpl implements BorrowService{

	@Autowired
	private BorrowDao borrowDao;
	
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
		
		// 用户以json形式传入的borrow对象
		JSONArray loanInfoArray = JSONArray.parseArray(loanInfos);
		// TODO 判断loanInfoArray是否为空
		
		// 获取下一个sequences
		String seq = borrowDao.getNexSeq();
		
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
			
			// 将borrow对象加入到集合中
			 borrows.add(borrow);
		}
		
		// 向borrow表中插入数据
		borrowDao.addBorrows(borrows);
		
		int score = 0;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idNo", seq);
		map.put("idCardNo", idCard);
		map.put("realName", realName);
		map.put("score", score);
		
		score = borrowDao.getCreditModel(map);
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("realName", realName);
		returnJson.put("idCard", idCard);
		returnJson.put("trxNo", trxNo);
		returnJson.put("seq", seq);
		
		// TODO
		String suggest = "";
		
		JSONObject retData = new JSONObject();
		retData.put("score", score);
		retData.put("suggest", suggest);
		
		returnJson.put("retData", retData);
		
		return returnJson;
	}

}
