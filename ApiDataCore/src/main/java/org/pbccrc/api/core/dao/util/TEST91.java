package org.pbccrc.api.core.dao.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TEST91 {

    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String GET_URL = "http://222.128.127.219:8989/ApiData/creditModel/getResult";

    private static final String POST_URL = "http://222.128.127.219:8989/ApiData/creditModel/getResult";

    public static void main(String[] args) throws IOException {
//        sendGET();
//        System.out.println("GET DONE");
        sendPOST();
        System.out.println("POST DONE");
    }

    private static void sendGET() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(GET_URL);
        httpGet.addHeader("User-Agent", USER_AGENT);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        System.out.println("GET Response Status:: "
                + httpResponse.getStatusLine().getStatusCode());

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpResponse.getEntity().getContent()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();

        // print result
        System.out.println(response.toString());
        httpClient.close();
    }

    private static void sendPOST() throws IOException {
    	//准备数据
    	JSONObject json = new JSONObject();
		json.put("realName", "陈成明");
		json.put("idCard", "452524196905140616");
		json.put("trxNo", "1234567890");
		json.put("loanInfos", getC2());
		
		String str = json.toJSONString();
		String retMsg = "";
		   byte[] encodeBase64 = Base64.encodeBase64(str.getBytes("UTF-8"));
		   retMsg = new String(encodeBase64);
		   retMsg = java.net.URLDecoder.decode(retMsg, "UTF-8");
	

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(POST_URL);
        httpPost.addHeader("User-Agent", USER_AGENT);
//        httpPost.setHeader("userID","50");
//        httpPost.setHeader("apiKey","7e163bbcc5a244b58d8380f88d4a30d1");
        httpPost.setHeader("userID","27");
        httpPost.setHeader("apiKey","17f11ef84b714581b64d5bd38a56a0e4");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("requestStr", retMsg));

        HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
        httpPost.setEntity(postParams);

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

        System.out.println("POST Response Status:: "
                + httpResponse.getStatusLine().getStatusCode());

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpResponse.getEntity().getContent()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();

        // print result
        System.out.println(response.toString());
        httpClient.close();

    }
    
	private static JSONArray getC2() {
		
		JSONArray array = new JSONArray();
		
		JSONObject obj1 = new JSONObject();
		obj1.put("borrowType", "1");
		obj1.put("borrowState", "1");
		obj1.put("borrowAmount", 0);
		obj1.put("contractDate", "1433116800000");
		obj1.put("loanPeriod", "0");
		obj1.put("repayState", "0");
		obj1.put("arrearsAmount", "0");
		obj1.put("companyCode", "3");
		
		JSONObject obj2 = new JSONObject();
		obj2.put("borrowType", "1");
		obj2.put("borrowState", "1");
		obj2.put("borrowAmount", 0);
		obj2.put("contractDate", "1430438400000");
		obj2.put("loanPeriod", "0");
		obj2.put("repayState", "0");
		obj2.put("arrearsAmount", "0");
		obj2.put("companyCode", "8");
		
		JSONObject obj3 = new JSONObject();
		obj3.put("borrowType", "1");
		obj3.put("borrowState", "1");
		obj3.put("borrowAmount", 0);
		obj3.put("contractDate", "1433116800000");
		obj3.put("loanPeriod", "0");
		obj3.put("repayState", "0");
		obj3.put("arrearsAmount", "0");
		obj3.put("companyCode", "8");
		
		
		array.add(obj1);
		array.add(obj2);
		array.add(obj3);
		
		return array;
	}

}