package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.external.vip.grcredit.Conts;
import org.pbccrc.api.base.external.vip.grcredit.RSAUtil;
import org.pbccrc.api.base.service.ExternalService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.core.dao.ApiLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service
public class ExternalServiceImpl implements ExternalService{
	
	@Autowired
	private ApiLogDao apiLogDao;

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

}
