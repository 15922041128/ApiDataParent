package org.pbccrc.api.base.external.vip.grcredit;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestMain {
	
	public static void main11(String [] args) {
		String timestamp = System.currentTimeMillis() + "";
		SortedMap<String, Object> sortedMap = new TreeMap<String, Object>();
		sortedMap.put("innerreqId", Conts.appid + timestamp);
		sortedMap.put("idNo", "410414188407305624");
		sortedMap.put("cardName", "张三");
		sortedMap.put("phone", "13817278031");
		sortedMap.put("cardNo", "622120202020219");
	    
		String aa = JSON.toJSONString(sortedMap);
		
		System.out.println(aa);
		
	        
	}
	
//    public static void main(String [] args) {
//
//        String timestamp = System.currentTimeMillis() + "";
//        SortedMap<String, Object> sortedMap = new TreeMap<String, Object>();
//        sortedMap.put("innerreqId", Conts.appid + timestamp);
//        sortedMap.put("idNo", "410414188407305624");
//        sortedMap.put("cardName", "张三");
//        sortedMap.put("phone", "13817278031");
//        sortedMap.put("cardNo", "622120202020219");
//
//        // 请求体信息
//        byte[] encryptData;
//        String body;
//        try {
//            body = JSON.toJSONString(sortedMap);
//            //RSA 加密
//            encryptData = RSAUtil.encryptRSA(body.getBytes(Conts.coding), false, Conts.coding, Conts.publicServiceKey);
//            //RSA 签名
//            String sign = RSAUtil.generateSign(encryptData, Conts.privateClientKey);
//            System.out.println("TestMain.main:[sign:" + sign + "]");
//
//            // 请求头部
//            HttpPost httpPost = new HttpPost("https://grcredit-webapp.vip.com/webapp/queryBlackList");
//            httpPost.addHeader("apiver", Conts.apiver);
//            httpPost.addHeader("appid", Conts.appid);
//            httpPost.addHeader("timestamp", timestamp);
//            httpPost.addHeader("sign", sign);
//
//            httpPost.addHeader("insCode", Conts.insCode);
//            httpPost.addHeader("serviceCode", Conts.serviceCode);
//
//            httpPost.addHeader("nz", Conts.nz);
//            httpPost.addHeader("crypt", Conts.crypt);
//
//            httpPost.setEntity(new ByteArrayEntity(encryptData));
//            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
//                HttpEntity entity = response.getEntity();
//                byte[] content = EntityUtils.toByteArray(entity);
//
//                int status = response.getStatusLine().getStatusCode();
//                if (HttpStatus.SC_OK == status) { // 请求成功
//                    byte[] desCryptData = RSAUtil.decryptRSA(content, false, Conts.coding, Conts.privateClientKey);
//                    System.out.println(new String(desCryptData, Conts.coding));
//                } else if (HttpStatus.SC_INTERNAL_SERVER_ERROR == status){ // 请求失败
//                    System.out.println(new String(content, Conts.coding));
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
