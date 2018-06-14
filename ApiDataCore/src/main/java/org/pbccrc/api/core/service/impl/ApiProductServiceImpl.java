package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.bean.TblHuluPhoneDetail;
import org.pbccrc.api.base.bean.TblPaBlackList;
import org.pbccrc.api.base.bean.TblPaLoan;
import org.pbccrc.api.base.bean.TblPaOverdue;
import org.pbccrc.api.base.bean.TblPaPhoneTag;
import org.pbccrc.api.base.bean.TblPaScore;
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
import org.pbccrc.api.core.dao.TblPaLoanDao;
import org.pbccrc.api.core.dao.TblPaOverdueDao;
import org.pbccrc.api.core.dao.TblPaPhoneTagDao;
import org.pbccrc.api.core.dao.TblPaScoreDao;
import org.pbccrc.api.core.dao.TblPaShixinDao;
import org.pbccrc.api.core.dao.TblXzsFx006Dao;
import org.pbccrc.api.core.dao.ZhIdentificationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
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
	public JSONObject bhyh(String name, String idCard, String userID, String uuid) throws Exception {
		
		boolean isSuccess = false;
		
		JSONObject returnJson = new JSONObject();
		
		JSONObject resultJson = new JSONObject();
		
		// 查询内码是否存在
     	Map<String, Object> insideCodeMap = null;
        // 根据两标进行查询
     	insideCodeMap =  zhIdentificationDao.getInnerID(name, idCard);
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
		apiLog.setLocalApiID(Constants.API_ID_PRODUCT_BHYH);
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
	public JSONObject fraud2(String phone, String name, String idCard, String userID, String uuid) throws Exception {
		
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
        	JSONObject returnData = resultObject.getJSONObject("data");
        	String tag = returnData.getString("tag");
        	if (tag.indexOf("欺诈") > 0) {
        		level = 3;
            	// 插入数据库
            	TblPaPhoneTag phoneTag = new TblPaPhoneTag();
            	phoneTag.setPhone(phone);
            	phoneTag.setApiQueryDate(apiQueryDate);
            	phoneTag.setReturnValue(returnData.toJSONString());
            	tblPaPhoneTagDao.addPaPhoneTag(phoneTag);
        	}
        	
        	returnObject.put("phoneTag", returnData);
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
        	blackList.setPhone(phone);
        	blackList.setApiQueryDate(apiQueryDate);
        	blackList.setReturnValue(resultObject.getString("data"));
        	tblPaBlackListDao.addPaBlackList(blackList);
        	
        	returnObject.put("blackList", resultObject.getString("data"));
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
        	score.setPhone(phone);
        	score.setApiQueryDate(apiQueryDate);
        	score.setReturnValue(resultObject.getString("data"));
        	tblPaScoreDao.addPaScore(score);
        	
        	JSONObject sqrsx = resultObject.getJSONObject("data");
        	
        	JSONObject generalRecord = sqrsx.getJSONObject("generalRecord");
        	generalRecord.remove("CR_DC_OGO02_LM12");
        	generalRecord.remove("CD_EX_EP_LM06");
        	generalRecord.remove("CR_TR_TR_LM24");
        	generalRecord.remove("CR_PS_MC_LM24");
        	generalRecord.remove("CD_AL_IS_LM24");
        	
        	returnObject.put("score", sqrsx.toJSONString());
        }
		
        // 如果全未命中,level置为7
        if (!isSuccess) {
        	level = 7;
        }
        
  		
        JSONObject returnJson = new JSONObject();
  		returnJson.put("result", returnObject);
  		returnJson.put("isSuccess", isSuccess);
  		
  		
  		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userID);
		apiLog.setLocalApiID(Constants.API_ID_PRODUCT_ANTI_NEW_FRAUD);
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
	public JSONObject fraud(String phone, String name, String idCard, String userID, String uuid) throws Exception {
		
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
        	JSONObject returnData = resultObject.getJSONObject("data");
        	String tag = returnData.getString("tag");
        	if (tag.indexOf("欺诈") > 0) {
        		level = 3;
            	// 插入数据库
            	TblPaPhoneTag phoneTag = new TblPaPhoneTag();
            	phoneTag.setPhone(phone);
            	phoneTag.setApiQueryDate(apiQueryDate);
            	phoneTag.setReturnValue(returnData.toJSONString());
            	tblPaPhoneTagDao.addPaPhoneTag(phoneTag);
        	}
        	
        	returnObject.put("phoneTag", returnData);
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
	 	int score = 71;
        // 判断内码是否为空
  		if (null != insideCodeMap) {
  			// 获取内码
  	 		String innerId = String.valueOf(insideCodeMap.get("INNERID"));
  	 		// 根据内码查询信用分
  	 		List<Map<String, Object>> scoreList = scoreDao.getScore(innerId);
  			if (null != scoreList && 0 != scoreList.size()) {
  				Map<String, Object> scoreMap = scoreList.get(0);
  				if (null != scoreMap.get("SCORE")) {
  					score = Integer.parseInt(String.valueOf(scoreMap.get("SCORE")));
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
		apiLog.setLocalApiID(Constants.API_ID_PRODUCT_ANTI_FRAUD);
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
	private int calcScore(int score, String level) {
		
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
	private String calcQuota(int score) {
		
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
	public JSONObject reditCardApplyRiskControl(String phone, String phonePassword, String name, String idCard, String userID, String uuid) throws Exception {
		
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
		apiLog.setLocalApiID(Constants.API_ID_PRODUCT_REDIT_CARD_APPLY_RISK_CONTROL);
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
	
}
