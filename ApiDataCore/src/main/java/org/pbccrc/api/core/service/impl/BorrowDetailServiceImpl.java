package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.service.BorrowDetailService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.core.dao.ApiLogDao;
import org.pbccrc.api.core.dao.BorrowDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class BorrowDetailServiceImpl implements BorrowDetailService{

	@Autowired
	private BorrowDetailDao borrowDetailDao;
	
	@Autowired
	private ApiLogDao apiLogDao;

	/**
	 * 查询白名单
	 * @param realName    姓名
	 * @param idCard	    身份证号
	 * @param userID	   用户ID
	 * @param uuid	      uuid
	 * @return
	 * @throws Exception
	 */
	public JSONObject getBorrowDetail(String realName, String idCard, String userID, String uuid) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("realName", realName);
		param.put("idCard", idCard);
		
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("REAL_NAME", realName);
		returnJson.put("ID_CARD", idCard);
		
		JSONObject retData = new JSONObject();
		Map<String, Object> borrowDetail = borrowDetailDao.getBorrowDetail(param);
		if (null == borrowDetail || borrowDetail.isEmpty()) {
			returnJson.put("isSuccess", false);
		} else {
			returnJson.put("isSuccess", true);
			
			retData.put("IS_GOOD", borrowDetail.get("IS_GOOD"));
			retData.put("IS_BAD", borrowDetail.get("IS_BAD"));
			retData.put("GOOD_CNT", borrowDetail.get("GOOD_CNT"));
			retData.put("BAD_CNT", borrowDetail.get("BAD_CNT"));
			retData.put("UNKNOWN_CNT", borrowDetail.get("UNKNOWN_CNT"));
			retData.put("SUCCESS_MAX_AMOUNT", borrowDetail.get("SUCCESS_MAX_AMOUNT"));
			retData.put("SUCCESS_MIN_AMOUNT", borrowDetail.get("SUCCESS_MIN_AMOUNT"));
		}
		
		returnJson.put("retData", retData);
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userID);
		apiLog.setLocalApiID(Constants.API_ID_GET_WHITE_LIST);
		// 参数
		JSONObject params = new JSONObject();
		params.put("realName", realName);
		params.put("idCard", idCard);
		apiLog.setParams(params.toJSONString());
		apiLog.setDataFrom(Constants.DATA_FROM_LOCAL);
		apiLog.setIsSuccess(String.valueOf(returnJson.get("isSuccess")));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);
		
		return returnJson;
	}
	

}
