package org.pbccrc.api.core.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.pbccrc.api.base.service.SendMessageCoreService;
import org.springframework.stereotype.Service;

@Service
public class SendMessageYunxinServiceImpl implements SendMessageCoreService{

	@Override
	public Map<String, Object> sendMessage(String telNos, String msgContent) throws Exception{
		
		String url = "http://112.124.24.5/api/MsgSend.asmx/SendMsg";

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("userCode", "JQXLYX"));
		nvps.add(new BasicNameValuePair("userPass", "JQXLyx123"));
		nvps.add(new BasicNameValuePair("DesNo", telNos));
		nvps.add(new BasicNameValuePair("Msg", msgContent + "【云信留客】"));
		nvps.add(new BasicNameValuePair("Channel", "86"));
		String post = httpPost(url, nvps);

		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		Document document = DocumentHelper.parseText(post);
		Element root = document.getRootElement();
		List strings = root.elements("string");
		if (null != strings && strings.size() > 0) {
			returnMap.put("isSuccess", true);
		} else {
			returnMap.put("isSuccess", false);
		}
		
		return returnMap;
	}

	public static String httpPost(String url, List<NameValuePair> params) {
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instreams = entity.getContent();
				result = convertStreamToString(instreams);
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
