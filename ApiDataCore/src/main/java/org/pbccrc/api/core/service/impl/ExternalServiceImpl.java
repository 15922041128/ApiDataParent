package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.bean.TblPaBlackList;
import org.pbccrc.api.base.bean.TblPaLoan;
import org.pbccrc.api.base.bean.TblPaOverdue;
import org.pbccrc.api.base.bean.TblPaPhoneTag;
import org.pbccrc.api.base.bean.TblPaScore;
import org.pbccrc.api.base.bean.TblPaShixin;
import org.pbccrc.api.base.external.vip.grcredit.Conts;
import org.pbccrc.api.base.external.vip.grcredit.RSAUtil;
import org.pbccrc.api.base.service.ExternalService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.dao.ApiLogDao;
import org.pbccrc.api.core.dao.TblPaBlackListDao;
import org.pbccrc.api.core.dao.TblPaLoanDao;
import org.pbccrc.api.core.dao.TblPaOverdueDao;
import org.pbccrc.api.core.dao.TblPaPhoneTagDao;
import org.pbccrc.api.core.dao.TblPaScoreDao;
import org.pbccrc.api.core.dao.TblPaShixinDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


@Service
public class ExternalServiceImpl implements ExternalService{

	/** 凭安 */
	// 凭安pname
	private static String pn_pname = "120180424002";
	// 凭安pkey
	private static String pn_pkey = "4ce9548728a2aa4220af150be5bb162a";
	
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
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 唯品会个人风险查询
	 * @param idNo
	 * @param cardName
	 * @param phone
	 * @param cardNo
	 * @param uuid
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject vipQueryBlackList(String idNo, String cardName, String phone, String cardNo, String userId, String uuid)
			throws Exception {
		
		String result = Constants.BLANK;
		
		String timestamp = System.currentTimeMillis() + "";
		SortedMap<String, Object> sortedMap = new TreeMap<String, Object>();
        sortedMap.put("innerreqId", Conts.appid + timestamp);
        sortedMap.put("idNo", idNo);
        sortedMap.put("cardName", cardName);
        sortedMap.put("phone", phone);
        sortedMap.put("cardNo", cardNo);
	        
        // 请求体信息
        byte[] encryptData;
        String body;
        try {
            body = JSON.toJSONString(sortedMap);
            //RSA 加密
            encryptData = RSAUtil.encryptRSA(body.getBytes(Conts.coding), false, Conts.coding, Conts.publicServiceKey);
            //RSA 签名
            String sign = RSAUtil.generateSign(encryptData, Conts.privateClientKey);

            // 请求头部
            HttpPost httpPost = new HttpPost("https://grcredit-webapp.vip.com/webapp/queryBlackList");
            httpPost.addHeader("apiver", Conts.apiver);
            httpPost.addHeader("appid", Conts.appid);
            httpPost.addHeader("timestamp", timestamp);
            httpPost.addHeader("sign", sign);

            httpPost.addHeader("insCode", Conts.insCode);
            httpPost.addHeader("serviceCode", Conts.serviceCode);

            httpPost.addHeader("nz", Conts.nz);
            httpPost.addHeader("crypt", Conts.crypt);

            httpPost.setEntity(new ByteArrayEntity(encryptData));
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                byte[] content = EntityUtils.toByteArray(entity);

                int status = response.getStatusLine().getStatusCode();
                if (HttpStatus.SC_OK == status) { // 请求成功
                    byte[] desCryptData = RSAUtil.decryptRSA(content, false, Conts.coding, Conts.privateClientKey);
                    result = new String(desCryptData, Conts.coding);
                } else if (HttpStatus.SC_INTERNAL_SERVER_ERROR == status){ // 请求失败
                    result = new String(content, Conts.coding);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JSONObject resultObject = (JSONObject)JSONObject.parse(result);
        
        // get status
        int status = resultObject.getInteger("status");
        // get body
        String resultBody = resultObject.getString("body");
        
        // 查询成功标识
        boolean isSuccess = true;
        
        // 判断status
        if (1 <= status && status <= 999) {
        	// 判断body是否为空
        	if (StringUtil.isNull(resultBody)) {
        		// 失败
        		isSuccess = false;
        	} else {
        		// 判断respCode
        		JSONObject bodyObject = (JSONObject)JSONObject.parse(resultBody);
        		String respCode = bodyObject.getString("respCode");
        		if (!"200".equals(respCode)) {
        			// 失败
        			isSuccess = false;
        		}
        	}
        } else {
        	// 失败
        	isSuccess = false;
        }
        
        // 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userId);
		apiLog.setLocalApiID(Constants.API_ID_VIP_QUERYBLACKLIST);
		// 参数
		apiLog.setParams(JSON.toJSONString(sortedMap));
		apiLog.setDataFrom(Constants.DATA_FROM_VIP);
		apiLog.setIsSuccess(String.valueOf(isSuccess));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("result", result);
		returnJson.put("isSuccess", isSuccess);
        
        return returnJson;
	}
	
	/**
	 * 凭安电话标签查询
	 * @param phone
	 * @param userId
	 * @param uuid
	 * @return
	 * @throws Exception
	 */
	public JSONObject paPhoneTag(String phone, String userId, String uuid) throws Exception {
		
		String result = Constants.BLANK;
		
		String ptime = String.valueOf(System.currentTimeMillis());
		
		String vKey = StringUtil.string2MD5(pn_pkey + "_" + ptime + "_" + pn_pkey);
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		WebResource resource = client.resource("https://jrapi.pacra.cn/phonetag?pname=" + pn_pname + "&vkey=" + vKey + "&ptime=" + ptime + "&phone=" + phone);
		
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		JSONObject resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        int api_result = resultObject.getInteger("result");
        // get message
        // String api_message = resultObject.getString("message");
        // get data
        // String api_data = resultObject.getString("data");
        
        // 查询成功标识
        boolean isSuccess = true;
        
        // 判断result
        if (0 == api_result || 2 == api_result) {
        	isSuccess = true;
        	// 插入数据库
        	TblPaPhoneTag phoneTag = new TblPaPhoneTag();
        	phoneTag.setPhone(phone);
        	phoneTag.setApiQueryDate(format.format(new Date()));
        	if (0 == api_result) {
        		phoneTag.setReturnValue(resultObject.getString("data"));	
        	} else {
        		phoneTag.setReturnValue(resultObject.getString("message"));
        	}
        	
        	tblPaPhoneTagDao.addPaPhoneTag(phoneTag);
        } else {
        	isSuccess = false;
        }
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userId);
		apiLog.setLocalApiID(Constants.API_ID_PA_PHONE_TAG);
		// 参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("phone", phone);
		apiLog.setParams(JSON.toJSONString(param));
		apiLog.setDataFrom(Constants.DATA_FROM_PA);
		apiLog.setIsSuccess(String.valueOf(isSuccess));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);

		JSONObject returnJson = new JSONObject();
		returnJson.put("result", result);
		returnJson.put("isSuccess", isSuccess);
     
		return returnJson;
	}
	
	/**
	 * 凭安失信被执行人查询
	 * @param uid
	 * @param orgName
	 * @param userId
	 * @param uuid
	 * @return
	 * @throws Exception
	 */
	public JSONObject paShixin(String name, String idCard, String orgName, String userId, String uuid) throws Exception {
		
		String result = Constants.BLANK;
		
		String ptime = String.valueOf(System.currentTimeMillis());
		
		String vKey = StringUtil.string2MD5(pn_pkey + "_" + ptime + "_" + pn_pkey);
		
		String uid = StringUtil.string2MD5(name + idCard);
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		StringBuilder sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/b/shixin?");
		sb.append("pname=" + pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&uid=" + uid);
		if (!StringUtil.isNull(orgName)) {
			sb.append("&orgName=" + orgName);
		}
		
		WebResource resource = client.resource(sb.toString());
		
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		JSONObject resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        int api_result = resultObject.getInteger("result");
        
        // 查询成功标识
        boolean isSuccess = true;
        
        // 判断result
        if (0 == api_result || 2 == api_result) {
        	isSuccess = true;
        	// 插入数据库
        	TblPaShixin shixin = new TblPaShixin();
        	shixin.setName(name);
        	shixin.setIdCard(idCard);
        	shixin.setApiQueryDate(format.format(new Date()));
        	if (0 == api_result) {
        		shixin.setReturnValue(resultObject.getString("data"));
        	} else {
        		shixin.setReturnValue(resultObject.getString("message"));
        	}
        	tblPaShixinDao.addPaShixin(shixin);
        } else {
        	isSuccess = false;
        }
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userId);
		apiLog.setLocalApiID(Constants.API_ID_PA_SHIXIN);
		// 参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("name", name);
		param.put("idCard", idCard);
		if (!StringUtil.isNull(orgName)) {
			param.put("orgName", orgName);
		}
		apiLog.setParams(JSON.toJSONString(param));
		apiLog.setDataFrom(Constants.DATA_FROM_PA);
		apiLog.setIsSuccess(String.valueOf(isSuccess));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);

		JSONObject returnJson = new JSONObject();
		returnJson.put("result", result);
		returnJson.put("isSuccess", isSuccess);
     
		return returnJson;
	}
	
	/**
	 * 凭安逾期查询
	 * @param phone
	 * @param uid
	 * @param orgName
	 * @param imsi
	 * @param imei
	 * @param queryDate
	 * @param userId
	 * @param name
	 * @param idCard
	 * @return
	 * @throws Exception
	 */
	public JSONObject paOverdue(String phone, String name, String idCard, String orgName, String imsi, String imei, String queryDate, String userId, String uuid) throws Exception {
		
		String result = Constants.BLANK;
		
		String ptime = String.valueOf(System.currentTimeMillis());
		
		String vKey = StringUtil.string2MD5(pn_pkey + "_" + ptime + "_" + pn_pkey);
		
		String uid = StringUtil.string2MD5(name + idCard);
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		StringBuilder sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/b/overdueClassify?");
		sb.append("pname=" + pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&phone=" + phone);
		sb.append("&uid=" + uid);
		if (!StringUtil.isNull(orgName)) {
			sb.append("&orgName=" + orgName);
		} else {
			orgName = Constants.BLANK;
		}
		if (!StringUtil.isNull(imsi)) {
			sb.append("&imsi=" + imsi);
		} else {
			imsi = Constants.BLANK;
		}
		if (!StringUtil.isNull(imei)) {
			sb.append("&imei=" + imei);
		} else {
			imei = Constants.BLANK;
		}
		if (!StringUtil.isNull(queryDate)) {
			sb.append("&queryDate=" + queryDate);
		} else {
			queryDate = Constants.BLANK;
		}
		
		WebResource resource = client.resource(sb.toString());
		
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		JSONObject resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        int api_result = resultObject.getInteger("result");
        
        // 查询成功标识
        boolean isSuccess = true;
        
        // 判断result
        if (0 == api_result || 2 == api_result) {
        	isSuccess = true;
        	// 插入数据库
        	TblPaOverdue overdue = new TblPaOverdue();
        	overdue.setName(name);
        	overdue.setIdCard(idCard);
        	overdue.setPhone(phone);
        	overdue.setImsi(imsi);
        	overdue.setImei(imei);
        	overdue.setOrgName(orgName);
        	overdue.setQueryDate(queryDate);
        	overdue.setApiQueryDate(format.format(new Date()));
        	if (0 == api_result) {
        		overdue.setReturnValue(resultObject.getString("data"));
        	} else {
        		overdue.setReturnValue(resultObject.getString("message"));
        	}
        	tblPaOverdueDao.addPaOverdue(overdue);
        } else {
        	isSuccess = false;
        }
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userId);
		apiLog.setLocalApiID(Constants.API_ID_PA_OVERDUE);
		// 参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("name", name);
		param.put("idCard", idCard);
		param.put("phone", phone);
		if (!StringUtil.isNull(orgName)) {
			param.put("orgName", orgName);
		}
		if (!StringUtil.isNull(imsi)) {
			param.put("imsi", imsi);
		}
		if (!StringUtil.isNull(imei)) {
			param.put("imei", imei);
		}
		if (!StringUtil.isNull(queryDate)) {
			param.put("queryDate", queryDate);
		}
		apiLog.setParams(JSON.toJSONString(param));
		apiLog.setDataFrom(Constants.DATA_FROM_PA);
		apiLog.setIsSuccess(String.valueOf(isSuccess));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);

		JSONObject returnJson = new JSONObject();
		returnJson.put("result", result);
		returnJson.put("isSuccess", isSuccess);
     
		return returnJson;
	}
	
	/**
	 * 凭安借贷查询
	 * @param phone
	 * @param uid
	 * @param orgName
	 * @param imsi
	 * @param imei
	 * @param queryDate
	 * @param userId
	 * @param name
	 * @param idCard
	 * @return
	 * @throws Exception
	 */
	public JSONObject paLoan(String phone, String name, String idCard, String orgName, String imsi, String imei, String queryDate, String userId, String uuid) throws Exception {
		
		String result = Constants.BLANK;
		
		String ptime = String.valueOf(System.currentTimeMillis());
		
		String vKey = StringUtil.string2MD5(pn_pkey + "_" + ptime + "_" + pn_pkey);
		
		String uid = StringUtil.string2MD5(name + idCard);
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		StringBuilder sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/b/loanClassify?");
		sb.append("pname=" + pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&phone=" + phone);
		sb.append("&uid=" + uid);
		if (!StringUtil.isNull(orgName)) {
			sb.append("&orgName=" + orgName);
		} else {
			orgName = Constants.BLANK;
		}
		if (!StringUtil.isNull(imsi)) {
			sb.append("&imsi=" + imsi);
		} else {
			imsi = Constants.BLANK;
		}
		if (!StringUtil.isNull(imei)) {
			sb.append("&imei=" + imei);
		} else {
			imei = Constants.BLANK;
		}
		if (!StringUtil.isNull(queryDate)) {
			sb.append("&queryDate=" + queryDate);
		} else {
			queryDate = Constants.BLANK;
		}
		
		WebResource resource = client.resource(sb.toString());
		
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		JSONObject resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        int api_result = resultObject.getInteger("result");
        
        // 查询成功标识
        boolean isSuccess = true;
        
        // 判断result
        if (0 == api_result || 2 == api_result) {
        	isSuccess = true;
        	// 插入数据库
        	TblPaLoan loan = new TblPaLoan();
        	loan.setName(name);
        	loan.setIdCard(idCard);
        	loan.setPhone(phone);
        	loan.setImsi(imsi);
        	loan.setImei(imei);
        	loan.setOrgName(orgName);
        	loan.setQueryDate(queryDate);
        	loan.setApiQueryDate(format.format(new Date()));
        	if (0 == api_result) {
        		loan.setReturnValue(resultObject.getString("data"));
        	} else {
        		loan.setReturnValue(resultObject.getString("message"));
        	}
        	tblPaLoanDao.addPaLoan(loan);
        } else {
        	isSuccess = false;
        }
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userId);
		apiLog.setLocalApiID(Constants.API_ID_PA_LOAN);
		// 参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("name", name);
		param.put("idCard", idCard);
		param.put("phone", phone);
		if (!StringUtil.isNull(orgName)) {
			param.put("orgName", orgName);
		}
		if (!StringUtil.isNull(imsi)) {
			param.put("imsi", imsi);
		}
		if (!StringUtil.isNull(imei)) {
			param.put("imei", imei);
		}
		if (!StringUtil.isNull(queryDate)) {
			param.put("queryDate", queryDate);
		}
		apiLog.setParams(JSON.toJSONString(param));
		apiLog.setDataFrom(Constants.DATA_FROM_PA);
		apiLog.setIsSuccess(String.valueOf(isSuccess));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);

		JSONObject returnJson = new JSONObject();
		returnJson.put("result", result);
		returnJson.put("isSuccess", isSuccess);
     
		return returnJson;
	}
	
	/**
	 * 凭安黑名单查询
	 * @param phone
	 * @param uid
	 * @param orgName
	 * @param imsi
	 * @param imei
	 * @param userId
	 * @param name
	 * @param idCard
	 * @return
	 * @throws Exception
	 */
	public JSONObject paBlackList(String phone, String name, String idCard, String orgName, String imsi, String imei, String userId, String uuid) throws Exception {
		
		String result = Constants.BLANK;
		
		String ptime = String.valueOf(System.currentTimeMillis());
		
		String vKey = StringUtil.string2MD5(pn_pkey + "_" + ptime + "_" + pn_pkey);
		
		String uid = StringUtil.string2MD5(name + idCard);
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		StringBuilder sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/b/blacklist?");
		sb.append("pname=" + pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&phone=" + phone);
		sb.append("&uid=" + uid);
		if (!StringUtil.isNull(orgName)) {
			sb.append("&orgName=" + orgName);
		} else {
			orgName = Constants.BLANK;
		}
		if (!StringUtil.isNull(imsi)) {
			sb.append("&imsi=" + imsi);
		} else {
			imsi = Constants.BLANK;
		}
		if (!StringUtil.isNull(imei)) {
			sb.append("&imei=" + imei);
		} else {
			imei = Constants.BLANK;
		}
		
		WebResource resource = client.resource(sb.toString());
		
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		JSONObject resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        int api_result = resultObject.getInteger("result");
        
        // 查询成功标识
        boolean isSuccess = true;
        
        // 判断result
        if (0 == api_result || 2 == api_result) {
        	isSuccess = true;
        	// 插入数据库
        	TblPaBlackList blackList = new TblPaBlackList();
        	blackList.setName(name);
        	blackList.setIdCard(idCard);
        	blackList.setPhone(phone);
        	blackList.setImsi(imsi);
        	blackList.setImei(imei);
        	blackList.setOrgName(orgName);
        	blackList.setApiQueryDate(format.format(new Date()));
        	if (0 == api_result) {
        		blackList.setReturnValue(resultObject.getString("data"));
        	} else {
        		blackList.setReturnValue(resultObject.getString("message"));
        	}
        	tblPaBlackListDao.addPaBlackList(blackList);
        } else {
        	isSuccess = false;
        }
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userId);
		apiLog.setLocalApiID(Constants.API_ID_PA_BLACK_LIST);
		// 参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("name", name);
		param.put("idCard", idCard);
		param.put("phone", phone);
		if (!StringUtil.isNull(orgName)) {
			param.put("orgName", orgName);
		}
		if (!StringUtil.isNull(imsi)) {
			param.put("imsi", imsi);
		}
		if (!StringUtil.isNull(imei)) {
			param.put("imei", imei);
		}
		apiLog.setParams(JSON.toJSONString(param));
		apiLog.setDataFrom(Constants.DATA_FROM_PA);
		apiLog.setIsSuccess(String.valueOf(isSuccess));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);

		JSONObject returnJson = new JSONObject();
		returnJson.put("result", result);
		returnJson.put("isSuccess", isSuccess);
     
		return returnJson;
	}
	
	/**
	 * 凭安申请人属性查询
	 * @param phone
	 * @param uid
	 * @param orgName
	 * @param imsi
	 * @param imei
	 * @param queryDate
	 * @param userId
	 * @param name
	 * @param idCard
	 * @return
	 * @throws Exception
	 */
	public JSONObject paPhkjModelerScore(String phone, String name, String idCard, String orgName, String imsi, String imei, String queryDate, String userId, String uuid) throws Exception {
		
		String result = Constants.BLANK;
		
		String ptime = String.valueOf(System.currentTimeMillis());
		
		String vKey = StringUtil.string2MD5(pn_pkey + "_" + ptime + "_" + pn_pkey);
		
		String uid = StringUtil.string2MD5(name + idCard);
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		StringBuilder sb = new StringBuilder("https://jrapi.pacra.cn");
		sb.append("/b/loanClassify?");
		sb.append("pname=" + pn_pname);
		sb.append("&vkey=" + vKey);
		sb.append("&ptime=" + ptime);
		sb.append("&phone=" + phone);
		sb.append("&uid=" + uid);
		if (!StringUtil.isNull(orgName)) {
			sb.append("&orgName=" + orgName);
		} else {
			orgName = Constants.BLANK;
		}
		if (!StringUtil.isNull(imsi)) {
			sb.append("&imsi=" + imsi);
		} else {
			imsi = Constants.BLANK;
		}
		if (!StringUtil.isNull(imei)) {
			sb.append("&imei=" + imei);
		} else {
			imei = Constants.BLANK;
		}
		if (!StringUtil.isNull(queryDate)) {
			sb.append("&queryDate=" + queryDate);
		} else {
			queryDate = Constants.BLANK;
		}
		
		WebResource resource = client.resource(sb.toString());
		
		result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		JSONObject resultObject = (JSONObject)JSONObject.parse(result);
		
		// get result
        int api_result = resultObject.getInteger("result");
        
        // 查询成功标识
        boolean isSuccess = true;
        
        // 判断result
        if (0 == api_result || 2 == api_result) {
        	isSuccess = true;
        	// 插入数据库
        	TblPaScore score = new TblPaScore();
        	score.setName(name);
        	score.setIdCard(idCard);
        	score.setPhone(phone);
        	score.setImsi(imsi);
        	score.setImei(imei);
        	score.setOrgName(orgName);
        	score.setQueryDate(queryDate);
        	score.setApiQueryDate(format.format(new Date()));
        	if (0 == api_result) {
        		score.setReturnValue(resultObject.getString("data"));
        	} else {
        		score.setReturnValue(resultObject.getString("message"));
        	}
        	tblPaScoreDao.addPaScore(score);
        } else {
        	isSuccess = false;
        }
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userId);
		apiLog.setLocalApiID(Constants.API_ID_PA_PHKJ_MODELER_SCORE);
		// 参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("name", name);
		param.put("idCard", idCard);
		param.put("phone", phone);
		if (!StringUtil.isNull(orgName)) {
			param.put("orgName", orgName);
		}
		if (!StringUtil.isNull(imsi)) {
			param.put("imsi", imsi);
		}
		if (!StringUtil.isNull(imei)) {
			param.put("imei", imei);
		}
		if (!StringUtil.isNull(queryDate)) {
			param.put("queryDate", queryDate);
		}
		apiLog.setParams(JSON.toJSONString(param));
		apiLog.setDataFrom(Constants.DATA_FROM_PA);
		apiLog.setIsSuccess(String.valueOf(isSuccess));
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogDao.addLog(apiLog);

		JSONObject returnJson = new JSONObject();
		returnJson.put("result", result);
		returnJson.put("isSuccess", isSuccess);
     
		return returnJson;
	}
}
