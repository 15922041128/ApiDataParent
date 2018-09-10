package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.bean.LocalApi;
import org.pbccrc.api.base.bean.TblHuluPhoneDetail;
import org.pbccrc.api.base.bean.TblPaBlackList;
import org.pbccrc.api.base.bean.TblPaBlackListDetail;
import org.pbccrc.api.base.bean.TblPaLoan;
import org.pbccrc.api.base.bean.TblPaLoanDetail;
import org.pbccrc.api.base.bean.TblPaOverdue;
import org.pbccrc.api.base.bean.TblPaOverdueDetail;
import org.pbccrc.api.base.bean.TblPaPhoneTag;
import org.pbccrc.api.base.bean.TblPaPhoneTagDetail;
import org.pbccrc.api.base.bean.TblPaScore;
import org.pbccrc.api.base.bean.TblPaScoreDetail;
import org.pbccrc.api.base.bean.TblPaShixin;
import org.pbccrc.api.base.bean.TblXzsFx006;
import org.pbccrc.api.base.external.huludata.HuluImplementation;
import org.pbccrc.api.base.external.xinzhishang.XzsImplementation;
import org.pbccrc.api.base.service.ApiProductService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.dao.ApiLogDao;
import org.pbccrc.api.core.dao.BhyhDao;
import org.pbccrc.api.core.dao.BlackListDao;
import org.pbccrc.api.core.dao.ScoreDao;
import org.pbccrc.api.core.dao.TblHuluPhoneDetailDao;
import org.pbccrc.api.core.dao.TblPaBlackListDao;
import org.pbccrc.api.core.dao.TblPaBlackListDetailDao;
import org.pbccrc.api.core.dao.TblPaLoanDao;
import org.pbccrc.api.core.dao.TblPaLoanDetailDao;
import org.pbccrc.api.core.dao.TblPaOverdueDao;
import org.pbccrc.api.core.dao.TblPaOverdueDetailDao;
import org.pbccrc.api.core.dao.TblPaPhoneTagDao;
import org.pbccrc.api.core.dao.TblPaPhoneTagDetailDao;
import org.pbccrc.api.core.dao.TblPaScoreDao;
import org.pbccrc.api.core.dao.TblPaScoreDetailDao;
import org.pbccrc.api.core.dao.TblPaShixinDao;
import org.pbccrc.api.core.dao.TblXzsFx006Dao;
import org.pbccrc.api.core.dao.TelPersonDao;
import org.pbccrc.api.core.dao.ZhIdentificationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Service
public class ApiProductServiceImpl implements ApiProductService {
	
	@Autowired
	private ApiLogDao apiLogDao;
	
	@Autowired
	private TblPaPhoneTagDao tblPaPhoneTagDao;
	
	@Autowired
	private TblPaShixinDao tblPaShixinDao;
	
	@Autowired
	private TblPaOverdueDao tblPaOverdueDao;
	
	@Autowired
	private TblPaLoanDao tblPaLoanDao;
	
	@Autowired
	private TblPaBlackListDao tblPaBlackListDao;
	
	@Autowired
	private TblPaScoreDao tblPaScoreDao;
	
	@Autowired
	private ZhIdentificationDao zhIdentificationDao;
	
	@Autowired
	private BlackListDao blackListDao;
	
	@Autowired
	private ScoreDao scoreDao;
	
	@Autowired
	private TblXzsFx006Dao tblXzsFx006Dao;
	
	@Autowired
	private TblHuluPhoneDetailDao tblHuluPhoneDetailDao; 
	
	@Autowired
	private BhyhDao bhyhDao;
	
	@Autowired
	private TblPaPhoneTagDetailDao tblPaPhoneTagDetailDao;
	
	@Autowired
	private TblPaScoreDetailDao tblPaScoreDetailDao;
	
	@Autowired
	private TblPaLoanDetailDao tblPaLoanDetailDao;
	
	@Autowired
	private TblPaOverdueDetailDao tblPaOverdueDetailDao;
	
	@Autowired
	private TblPaBlackListDetailDao tblPaBlackListDetailDao; 
	
	@Autowired
	private TelPersonDao telPersonDao;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * bhyh
	 * @param name     姓名 
	 * @param idCard   身份证号码
	 * @param userID   用户ID
	 * @param uuid     UUID
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject bhyh(String name, String idCard, String userID, String uuid, LocalApi localApi) throws Exception {
		
		boolean isSuccess = false;
		
		JSONObject returnJson = new JSONObject();
		
		JSONObject resultJson = new JSONObject();
		
		// 查询内码是否存在
     	Map<String, Object> insideCodeMap = null;
        // 根据两标进行查询
     	insideCodeMap =  zhIdentificationDao.getInnerIDWithoutMD5(name, idCard);
     	// 判断内码是否为空
 		if (null != insideCodeMap) {
 			// 获取内码
 	 		String innerId = String.valueOf(insideCodeMap.get("INNERID"));
 	 		// 查询bhyh
 			Map<String, Object> bhyh = null;
 			bhyh = bhyhDao.getList(innerId);
 			// 判断bhyh是否为空
 			if (null != bhyh) {
 				isSuccess = true;
 				for (Map.Entry<String, Object> entry : bhyh.entrySet()) { 
 					resultJson.put(entry.getKey(), entry.getValue());
 				}
 				resultJson.remove("INNERID");
 			}
 		}
 		
 		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userID);
		apiLog.setLocalApiID(String.valueOf(localApi.getId()));
		// 参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("name", name);
		param.put("idCard", idCard);
		apiLog.setParams(JSON.toJSONString(param));
		apiLog.setDataFrom(Constants.DATA_FROM_PA);
		apiLog.setIsSuccess(String.valueOf(isSuccess));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);
 		
   		returnJson.put("result", resultJson);
   		returnJson.put("isSuccess", isSuccess);
 		
 		return returnJson;
	}
	
	/**
	 * 新反欺诈服务
	 * @param phone    电话号码
	 * @param name     姓名 
	 * @param idCard   身份证号码
	 * @param userID   用户ID
	 * @param uuid     UUID
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject fraud2(String phone, String name, String idCard, String userID, String uuid, LocalApi localApi) throws Exception {
		
		// 基本调用数据初始化
		String ptime = String.valueOf(System.currentTimeMillis());
		String vKey = StringUtil.string2MD5(Constants.pn_pkey + "_" + ptime + "_" + Constants.pn_pkey);
		String uid = StringUtil.MD5Encoder(name + idCard, "utf-8");
		// 返回数据初始化
		String result = Constants.BLANK;
		JSONObject returnObject = new JSONObject();
		// 客户端初始化
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		WebResource resource = null;
		int api_result = 0;
		
		 // 查询成功标识
        boolean isSuccess = false;
		
        /** 1.反欺诈识别 */
		/** 凭安电话号码查询 */
		resource = client.resource("https://jrapi.pacra.cn/phonetag?pname=" + Constants.pn_pname + "&vkey=" + vKey + "&ptime=" + ptime + "&phone=" + phone);
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		result = StringUtil.decodeUnicode(result);
		JSONObject resultObject = (JSONObject)JSONObject.parse(result);
        api_result = resultObject.getInteger("result");
        
        // 判断result
        if (0 == api_result) {
        	isSuccess = true;
        	JSONArray array = resultObject.getJSONArray("data");
        	
        	JSONArray returnArray = new JSONArray();
        	
        	// 将数据存入数据库(array是数组,需要遍历)
        	for (Object object : array) {
        		JSONObject returnData = (JSONObject) JSON.toJSON(object);
        		TblPaPhoneTagDetail tblPaPhoneTagDetail = new TblPaPhoneTagDetail();
        		tblPaPhoneTagDetail.setPhone(phone);
        		tblPaPhoneTagDetail.setTag(returnData.getString("tag"));
        		tblPaPhoneTagDetail.setTagTimes(returnData.getString("times"));
        		tblPaPhoneTagDetailDao.addPaPhoneTagDetail(tblPaPhoneTagDetail);
        		
        		returnArray.add(tblPaPhoneTagDetail);
        	}
        	
        	returnObject.put("phoneTag", JSON.toJSONString(returnArray));
        	
//        	for (Object object : array) {
//        		JSONObject returnData = (JSONObject) JSON.toJSON(object);
//        		String tag = returnData.getString("tag");
//            	if (tag.indexOf("欺诈") > 0) {
//                	// 插入数据库
//                	TblPaPhoneTag phoneTag = new TblPaPhoneTag();
//                	phoneTag.setPhone(phone);
//                	phoneTag.setApiQueryDate(apiQueryDate);
//                	phoneTag.setReturnValue(returnData.toJSONString());
//                	tblPaPhoneTagDao.addPaPhoneTag(phoneTag);
//                	
//                	returnObject.put("phoneTag", returnData);
//                	break;
//            	}
//        	}
        	
        }
        
        /** 凭安逾期查询 */
		StringBuilder sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/b/overdueClassify?");
		sb.append("pname=" + Constants.pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&phone=" + phone);
		sb.append("&uid=" + uid);
		
		resource = client.resource(sb.toString());
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		result = StringUtil.decodeUnicode(result);
		resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        api_result = resultObject.getInteger("result");
        
        // 判断result
        if (0 == api_result) {
        	isSuccess = true;
        	
        	// 插入数据库
        	TblPaOverdueDetail tblPaOverdueDetail = new TblPaOverdueDetail();
        	tblPaOverdueDetail.setPhone(phone);
        	
        	JSONObject returnData = resultObject.getJSONObject("data");
        	
        	// 获取接口返回数据
        	JSONObject record = returnData.getJSONArray("record").getJSONObject(0);
        	JSONObject classification = record.getJSONArray("classification").getJSONObject(0);
        	
        	// m3
        	JSONObject m3 =  classification.getJSONObject("M3");
        	if (null != m3) {
        		// bankLoan
        		JSONObject m3BankLoan = m3.getJSONObject("bankLoan"); 
        		if (null != m3BankLoan) {
        			String m3BankLoanRecordNums = m3BankLoan.getString("recordNums");
                	String m3BankLoanOrgNums = m3BankLoan.getString("orgNums");
                	String m3BankLoanMaxAmount = m3BankLoan.getString("maxAmount");
                	String m3BankLoanLongestDays = m3BankLoan.getString("longestDays");
                	String m3BankLoanLongestDaysTime = m3BankLoan.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM3BankLoanLongestDays(m3BankLoanLongestDays);
                	tblPaOverdueDetail.setM3BankLoanLongestDaysTime(m3BankLoanLongestDaysTime);
                	tblPaOverdueDetail.setM3BankLoanMaxAmount(m3BankLoanMaxAmount);
                	tblPaOverdueDetail.setM3BankLoanOrgNums(m3BankLoanOrgNums);
                	tblPaOverdueDetail.setM3BankLoanRecordNums(m3BankLoanRecordNums);
        		}
        		
        		// bankCredit
            	JSONObject m3BankCredit = m3.getJSONObject("bankCredit");
            	if (null != m3BankCredit) {
            		String m3BankCreditRecordNums = m3BankCredit.getString("recordNums");
                	String m3BankCreditOrgNums = m3BankCredit.getString("orgNums");
                	String m3BankCreditMaxAmount = m3BankCredit.getString("maxAmount");
                	String m3BankCreditLongestDays = m3BankCredit.getString("longestDays");
                	String m3BankCreditLongestDaysTime = m3BankCredit.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM3BankCreditLongestDays(m3BankCreditLongestDays);
                	tblPaOverdueDetail.setM3BankCreditLongestDaysTime(m3BankCreditLongestDaysTime);
                	tblPaOverdueDetail.setM3BankCreditMaxAmount(m3BankCreditMaxAmount);
                	tblPaOverdueDetail.setM3BankCreditOrgNums(m3BankCreditOrgNums);
                	tblPaOverdueDetail.setM3BankCreditRecordNums(m3BankCreditRecordNums);
            	}
            	
            	// otherLoan
            	JSONObject m3OtherLoan = m3.getJSONObject("otherLoan");
            	if (null != m3OtherLoan) {
            		String m3OtherLoanRecordNums = m3OtherLoan.getString("recordNums");
                	String m3OtherLoanOrgNums = m3OtherLoan.getString("orgNums");
                	String m3OtherLoanMaxAmount = m3OtherLoan.getString("maxAmount");
                	String m3OtherLoanLongestDays = m3OtherLoan.getString("longestDays");
                	String m3OtherLoanLongestDaysTime = m3OtherLoan.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM3OtherLoanLongestDays(m3OtherLoanLongestDays);
                	tblPaOverdueDetail.setM3OtherLoanLongestDaysTime(m3OtherLoanLongestDaysTime);
                	tblPaOverdueDetail.setM3OtherLoanMaxAmount(m3OtherLoanMaxAmount);
                	tblPaOverdueDetail.setM3OtherLoanOrgNums(m3OtherLoanOrgNums);
                	tblPaOverdueDetail.setM3OtherLoanRecordNums(m3OtherLoanRecordNums);
            	}
            	
            	// otherCredit
            	JSONObject m3OtherCredit = m3.getJSONObject("otherCredit");
            	if (null != m3OtherCredit) {
            		String m3OtherCreditRecordNums = m3OtherCredit.getString("recordNums");
                	String m3OtherCreditOrgNums = m3OtherCredit.getString("orgNumes");
                	String m3OtherCreditMaxAmount = m3OtherCredit.getString("maxAmount");
                	String m3OtherCreditLongestDays = m3OtherCredit.getString("longestDays");
                	String m3OtherCreditLongestDaysTime = m3OtherCredit.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM3OtherCreditLongestDays(m3OtherCreditLongestDays);
                	tblPaOverdueDetail.setM3OtherCreditLongestDaysTime(m3OtherCreditLongestDaysTime);
                	tblPaOverdueDetail.setM3OtherCreditMaxAmount(m3OtherCreditMaxAmount);
                	tblPaOverdueDetail.setM3OtherCreditOrgNums(m3OtherCreditOrgNums);
                	tblPaOverdueDetail.setM3OtherCreditRecordNums(m3OtherCreditRecordNums);
            	}
        	}
        	
        	// m6
        	JSONObject m6 =  classification.getJSONObject("M6");
        	if (null != m6) {
        		
            	// bankLoan
            	JSONObject m6BankLoan = m6.getJSONObject("bankLoan"); 
            	if (null != m6BankLoan) {
            		String m6BankLoanRecordNums = m6BankLoan.getString("recordNums");
                	String m6BankLoanOrgNums = m6BankLoan.getString("orgNums");
                	String m6BankLoanMaxAmount = m6BankLoan.getString("maxAmount");
                	String m6BankLoanLongestDays = m6BankLoan.getString("longestDays");
                	String m6BankLoanLongestDaysTime = m6BankLoan.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM6BankLoanLongestDays(m6BankLoanLongestDays);
                	tblPaOverdueDetail.setM6BankLoanLongestDaysTime(m6BankLoanLongestDaysTime);
                	tblPaOverdueDetail.setM6BankLoanMaxAmount(m6BankLoanMaxAmount);
                	tblPaOverdueDetail.setM6BankLoanOrgNums(m6BankLoanOrgNums);
                	tblPaOverdueDetail.setM6BankLoanRecordNums(m6BankLoanRecordNums);
            	}
            	
            	// bankCredit
            	JSONObject m6BankCredit = m6.getJSONObject("bankCredit");
            	if (null != m6BankCredit) {
            		String m6BankCreditRecordNums = m6BankCredit.getString("recordNums");
                	String m6BankCreditOrgNums = m6BankCredit.getString("orgNums");
                	String m6BankCreditMaxAmount = m6BankCredit.getString("maxAmount");
                	String m6BankCreditLongestDays = m6BankCredit.getString("longestDays");
                	String m6BankCreditLongestDaysTime = m6BankCredit.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM6BankCreditLongestDays(m6BankCreditLongestDays);
                	tblPaOverdueDetail.setM6BankCreditLongestDaysTime(m6BankCreditLongestDaysTime);
                	tblPaOverdueDetail.setM6BankCreditMaxAmount(m6BankCreditMaxAmount);
                	tblPaOverdueDetail.setM6BankCreditOrgNums(m6BankCreditOrgNums);
                	tblPaOverdueDetail.setM6BankCreditRecordNums(m6BankCreditRecordNums);
            	}
            	
            	// otherLoan
            	JSONObject m6OtherLoan = m6.getJSONObject("otherLoan");
            	if(null != m6OtherLoan) {
            		String m6OtherLoanRecordNums = m6OtherLoan.getString("recordNums");
                	String m6OtherLoanOrgNums = m6OtherLoan.getString("orgNums");
                	String m6OtherLoanMaxAmount = m6OtherLoan.getString("maxAmount");
                	String m6OtherLoanLongestDays = m6OtherLoan.getString("longestDays");
                	String m6OtherLoanLongestDaysTime = m6OtherLoan.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM6OtherLoanLongestDays(m6OtherLoanLongestDays);
                	tblPaOverdueDetail.setM6OtherLoanLongestDaysTime(m6OtherLoanLongestDaysTime);
                	tblPaOverdueDetail.setM6OtherLoanMaxAmount(m6OtherLoanMaxAmount);
                	tblPaOverdueDetail.setM6OtherLoanOrgNums(m6OtherLoanOrgNums);
                	tblPaOverdueDetail.setM6OtherLoanRecordNums(m6OtherLoanRecordNums);
            	}
            	
            	// otherCredit
            	JSONObject m6OtherCredit = m6.getJSONObject("otherCredit");
            	if (null != m6OtherCredit) {
            		String m6OtherCreditRecordNums = m6OtherCredit.getString("recordNums");
                	String m6OtherCreditOrgNums = m6OtherCredit.getString("orgNumes");
                	String m6OtherCreditMaxAmount = m6OtherCredit.getString("maxAmount");
                	String m6OtherCreditLongestDays = m6OtherCredit.getString("longestDays");
                	String m6OtherCreditLongestDaysTime = m6OtherCredit.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM6OtherCreditLongestDays(m6OtherCreditLongestDays);
                	tblPaOverdueDetail.setM6OtherCreditLongestDaysTime(m6OtherCreditLongestDaysTime);
                	tblPaOverdueDetail.setM6OtherCreditMaxAmount(m6OtherCreditMaxAmount);
                	tblPaOverdueDetail.setM6OtherCreditOrgNums(m6OtherCreditOrgNums);
                	tblPaOverdueDetail.setM6OtherCreditRecordNums(m6OtherCreditRecordNums);
            	}
        	}
        	
        	// m9
        	JSONObject m9 =  classification.getJSONObject("M9");
        	if (null != m9) {
        		// bankLoan
        		JSONObject m9BankLoan = m9.getJSONObject("bankLoan");
        		if (null != m9BankLoan) {
        			String m9BankLoanRecordNums = m9BankLoan.getString("recordNums");
                	String m9BankLoanOrgNums = m9BankLoan.getString("orgNums");
                	String m9BankLoanMaxAmount = m9BankLoan.getString("maxAmount");
                	String m9BankLoanLongestDays = m9BankLoan.getString("longestDays");
                	String m9BankLoanLongestDaysTime = m9BankLoan.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM9BankLoanLongestDays(m9BankLoanLongestDays);
                	tblPaOverdueDetail.setM9BankLoanLongestDaysTime(m9BankLoanLongestDaysTime);
                	tblPaOverdueDetail.setM9BankLoanMaxAmount(m9BankLoanMaxAmount);
                	tblPaOverdueDetail.setM9BankLoanOrgNums(m9BankLoanOrgNums);
                	tblPaOverdueDetail.setM9BankLoanRecordNums(m9BankLoanRecordNums);        			
        		}
        		
        		// bankCredit
            	JSONObject m9BankCredit = m9.getJSONObject("bankCredit");
            	if (null != m9BankCredit) {
            		String m9BankCreditRecordNums = m9BankCredit.getString("recordNums");
                	String m9BankCreditOrgNums = m9BankCredit.getString("orgNums");
                	String m9BankCreditMaxAmount = m9BankCredit.getString("maxAmount");
                	String m9BankCreditLongestDays = m9BankCredit.getString("longestDays");
                	String m9BankCreditLongestDaysTime = m9BankCredit.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM9BankCreditLongestDays(m9BankCreditLongestDays);
                	tblPaOverdueDetail.setM9BankCreditLongestDaysTime(m9BankCreditLongestDaysTime);
                	tblPaOverdueDetail.setM9BankCreditMaxAmount(m9BankCreditMaxAmount);
                	tblPaOverdueDetail.setM9BankCreditOrgNums(m9BankCreditOrgNums);
                	tblPaOverdueDetail.setM9BankCreditRecordNums(m9BankCreditRecordNums);            		
            	}
            	
            	// otherLoan
            	JSONObject m9OtherLoan = m9.getJSONObject("otherLoan");
            	if (null != m9OtherLoan) {
            		String m9OtherLoanRecordNums = m9OtherLoan.getString("recordNums");
                	String m9OtherLoanOrgNums = m9OtherLoan.getString("orgNums");
                	String m9OtherLoanMaxAmount = m9OtherLoan.getString("maxAmount");
                	String m9OtherLoanLongestDays = m9OtherLoan.getString("longestDays");
                	String m9OtherLoanLongestDaysTime = m9OtherLoan.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM9OtherLoanLongestDays(m9OtherLoanLongestDays);
                	tblPaOverdueDetail.setM9OtherLoanLongestDaysTime(m9OtherLoanLongestDaysTime);
                	tblPaOverdueDetail.setM9OtherLoanMaxAmount(m9OtherLoanMaxAmount);
                	tblPaOverdueDetail.setM9OtherLoanOrgNums(m9OtherLoanOrgNums);
                	tblPaOverdueDetail.setM9OtherLoanRecordNums(m9OtherLoanRecordNums);            		
            	}
            	
            	// otherCredit
            	JSONObject m9OtherCredit = m9.getJSONObject("otherCredit");
            	if (null != m9OtherCredit) {
            		String m9OtherCreditRecordNums = m9OtherCredit.getString("recordNums");
                	String m9OtherCreditOrgNums = m9OtherCredit.getString("orgNumes");
                	String m9OtherCreditMaxAmount = m9OtherCredit.getString("maxAmount");
                	String m9OtherCreditLongestDays = m9OtherCredit.getString("longestDays");
                	String m9OtherCreditLongestDaysTime = m9OtherCredit.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM9OtherCreditLongestDays(m9OtherCreditLongestDays);
                	tblPaOverdueDetail.setM9OtherCreditLongestDaysTime(m9OtherCreditLongestDaysTime);
                	tblPaOverdueDetail.setM9OtherCreditMaxAmount(m9OtherCreditMaxAmount);
                	tblPaOverdueDetail.setM9OtherCreditOrgNums(m9OtherCreditOrgNums);
                	tblPaOverdueDetail.setM9OtherCreditRecordNums(m9OtherCreditRecordNums);            		
            	}
        	}
        	
        	// m12
        	JSONObject m12 =  classification.getJSONObject("M12");
        	if (null != m12) {
        		// bankLoan
        		JSONObject m12BankLoan = m12.getJSONObject("bankLoan");
        		if (null != m12BankLoan) {
        			String m12BankLoanRecordNums = m12BankLoan.getString("recordNums");
                	String m12BankLoanOrgNums = m12BankLoan.getString("orgNums");
                	String m12BankLoanMaxAmount = m12BankLoan.getString("maxAmount");
                	String m12BankLoanLongestDays = m12BankLoan.getString("longestDays");
                	String m12BankLoanLongestDaysTime = m12BankLoan.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM12BankLoanLongestDays(m12BankLoanLongestDays);
                	tblPaOverdueDetail.setM12BankLoanLongestDaysTime(m12BankLoanLongestDaysTime);
                	tblPaOverdueDetail.setM12BankLoanMaxAmount(m12BankLoanMaxAmount);
                	tblPaOverdueDetail.setM12BankLoanOrgNums(m12BankLoanOrgNums);
                	tblPaOverdueDetail.setM12BankLoanRecordNums(m12BankLoanRecordNums);
        		}
        		
        		// bankCredit
            	JSONObject m12BankCredit = m12.getJSONObject("bankCredit");
            	if (null != m12BankCredit) {
            		String m12BankCreditRecordNums = m12BankCredit.getString("recordNums");
                	String m12BankCreditOrgNums = m12BankCredit.getString("orgNums");
                	String m12BankCreditMaxAmount = m12BankCredit.getString("maxAmount");
                	String m12BankCreditLongestDays = m12BankCredit.getString("longestDays");
                	String m12BankCreditLongestDaysTime = m12BankCredit.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM12BankCreditLongestDays(m12BankCreditLongestDays);
                	tblPaOverdueDetail.setM12BankCreditLongestDaysTime(m12BankCreditLongestDaysTime);
                	tblPaOverdueDetail.setM12BankCreditMaxAmount(m12BankCreditMaxAmount);
                	tblPaOverdueDetail.setM12BankCreditOrgNums(m12BankCreditOrgNums);
                	tblPaOverdueDetail.setM12BankCreditRecordNums(m12BankCreditRecordNums);            		
            	}
            	
            	// otherLoan
            	JSONObject m12OtherLoan = m12.getJSONObject("otherLoan");
            	if (null != m12OtherLoan) {
            		String m12OtherLoanRecordNums = m12OtherLoan.getString("recordNums");
                	String m12OtherLoanOrgNums = m12OtherLoan.getString("orgNums");
                	String m12OtherLoanMaxAmount = m12OtherLoan.getString("maxAmount");
                	String m12OtherLoanLongestDays = m12OtherLoan.getString("longestDays");
                	String m12OtherLoanLongestDaysTime = m12OtherLoan.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM12OtherLoanLongestDays(m12OtherLoanLongestDays);
                	tblPaOverdueDetail.setM12OtherLoanLongestDaysTime(m12OtherLoanLongestDaysTime);
                	tblPaOverdueDetail.setM12OtherLoanMaxAmount(m12OtherLoanMaxAmount);
                	tblPaOverdueDetail.setM12OtherLoanOrgNums(m12OtherLoanOrgNums);
                	tblPaOverdueDetail.setM12OtherLoanRecordNums(m12OtherLoanRecordNums);            		
            	}
            	
            	// otherCredit
            	JSONObject m12OtherCredit = m12.getJSONObject("otherCredit");
            	if (null != m12OtherCredit) {
            		String m12OtherCreditRecordNums = m12OtherCredit.getString("recordNums");
                	String m12OtherCreditOrgNums = m12OtherCredit.getString("orgNumes");
                	String m12OtherCreditMaxAmount = m12OtherCredit.getString("maxAmount");
                	String m12OtherCreditLongestDays = m12OtherCredit.getString("longestDays");
                	String m12OtherCreditLongestDaysTime = m12OtherCredit.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM12OtherCreditLongestDays(m12OtherCreditLongestDays);
                	tblPaOverdueDetail.setM12OtherCreditLongestDaysTime(m12OtherCreditLongestDaysTime);
                	tblPaOverdueDetail.setM12OtherCreditMaxAmount(m12OtherCreditMaxAmount);
                	tblPaOverdueDetail.setM12OtherCreditOrgNums(m12OtherCreditOrgNums);
                	tblPaOverdueDetail.setM12OtherCreditRecordNums(m12OtherCreditRecordNums);		
            	}
        	}
        	
        	// m24
        	JSONObject m24 =  classification.getJSONObject("M24");
        	if (null != m24) {
        		// bankLoan
        		JSONObject m24BankLoan = m24.getJSONObject("bankLoan");
        		if (null != m24BankLoan) {
        			String m24BankLoanRecordNums = m24BankLoan.getString("recordNums");
                	String m24BankLoanOrgNums = m24BankLoan.getString("orgNums");
                	String m24BankLoanMaxAmount = m24BankLoan.getString("maxAmount");
                	String m24BankLoanLongestDays = m24BankLoan.getString("longestDays");
                	String m24BankLoanLongestDaysTime = m24BankLoan.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM24BankLoanLongestDays(m24BankLoanLongestDays);
                	tblPaOverdueDetail.setM24BankLoanLongestDaysTime(m24BankLoanLongestDaysTime);
                	tblPaOverdueDetail.setM24BankLoanMaxAmount(m24BankLoanMaxAmount);
                	tblPaOverdueDetail.setM24BankLoanOrgNums(m24BankLoanOrgNums);
                	tblPaOverdueDetail.setM24BankLoanRecordNums(m24BankLoanRecordNums);		
        		}
        		
        		// bankCredit
            	JSONObject m24BankCredit = m24.getJSONObject("bankCredit");
            	if (null != m24BankCredit) {
            		String m24BankCreditRecordNums = m24BankCredit.getString("recordNums");
                	String m24BankCreditOrgNums = m24BankCredit.getString("orgNums");
                	String m24BankCreditMaxAmount = m24BankCredit.getString("maxAmount");
                	String m24BankCreditLongestDays = m24BankCredit.getString("longestDays");
                	String m24BankCreditLongestDaysTime = m24BankCredit.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM24BankCreditLongestDays(m24BankCreditLongestDays);
                	tblPaOverdueDetail.setM24BankCreditLongestDaysTime(m24BankCreditLongestDaysTime);
                	tblPaOverdueDetail.setM24BankCreditMaxAmount(m24BankCreditMaxAmount);
                	tblPaOverdueDetail.setM24BankCreditOrgNums(m24BankCreditOrgNums);
                	tblPaOverdueDetail.setM24BankCreditRecordNums(m24BankCreditRecordNums);		
            	}
            	
            	// otherLoan
            	JSONObject m24OtherLoan = m24.getJSONObject("otherLoan");
            	if (null != m24OtherLoan) {
            		String m24OtherLoanRecordNums = m24OtherLoan.getString("recordNums");
                	String m24OtherLoanOrgNums = m24OtherLoan.getString("orgNums");
                	String m24OtherLoanMaxAmount = m24OtherLoan.getString("maxAmount");
                	String m24OtherLoanLongestDays = m24OtherLoan.getString("longestDays");
                	String m24OtherLoanLongestDaysTime = m24OtherLoan.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM24OtherLoanLongestDays(m24OtherLoanLongestDays);
                	tblPaOverdueDetail.setM24OtherLoanLongestDaysTime(m24OtherLoanLongestDaysTime);
                	tblPaOverdueDetail.setM24OtherLoanMaxAmount(m24OtherLoanMaxAmount);
                	tblPaOverdueDetail.setM24OtherLoanOrgNums(m24OtherLoanOrgNums);
                	tblPaOverdueDetail.setM24OtherLoanRecordNums(m24OtherLoanRecordNums);            		
            	}
            	
            	// otherCredit
            	JSONObject m24OtherCredit = m24.getJSONObject("otherCredit");   
            	if (null != m24OtherCredit) {
            		String m24OtherCreditRecordNums = m24OtherCredit.getString("recordNums");
                	String m24OtherCreditOrgNums = m24OtherCredit.getString("orgNumes");
                	String m24OtherCreditMaxAmount = m24OtherCredit.getString("maxAmount");
                	String m24OtherCreditLongestDays = m24OtherCredit.getString("longestDays");
                	String m24OtherCreditLongestDaysTime = m24OtherCredit.getString("longestDaysTime");
                	
                	tblPaOverdueDetail.setM24OtherCreditLongestDays(m24OtherCreditLongestDays);
                	tblPaOverdueDetail.setM24OtherCreditLongestDaysTime(m24OtherCreditLongestDaysTime);
                	tblPaOverdueDetail.setM24OtherCreditMaxAmount(m24OtherCreditMaxAmount);
                	tblPaOverdueDetail.setM24OtherCreditOrgNums(m24OtherCreditOrgNums);
                	tblPaOverdueDetail.setM24OtherCreditRecordNums(m24OtherCreditRecordNums);
            	}
        	}
        	
        	
        	// 插入数据库
//        	TblPaOverdue overdue = new TblPaOverdue();
//        	overdue.setPhone(phone);
//        	overdue.setApiQueryDate(apiQueryDate);
//        	overdue.setReturnValue(returnData.toJSONString());
//        	tblPaOverdueDao.addPaOverdue(overdue);
        	
        	tblPaOverdueDetailDao.addPaOverdueDetail(tblPaOverdueDetail);
        	
        	returnObject.put("overdue", JSON.toJSON(tblPaOverdueDetail));
        }
        
        /** 凭安借贷查询 */
        sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/b/loanClassify?");
		sb.append("pname=" + Constants.pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&phone=" + phone);
		sb.append("&uid=" + uid);
		
		resource = client.resource(sb.toString());
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		result = StringUtil.decodeUnicode(result);
		resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        api_result = resultObject.getInteger("result");
        
        // 判断result
        if (0 == api_result) {
        	isSuccess = true;
        	
        	JSONObject data = resultObject.getJSONObject("data");
        	JSONArray record = data.getJSONArray("record");
//        	JSONArray classification = JSONArray.parseArray(JSONArray.toJSONString(record.get(0)));
        	JSONObject record0 = (JSONObject) JSON.toJSON(record.get(0));
        	JSONArray classification = JSONArray.parseArray(record0.getString("classification"));
        	
        	JSONObject m3 = null;
        	JSONObject m6 = null;
        	JSONObject m9 = null;
        	JSONObject m12 = null;
        	JSONObject m24 = null;
        	
        	for (Object object : classification) {
        		
        		JSONObject tempJson = (JSONObject) object;
        		
        		JSONObject tempM3 = tempJson.getJSONObject("M3");
        		JSONObject tempM6 = tempJson.getJSONObject("M6");
        		JSONObject tempM9 = tempJson.getJSONObject("M9");
        		JSONObject tempM12 = tempJson.getJSONObject("M12");
        		JSONObject tempM24 = tempJson.getJSONObject("M24");
        		
        		if (null != tempM3) {
        			m3 = tempM3;
        			continue;
        		}
        		
        		if (null != tempM6) {
        			m6 = tempM6;
        			continue;
        		}
        		
        		if (null != tempM9) {
        			m9 = tempM9;
        			continue;
        		}
        		
        		if (null != tempM12) {
        			m12 = tempM12;
        			continue;
        		}
        		
        		if (null != tempM24) {
        			m24 = tempM24;
        			continue;
        		}
        	}
        	
        	
//        	JSONObject m3 = classification.getJSONObject(0).getJSONObject("M3");
//        	JSONObject m6 = classification.getJSONObject(0).getJSONObject("M6");
//        	JSONObject m9 = classification.getJSONObject(0).getJSONObject("M9");
//        	JSONObject m12 = classification.getJSONObject(0).getJSONObject("M12");
//        	JSONObject m24 = classification.getJSONObject(0).getJSONObject("M24");
        	
        	// 插入数据库
        	TblPaLoanDetail tblPaLoanDetail = new TblPaLoanDetail();
        	tblPaLoanDetail.setPhone(phone);
        	
        	// M3
        	if (null != m3) {
        		// other
        		JSONObject m3_other = m3.getJSONObject("other");
        		if (null != m3_other) {
        			String m3_other_orgNums = m3_other.getString("orgNums");
                	String m3_other_loanAmount = m3_other.getString("loanAmount");
                	String m3_other_totalAmount = m3_other.getString("totalAmount");
                	String m3_other_repayAmount = m3_other.getString("repayAmount");
                	String m3_other_latestLoanTime = m3_other.getString("latestLoanTime");
                	
                	tblPaLoanDetail.setM3OtherLatestLoanTime(m3_other_latestLoanTime);
                	tblPaLoanDetail.setM3OtherLoanAmount(m3_other_loanAmount);
                	tblPaLoanDetail.setM3OtherOrgNums(m3_other_orgNums);
                	tblPaLoanDetail.setM3OtherRepayAmount(m3_other_repayAmount);
                	tblPaLoanDetail.setM3OtherTotalAmount(m3_other_totalAmount);
        		}
        		
        		// bank
            	JSONObject m3_bank = m3.getJSONObject("bank");
            	if (null != m3_bank) {
            		String m3_bank_orgNums = m3_bank.getString("orgNums");
                	String m3_bank_loanAmount = m3_bank.getString("loanAmount");
                	String m3_bank_totalAmount = m3_bank.getString("totalAmount");
                	String m3_bank_repayAmount = m3_bank.getString("repayAmount");
                	String m3_bank_latestLoanTime = m3_bank.getString("latestLoanTime");
                	
                	tblPaLoanDetail.setM3BankLatestLoanTime(m3_bank_latestLoanTime);
                	tblPaLoanDetail.setM3BankLoanAmount(m3_bank_loanAmount);
                	tblPaLoanDetail.setM3BankOrgNums(m3_bank_orgNums);
                	tblPaLoanDetail.setM3BankRepayAmount(m3_bank_repayAmount);
                	tblPaLoanDetail.setM3BankTotalAmount(m3_bank_totalAmount);
            	}
        	}
        	
        	// M6
        	if (null != m6) {
        		// other
        		JSONObject m6_other = m6.getJSONObject("other");
        		if (null != m6_other) {
        			String m6_other_orgNums = m6_other.getString("orgNums");
                	String m6_other_loanAmount = m6_other.getString("loanAmount");
                	String m6_other_totalAmount = m6_other.getString("totalAmount");
                	String m6_other_repayAmount = m6_other.getString("repayAmount");
                	String m6_other_latestLoanTime = m6_other.getString("latestLoanTime");
                	
                	tblPaLoanDetail.setM6OtherLatestLoanTime(m6_other_latestLoanTime);
                	tblPaLoanDetail.setM6OtherLoanAmount(m6_other_loanAmount);
                	tblPaLoanDetail.setM6OtherOrgNums(m6_other_orgNums);
                	tblPaLoanDetail.setM6OtherRepayAmount(m6_other_repayAmount);
                	tblPaLoanDetail.setM6OtherTotalAmount(m6_other_totalAmount);
        		}
        		
        		// bank
            	JSONObject m6_bank = m6.getJSONObject("bank");
            	if (null != m6_bank) {
            		String m6_bank_orgNums = m6_bank.getString("orgNums");
                	String m6_bank_loanAmount = m6_bank.getString("loanAmount");
                	String m6_bank_totalAmount = m6_bank.getString("totalAmount");
                	String m6_bank_repayAmount = m6_bank.getString("repayAmount");
                	String m6_bank_latestLoanTime = m6_bank.getString("latestLoanTime");
                	
                	tblPaLoanDetail.setM6BankLatestLoanTime(m6_bank_latestLoanTime);
                	tblPaLoanDetail.setM6BankLoanAmount(m6_bank_loanAmount);
                	tblPaLoanDetail.setM6BankOrgNums(m6_bank_orgNums);
                	tblPaLoanDetail.setM6BankRepayAmount(m6_bank_repayAmount);
                	tblPaLoanDetail.setM6BankTotalAmount(m6_bank_totalAmount);
            	}
        	}
        	
        	// M9
        	if (null != m9) {
        		// other
        		JSONObject m9_other = m9.getJSONObject("other");
        		if (null != m9_other) {
        			String m9_other_orgNums = m9_other.getString("orgNums");
                	String m9_other_loanAmount = m9_other.getString("loanAmount");
                	String m9_other_totalAmount = m9_other.getString("totalAmount");
                	String m9_other_repayAmount = m9_other.getString("repayAmount");
                	String m9_other_latestLoanTime = m9_other.getString("latestLoanTime");
                	
                	tblPaLoanDetail.setM9OtherLatestLoanTime(m9_other_latestLoanTime);
                	tblPaLoanDetail.setM9OtherLoanAmount(m9_other_loanAmount);
                	tblPaLoanDetail.setM9OtherOrgNums(m9_other_orgNums);
                	tblPaLoanDetail.setM9OtherRepayAmount(m9_other_repayAmount);
                	tblPaLoanDetail.setM9OtherTotalAmount(m9_other_totalAmount);
        		}
        		
        		// bank
            	JSONObject m9_bank = m9.getJSONObject("bank");
            	if (null != m9_bank) {
            		String m9_bank_orgNums = m9_bank.getString("orgNums");
                	String m9_bank_loanAmount = m9_bank.getString("loanAmount");
                	String m9_bank_totalAmount = m9_bank.getString("totalAmount");
                	String m9_bank_repayAmount = m9_bank.getString("repayAmount");
                	String m9_bank_latestLoanTime = m9_bank.getString("latestLoanTime");
                	
                	tblPaLoanDetail.setM9BankLatestLoanTime(m9_bank_latestLoanTime);
                	tblPaLoanDetail.setM9BankLoanAmount(m9_bank_loanAmount);
                	tblPaLoanDetail.setM9BankOrgNums(m9_bank_orgNums);
                	tblPaLoanDetail.setM9BankRepayAmount(m9_bank_repayAmount);
                	tblPaLoanDetail.setM9BankTotalAmount(m9_bank_totalAmount);
            	}
        	}
        	
        	// M12
        	if (null != m12) {
        		// other
        		JSONObject m12_other = m12.getJSONObject("other");
        		if (null != m12_other) {
        			String m12_other_orgNums = m12_other.getString("orgNums");
                	String m12_other_loanAmount = m12_other.getString("loanAmount");
                	String m12_other_totalAmount = m12_other.getString("totalAmount");
                	String m12_other_repayAmount = m12_other.getString("repayAmount");
                	String m12_other_latestLoanTime = m12_other.getString("latestLoanTime");
                	
                	tblPaLoanDetail.setM12OtherLatestLoanTime(m12_other_latestLoanTime);
                	tblPaLoanDetail.setM12OtherLoanAmount(m12_other_loanAmount);
                	tblPaLoanDetail.setM12OtherOrgNums(m12_other_orgNums);
                	tblPaLoanDetail.setM12OtherRepayAmount(m12_other_repayAmount);
                	tblPaLoanDetail.setM12OtherTotalAmount(m12_other_totalAmount);
        		}
        		
        		// bank
            	JSONObject m12_bank = m12.getJSONObject("bank");
            	if (null != m12_bank) {
            		String m12_bank_orgNums = m12_bank.getString("orgNums");
                	String m12_bank_loanAmount = m12_bank.getString("loanAmount");
                	String m12_bank_totalAmount = m12_bank.getString("totalAmount");
                	String m12_bank_repayAmount = m12_bank.getString("repayAmount");
                	String m12_bank_latestLoanTime = m12_bank.getString("latestLoanTime");
                	
                	tblPaLoanDetail.setM12BankLatestLoanTime(m12_bank_latestLoanTime);
                	tblPaLoanDetail.setM12BankLoanAmount(m12_bank_loanAmount);
                	tblPaLoanDetail.setM12BankOrgNums(m12_bank_orgNums);
                	tblPaLoanDetail.setM12BankRepayAmount(m12_bank_repayAmount);
                	tblPaLoanDetail.setM12BankTotalAmount(m12_bank_totalAmount);
            	}
        	}
        	
        	
        	// M24
        	if (null != m24) {
        		// other
        		JSONObject m24_other = m24.getJSONObject("other");
        		if (null != m24_other) {
        			String m24_other_orgNums = m24_other.getString("orgNums");
                	String m24_other_loanAmount = m24_other.getString("loanAmount");
                	String m24_other_totalAmount = m24_other.getString("totalAmount");
                	String m24_other_repayAmount = m24_other.getString("repayAmount");
                	String m24_other_latestLoanTime = m24_other.getString("latestLoanTime");
                	
                	tblPaLoanDetail.setM24OtherLatestLoanTime(m24_other_latestLoanTime);
                	tblPaLoanDetail.setM24OtherLoanAmount(m24_other_loanAmount);
                	tblPaLoanDetail.setM24OtherOrgNums(m24_other_orgNums);
                	tblPaLoanDetail.setM24OtherRepayAmount(m24_other_repayAmount);
                	tblPaLoanDetail.setM24OtherTotalAmount(m24_other_totalAmount);
        		}
        		
        		// bank
            	JSONObject m24_bank = m24.getJSONObject("bank");
            	if (null != m24_bank) {
            		String m24_bank_orgNums = m24_bank.getString("orgNums");
                	String m24_bank_loanAmount = m24_bank.getString("loanAmount");
                	String m24_bank_totalAmount = m24_bank.getString("totalAmount");
                	String m24_bank_repayAmount = m24_bank.getString("repayAmount");
                	String m24_bank_latestLoanTime = m24_bank.getString("latestLoanTime");
                	
                	tblPaLoanDetail.setM24BankLatestLoanTime(m24_bank_latestLoanTime);
                	tblPaLoanDetail.setM24BankLoanAmount(m24_bank_loanAmount);
                	tblPaLoanDetail.setM24BankOrgNums(m24_bank_orgNums);
                	tblPaLoanDetail.setM24BankRepayAmount(m24_bank_repayAmount);
                	tblPaLoanDetail.setM24BankTotalAmount(m24_bank_totalAmount);
            	}
        	}
        	
        	
        	// 插入数据库 
//        	TblPaLoan loan = new TblPaLoan();
//        	loan.setPhone(phone);
//        	loan.setApiQueryDate(apiQueryDate);
//        	loan.setReturnValue(resultObject.getString("data"));
//        	tblPaLoanDao.addPaLoan(loan);
        	
        	tblPaLoanDetailDao.addPaLoanDetail(tblPaLoanDetail);
        	
        	returnObject.put("loan", JSON.toJSON(tblPaLoanDetail));
        }
        
        /** 凭安黑名单查询 */
        sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/b/blacklist?");
		sb.append("pname=" + Constants.pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&phone=" + phone);
		sb.append("&uid=" + uid);
		
		resource = client.resource(sb.toString());
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		result = StringUtil.decodeUnicode(result);
		resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        api_result = resultObject.getInteger("result");
        
        // 判断result
        if (0 == api_result) {
        	isSuccess = true;
        	
        	// 插入数据库 
//        	TblPaBlackList blackList = new TblPaBlackList();
//        	blackList.setPhone(phone);
//        	blackList.setApiQueryDate(apiQueryDate);
//        	blackList.setReturnValue(resultObject.getString("data"));
//        	tblPaBlackListDao.addPaBlackList(blackList);
        	
        	JSONArray others = resultObject.getJSONObject("data").getJSONArray("others");
        	JSONArray returnArray = new JSONArray();
        	
        	for (Object object : others) {
        		JSONObject returnData = (JSONObject) JSON.toJSON(object);
        		TblPaBlackListDetail tblPaBlackListDetail = new TblPaBlackListDetail();
        		tblPaBlackListDetail.setPhone(phone);
        		tblPaBlackListDetail.setBankLostContact(returnData.getString("bankLostContact"));
        		tblPaBlackListDetail.setOrgLostContact(returnData.getString("orgLostContact"));
        		tblPaBlackListDetail.setOrgOverduePeriod(returnData.getString("orgOverduePeriod"));
        		tblPaBlackListDetail.setSeriousOverdueTime(returnData.getString("seriousOverdueTime"));
        		tblPaBlackListDetail.setBankOverduePeriod(returnData.getString("bankOverduePeriod"));
        		tblPaBlackListDetail.setOrgLitigation(returnData.getString("orgLitigation"));
        		tblPaBlackListDetail.setBankLitigation(returnData.getString("bankLitigation"));
        		tblPaBlackListDetail.setDuntelCallTime(returnData.getString("duntelCallTime"));
        		tblPaBlackListDetail.setOrgOneMonthOvedue(returnData.getString("orgOneMonthOvedue"));
        		tblPaBlackListDetail.setOrgBlackNum(String.valueOf(returnData.getJSONArray("orgBlackList").size()));
        		
        		tblPaBlackListDetailDao.addPaBlackListDetail(tblPaBlackListDetail);
        		
        		returnArray.add(tblPaBlackListDetail);
        	}
        	
        	returnObject.put("blackList", returnArray.toJSONString());
        }
        
        
        /** 凭安个人属性查询 */
        sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/biz/phkjModelerScore?");
		sb.append("pname=" + Constants.pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&phone=" + phone);
		sb.append("&uid=" + uid);
		
		resource = client.resource(sb.toString());
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		result = StringUtil.decodeUnicode(result);
		resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        api_result = resultObject.getInteger("result");
        
        // 判断result
        if (0 == api_result) {
        	isSuccess = true;
        	// 插入数据库
//        	TblPaScore score = new TblPaScore();
//        	score.setPhone(phone);
//        	score.setApiQueryDate(apiQueryDate);
//        	score.setReturnValue(resultObject.getString("data"));
//        	tblPaScoreDao.addPaScore(score);
        	
        	JSONObject sqrsx = resultObject.getJSONObject("data");
        	JSONObject generalRecord = sqrsx.getJSONObject("generalRecord");
        	
        	JSONObject returnData = new JSONObject();
        	
        	String CR_TR_TR_LM24 = generalRecord.getString("CR_TR_TR_LM24");
        	String CR_CC_CS_LM03 = generalRecord.getString("CR_CC_CS_LM03");
        	String CR_PS_MC_LM24 = generalRecord.getString("CR_PS_MC_LM24");
        	String CD_CC_AL_LM12 = generalRecord.getString("CD_CC_AL_LM12");
        	String CR_DC_OGO2_LM12 = generalRecord.getString("CR_DC_OGO2_LM12");
        	String CD_AL_IS_LM24 = generalRecord.getString("CD_AL_IS_LM24");
        	String CR_EX_EP_LM06 = generalRecord.getString("CR_EX_EP_LM06");
        	
        	returnData.put("CR_TR_TR_LM24", CR_TR_TR_LM24);
        	returnData.put("CR_CC_CS_LM03", CR_CC_CS_LM03);
        	returnData.put("CR_PS_MC_LM24", CR_PS_MC_LM24);
        	returnData.put("CD_CC_AL_LM12", CD_CC_AL_LM12);
        	returnData.put("CR_DC_OGO2_LM12", CR_DC_OGO2_LM12);
        	returnData.put("CD_AL_IS_LM24", CD_AL_IS_LM24);
        	returnData.put("CR_EX_EP_LM06", CR_EX_EP_LM06);
        	
        	TblPaScoreDetail tblPaScoreDetail = new TblPaScoreDetail();
        	tblPaScoreDetail.setPhone(phone);
        	tblPaScoreDetail.setCr_tr_tr_lm24(CR_TR_TR_LM24);
        	tblPaScoreDetail.setCr_cc_cs_lm03(CR_CC_CS_LM03);
        	tblPaScoreDetail.setCr_ps_mc_lm24(CR_PS_MC_LM24);
        	tblPaScoreDetail.setCd_cc_al_lm12(CD_CC_AL_LM12);
        	tblPaScoreDetail.setCr_dc_ogo2_lm12(CR_DC_OGO2_LM12);
        	tblPaScoreDetail.setCd_al_is_lm24(CD_AL_IS_LM24);
        	tblPaScoreDetail.setCr_ex_ep_lm06(CR_EX_EP_LM06);
        	tblPaScoreDetailDao.addPaScoreDetail(tblPaScoreDetail);
        	
        	returnObject.put("score", returnData.toJSONString());
        }
		
  		
        JSONObject returnJson = new JSONObject();
  		returnJson.put("result", returnObject);
  		returnJson.put("isSuccess", isSuccess);
  		
  		
  		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userID);
		apiLog.setLocalApiID(String.valueOf(localApi.getId()));
		// 参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("phone", phone);
		param.put("name", name);
		param.put("idCard", idCard);
		apiLog.setParams(JSON.toJSONString(param));
		apiLog.setDataFrom(Constants.DATA_FROM_PA);
		apiLog.setIsSuccess(String.valueOf(isSuccess));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);
  		
		return returnJson;
	}

	/**
	 * 反欺诈服务
	 * @param phone    电话号码
	 * @param name     姓名 
	 * @param idCard   身份证号码
	 * @param userID   用户ID
	 * @param uuid     UUID
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject fraud(String phone, String name, String idCard, String userID, String uuid, LocalApi localApi) throws Exception {
		
		// 等级初始化
		int level = 6;
		// 基本调用数据初始化
		String ptime = String.valueOf(System.currentTimeMillis());
		String vKey = StringUtil.string2MD5(Constants.pn_pkey + "_" + ptime + "_" + Constants.pn_pkey);
		String uid = StringUtil.MD5Encoder(name + idCard, "utf-8");
		// 查询时间初始化
		String apiQueryDate = format.format(new Date());
		// 返回数据初始化
		String result = Constants.BLANK;
		JSONObject returnObject = new JSONObject();
		// 客户端初始化
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		WebResource resource = null;
		int api_result = 0;
		
		 // 查询成功标识
        boolean isSuccess = false;
		
        /** 1.反欺诈识别 */
		/** 凭安电话号码查询 */
		resource = client.resource("https://jrapi.pacra.cn/phonetag?pname=" + Constants.pn_pname + "&vkey=" + vKey + "&ptime=" + ptime + "&phone=" + phone);
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		result = StringUtil.decodeUnicode(result);
		JSONObject resultObject = (JSONObject)JSONObject.parse(result);
        api_result = resultObject.getInteger("result");
        
        // 判断result
        if (0 == api_result) {
        	isSuccess = true;
        	// 只返回欺诈电话
        	JSONArray array = resultObject.getJSONArray("data");
        	
        	for (Object object : array) {
        		JSONObject returnData = (JSONObject) JSON.toJSON(object);
        		String tag = returnData.getString("tag");
            	if (tag.indexOf("欺诈") > 0) {
            		level = 3;
                	// 插入数据库
                	TblPaPhoneTag phoneTag = new TblPaPhoneTag();
                	phoneTag.setPhone(phone);
                	phoneTag.setApiQueryDate(apiQueryDate);
                	phoneTag.setReturnValue(returnData.toJSONString());
                	tblPaPhoneTagDao.addPaPhoneTag(phoneTag);
                	
                	returnObject.put("phoneTag", returnData);
                	break;
            	}
        	}
        	
        }
        
        /** 凭安失信被执行人查询 */
        StringBuilder sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/b/shixin?");
		sb.append("pname=" + Constants.pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&uid=" + uid);
		
		resource = client.resource(sb.toString());
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		result = StringUtil.decodeUnicode(result);
		resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        api_result = resultObject.getInteger("result");
        
        // 判断result
        if (0 == api_result) {
        	isSuccess = true;
        	// 插入数据库
        	TblPaShixin shixin = new TblPaShixin();
        	shixin.setName(name);
        	shixin.setIdCard(idCard);
        	shixin.setApiQueryDate(apiQueryDate);
        	shixin.setReturnValue(resultObject.getString("data"));
        	tblPaShixinDao.addPaShixin(shixin);
        	
        	returnObject.put("shixin", resultObject.getString("data"));
        }
        
        /** 凭安逾期查询 */
		sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/b/overdueClassify?");
		sb.append("pname=" + Constants.pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&phone=" + phone);
		sb.append("&uid=" + uid);
		
		resource = client.resource(sb.toString());
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		result = StringUtil.decodeUnicode(result);
		resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        api_result = resultObject.getInteger("result");
        
        // 判断result
        if (0 == api_result) {
        	isSuccess = true;
        	
        	JSONObject returnData = resultObject.getJSONObject("data");
        	
        	// 计算逾期等级
        	JSONObject record = returnData.getJSONArray("record").getJSONObject(0);
        	JSONObject classification = record.getJSONArray("classification").getJSONObject(0);
        	
        	// m3
        	JSONObject m3 =  classification.getJSONObject("M3");
        	if (null != m3) {
        		level = this.calcOverdueLevel(m3, level);
        	}
        	
        	// m6
        	JSONObject m6 =  classification.getJSONObject("M6");
        	if (null != m6) {
        		level = this.calcOverdueLevel(m6, level);
        	}
        	
        	// m9
        	JSONObject m9 =  classification.getJSONObject("M9");
        	if (null != m9) {
        		level = this.calcOverdueLevel(m9, level);
        	}
        	
        	// m12
        	JSONObject m12 =  classification.getJSONObject("M12");
        	if (null != m12) {
        		level = this.calcOverdueLevel(m12, level);
        	}
        	
        	// m24
        	JSONObject m24 =  classification.getJSONObject("M24");
        	if (null != m24) {
        		level = this.calcOverdueLevel(m24, level);
        	}
        	
        	// 插入数据库
        	TblPaOverdue overdue = new TblPaOverdue();
        	overdue.setName(name);
        	overdue.setIdCard(idCard);
        	overdue.setPhone(phone);
        	overdue.setApiQueryDate(apiQueryDate);
        	overdue.setReturnValue(returnData.toJSONString());
        	tblPaOverdueDao.addPaOverdue(overdue);
        	
        	returnObject.put("overdue", returnData);
        }
        
        /** 凭安借贷查询 */
        sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/b/loanClassify?");
		sb.append("pname=" + Constants.pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&phone=" + phone);
		sb.append("&uid=" + uid);
		
		resource = client.resource(sb.toString());
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		result = StringUtil.decodeUnicode(result);
		resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        api_result = resultObject.getInteger("result");
        
        // 判断result
        if (0 == api_result) {
        	isSuccess = true;
        	// 插入数据库
        	TblPaLoan loan = new TblPaLoan();
        	loan.setName(name);
        	loan.setIdCard(idCard);
        	loan.setPhone(phone);
        	loan.setApiQueryDate(apiQueryDate);
        	loan.setReturnValue(resultObject.getString("data"));
        	tblPaLoanDao.addPaLoan(loan);
        	
        	returnObject.put("loan", resultObject.getString("data"));
        }
        
        /** 凭安黑名单查询 */
        sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/b/blacklist?");
		sb.append("pname=" + Constants.pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&phone=" + phone);
		sb.append("&uid=" + uid);
		
		resource = client.resource(sb.toString());
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		result = StringUtil.decodeUnicode(result);
		resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        api_result = resultObject.getInteger("result");
        
        // 判断result
        if (0 == api_result) {
        	isSuccess = true;
        	
        	if (level > 2) {
        		level = 2;
        	}
        	
        	// 插入数据库
        	TblPaBlackList blackList = new TblPaBlackList();
        	blackList.setName(name);
        	blackList.setIdCard(idCard);
        	blackList.setPhone(phone);
        	blackList.setApiQueryDate(apiQueryDate);
        	blackList.setReturnValue(resultObject.getString("data"));
        	tblPaBlackListDao.addPaBlackList(blackList);
        	
        	returnObject.put("blackList", resultObject.getString("data"));
        }
        
        // 查询yingze黑名单
        // 查询内码是否存在
     	Map<String, Object> insideCodeMap = null;
        // 根据两标进行查询
     	insideCodeMap =  zhIdentificationDao.getInnerID(name, idCard);
     	// 判断内码是否为空
 		if (null != insideCodeMap) {
 			// 获取内码
 	 		String innerId = String.valueOf(insideCodeMap.get("INNERID"));
 	 		// 查询黑名单
 			Map<String, Object> blackList = null;
 			blackList = blackListDao.getList(innerId);
 			// 判断黑名单是否为空
 			if (null != blackList) {
 				String grade = String.valueOf(blackList.get("GRADE"));
 				if (!StringUtil.isNull(grade)) {
 					if ("1".equals(grade)) {
 						if (level > 3) {
 							level = 3;
 						}
 					} else if ("2".equals(grade)) {
 						if (level > 2) {
 							level = 2;
 						}
 					}
 				}
 			}
 		}
 		
        
        /** 凭安个人属性查询 */
        sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/biz/phkjModelerScore?");
		sb.append("pname=" + Constants.pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&phone=" + phone);
		sb.append("&uid=" + uid);
		
		resource = client.resource(sb.toString());
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		result = StringUtil.decodeUnicode(result);
		resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        api_result = resultObject.getInteger("result");
        
        // 判断result
        if (0 == api_result) {
        	isSuccess = true;
        	// 插入数据库
        	TblPaScore score = new TblPaScore();
        	score.setName(name);
        	score.setIdCard(idCard);
        	score.setPhone(phone);
        	score.setApiQueryDate(apiQueryDate);
        	score.setReturnValue(resultObject.getString("data"));
        	tblPaScoreDao.addPaScore(score);
        	
        	returnObject.put("score", resultObject.getString("data"));
        }
		
        // 如果全未命中,level置为7
        if (!isSuccess) {
        	level = 7;
        }
        
        String levelStr = this.changeLevel(level);
        
        /** 2.计算分数 */
        // 信用分初始化
	 	double score = 71;
        // 判断内码是否为空
  		if (null != insideCodeMap) {
  			// 获取内码
  	 		String innerId = String.valueOf(insideCodeMap.get("INNERID"));
  	 		// 根据内码查询信用分
  	 		List<Map<String, Object>> scoreList = scoreDao.getScore(innerId);
  			if (null != scoreList && 0 != scoreList.size()) {
  				Map<String, Object> scoreMap = scoreList.get(0);
  				if (null != scoreMap.get("SCORE")) {
  					score = Double.parseDouble(String.valueOf(scoreMap.get("SCORE")));
  				}
  			}
  		}
  		score = this.calcScore(score, levelStr);
  		
  		/** 3.计算额度 */
  		String quota = this.calcQuota(score);
        
  		returnObject.put("yz_level", levelStr);
  		returnObject.put("yz_quota", quota);
  		returnObject.put("yz_score", score);
  		
        JSONObject returnJson = new JSONObject();
  		returnJson.put("result", returnObject);
  		returnJson.put("isSuccess", isSuccess);
  		
  		
  		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userID);
		apiLog.setLocalApiID(String.valueOf(localApi.getId()));
		// 参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("name", name);
		param.put("idCard", idCard);
		param.put("phone", phone);
		apiLog.setParams(JSON.toJSONString(param));
		apiLog.setDataFrom(Constants.DATA_FROM_PA);
		apiLog.setIsSuccess(String.valueOf(isSuccess));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);
  		
		return returnJson;
	}
	
	/**
	 * 反欺诈服务用
	 * @param object
	 * @param level
	 * @return
	 */
	private int calcOverdueLevel(JSONObject object, int level) {
		
		JSONObject bankLoan = object.getJSONObject("bankLoan");
		JSONObject bankCredit = object.getJSONObject("bankCredit");
		JSONObject otherLoan = object.getJSONObject("otherLoan");
		JSONObject otherCredit = object.getJSONObject("otherCredit");
		
		if (null != bankLoan) {
			int bankLoanDays = bankLoan.getIntValue("longestDays");
			if (bankLoanDays >= 180 && level > 2) {
    			level = 2;
    		} else if ((bankLoanDays < 180 && bankLoanDays > 90) && level > 3) {
    			level = 3;
    		} else {
    			if (level > 4) {
    				level = 4;	
    			}
    		}
		}
		if (null != bankCredit) {
			int bankCreditDays = bankCredit.getIntValue("longestDays");
			if (bankCreditDays >= 180 && level > 2) {
    			level = 2;
    		} else if ((bankCreditDays < 180 && bankCreditDays > 90) && level > 3) {
    			level = 3;
    		} else {
    			if (level > 4) {
    				level = 4;	
    			}
    		}
		}
		if (null != otherLoan) {
			int otherLoanDays = otherLoan.getIntValue("longestDays");
			if (otherLoanDays >= 30 && level > 2) {
    			level = 2;
    		} else if ((otherLoanDays >= 15 && otherLoanDays < 30) && level > 3) {
    			level = 3;
    		} else {
    			if (level > 4) {
    				level = 4;	
    			}
    		}
		}
		if (null != otherCredit) {
			int otherCreditDays = otherCredit.getIntValue("longestDays");
			if (otherCreditDays >= 30 && level > 2) {
    			level = 2;
    		} else if ((otherCreditDays >= 15 && otherCreditDays < 30) && level > 3) {
    			level = 3;
    		} else {
    			if (level > 4) {
    				level = 4;	
    			}
    		}
		}
		
		return level;
	}
	
	/**
	 * 反欺诈服务用
	 * @param level
	 * @return
	 */
	private String changeLevel(int level) {
		
		String returnValue = Constants.BLANK;
		
		switch (level) {
			case 1 : 
				returnValue = "A";
				break;
			case 2 : 
				returnValue = "B";
				break;
			case 3 : 
				returnValue = "C";
				break;
			case 4 : 
				returnValue = "D";
				break;
			case 5 : 
				returnValue = "E";
				break;
			case 6 : 
				returnValue = "F";
				break;
			default :
				break;
		}
		
		return returnValue;
	}
	
	/**
	 * 反欺诈服务用
	 * 无分数→71分
	 * A →0
	 * B C →0+原分×0.2取整
	 * D→ 20+原分×0.2取整
	 * E→ 40+原分×0.2取整
	 * F 原分小于70取60+原分×0.2，大于70取原分
	 * @param score
	 * @param level
	 * @return
	 */
	private double calcScore(double score, String level) {
		
		switch (level) {
			case "A" : 
				score = 0;
				break;
			case "B" : 
				score = 0 + (int)Math.round(score * 0.2);
				break;
			case "c" : 
				score = 0 + (int)Math.round(score * 0.2);
				break;
			case "D" : 
				score = 20 + (int)Math.round(score * 0.2);
				break;
			case "E" : 
				score = 40 + (int)Math.round(score * 0.2);
				break;
			case "F" : 
				if (score < 70) {
					score = 60 + (int)Math.round(score * 0.2);
				}
				break;
			default :
				break;
		}
		
		return score;
	}
	
	/**
	 * 反欺诈服务用
	 * 计算额度
	 * 额度80→A，65~80→B，45~65~C，0~45→D
	 * @param score
	 * @return
	 */
	private String calcQuota(double score) {
		
		String quota = Constants.BLANK;
		
		if (score > 80) {
			quota = "A";
		} else if (score >= 65 && score < 80) {
			quota = "B";
		} else if (score >= 45 && score < 65) {
			quota = "C";
		} else {
			quota = "D";
		}
		
		return quota;
	}
	
	/**
	 * 信用卡申请风控
	 * @param phone    电话号码
	 * @param phonepassword  电话服务密码
	 * @param name     姓名 
	 * @param idCard   身份证号码
	 * @param userID   用户ID
	 * @param uuid     UUID
	 * @return
	 * @throws Exception
	 */
	public JSONObject reditCardApplyRiskControl(String phone, String phonePassword, 
			String name, String idCard, String userID, String uuid, LocalApi localApi) throws Exception {
		
		// 查询时间初始化
		String apiQueryDate = format.format(new Date());
		// 返回数据初始化
		JSONObject returnObject = new JSONObject();
		boolean isSuccess = false;
		JSONObject resultObject = null;
		
		/** 1.风险涉诉 */
		String sign =  XzsImplementation.getSign(name, idCard);
		String fxResult = XzsImplementation.getFX006(name, idCard, sign);
		resultObject = (JSONObject)JSONObject.parse(fxResult);
		String resCode = resultObject.getString("resCode");
		if ("0000".equals(resCode)) {
			isSuccess = true;
			// 插入数据库
			TblXzsFx006 xzsFx006 = new TblXzsFx006();
			xzsFx006.setIdCard(idCard);
			xzsFx006.setName(name);
			xzsFx006.setApiQueryDate(apiQueryDate);
			xzsFx006.setReturnValue(resultObject.getString("data"));
			tblXzsFx006Dao.addFx006(xzsFx006);
			returnObject.put("fxss", resultObject.getString("data"));
		}
		
		/** 电话详单 */
		String phoneResult = HuluImplementation.service_getRawdata(name, idCard, phone, phonePassword);
		// 判断返回结果是否正常
		if (!StringUtil.isNull(phoneResult)) {
			resultObject = (JSONObject)JSONObject.parse(phoneResult);
			String code = resultObject.getString("code");
			String code_description = resultObject.getString("code_description");
			
			if ("16387".equals(code) && "DATA_RAW_ACCESS_SUCCESS".equals(code_description)) {
				isSuccess = true;
				// 插入数据库
				TblHuluPhoneDetail huluPhoneDetail = new TblHuluPhoneDetail();
				huluPhoneDetail.setIdCard(idCard);
				huluPhoneDetail.setName(name);
				huluPhoneDetail.setPhone(phone);
				huluPhoneDetail.setPhonePassword(phonePassword);
				huluPhoneDetail.setApiQueryDate(apiQueryDate);
				huluPhoneDetail.setReturnValue(resultObject.getString("data"));
				tblHuluPhoneDetailDao.addPhoneDetail(huluPhoneDetail);
				returnObject.put("phoneDetail", resultObject.getString("data"));
			}
		} 
		
		/** 唯品会 */
		// TODO
		returnObject.put("blackList", Constants.BLANK);
		
		JSONObject returnJson = new JSONObject();
  		returnJson.put("result", returnObject);
  		returnJson.put("isSuccess", isSuccess);
  		
  		
  		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userID);
		apiLog.setLocalApiID(String.valueOf(localApi.getId()));
		// 参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("name", name);
		param.put("idCard", idCard);
		param.put("phone", phone);
		apiLog.setParams(JSON.toJSONString(param));
		apiLog.setDataFrom(Constants.DATA_FROM_HULU 
				+ Constants.COMMA + Constants.DATA_FROM_XINZHISHANG 
				+ Constants.COMMA + Constants.DATA_FROM_VIP);
		apiLog.setIsSuccess(String.valueOf(isSuccess));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);
  		
		return returnJson;
	}
	
	/**
	 * 获取动态码(信用卡申请风控-电话详情用)
	 * @param phone    电话号码
	 * @param name     姓名 
	 * @param idCard   身份证号码
	 * @return
	 * @throws Exception
	 */
	public JSONObject getDynamicCode(String phone, String name, String idCard) throws Exception {
	
		String token = HuluImplementation.service_getDynamicCode(name, idCard, phone);
		
		JSONObject object = new JSONObject();
		object.put("token", token);
		
		return object;
	}
	
	/**
	 * 重置密码(信用卡申请风控-电话详情用)
	 * @param token     获取动态码时返回的token
	 * @param password  要重置的密码 
	 * @param captcha   动态码
	 * @return
	 * @throws Exception
	 */
	public JSONObject resetPassword(String token, String password, String captcha) throws Exception {
	
		String result = HuluImplementation.service_resetPassword(token, password, captcha);
		
		JSONObject resultObject = JSON.parseObject(result);
		
		String code_description = resultObject.getString("code_description");
		
		JSONObject object = new JSONObject();
		
		if ("RESET_PASSWORD_SUCCESS".equals(code_description)) {
			object.put("isSuccess", true);
		} else {
			object.put("isSuccess", false);
		}
		
		return object;
	}
	
	/**
	 * 一致性验证(身份证与电话号码 )
	 * @param phone    电话号码
	 * @param idCard   身份证号码
	 * @param userID   用户ID
	 * @param uuid     UUID
	 * @return
	 * @throws Exception
	 */
	public JSONObject checkConsistency(String phone, String idCard, String userID, String uuid, LocalApi localApi) throws Exception {
		
		JSONObject returnObject = new JSONObject();
		
		// 查询是否成功
		boolean isSuccess = false;
		// 返回信息
		String result = "一致";
		
		// 查询内码是否存在
     	Map<String, Object> insideCodeMap = null;
        // 根据两标进行查询
     	insideCodeMap =  zhIdentificationDao.getInnerID("", idCard);
     	// 判断内码是否为空
 		if (null != insideCodeMap) {
 			isSuccess = true;
 			// 获取内码
 	 		String innerId = String.valueOf(insideCodeMap.get("INNERID"));
 	 		// 根据内码获取telPerson
 	 		Map<String, Object> telPersonMap = telPersonDao.getTelPerson(innerId);
 	 		// 判断telPerson是否为空
 	 		if (null != telPersonMap) {
 	 			// 获取电话号码
 	 			String telNum = String.valueOf(telPersonMap.get("TELNUM"));
 	 			// 将取得的电话号码与传入的电话号码进行比较
 	 			// 若一致返回是,若不一致返回取得的电话号码
 	 			if (phone.equals(telNum)) {
 	 				// 一致
// 	 				isSuccess = true;
 	 			} else {
 	 				// 不一致
 	 				result = "不一致,电话号码为: " + telNum;
 	 			}
 	 		}
 		} else {
 			// 不一致
 			result = "不一致";
 		}
 		
 		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userID);
		apiLog.setLocalApiID(String.valueOf(localApi.getId()));
		// 参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("phone", phone);
		param.put("idCard", idCard);
		apiLog.setParams(JSON.toJSONString(param));
		apiLog.setDataFrom(Constants.DATA_FROM_PA);
		apiLog.setIsSuccess(String.valueOf(isSuccess));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);
		
		returnObject.put("isSuccess", isSuccess);
		returnObject.put("result", result);
		
		return returnObject;
	}
	
}
