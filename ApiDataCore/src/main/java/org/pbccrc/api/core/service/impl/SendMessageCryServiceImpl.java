package org.pbccrc.api.core.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.pbccrc.api.base.service.SendMessageCoreService;

/** 短信服务接口(创瑞云实现) */
public class SendMessageCryServiceImpl implements SendMessageCoreService {
	
		@Override
		public Map<String, Object> sendMessage(String phoneNos, String content, String sign) throws Exception {
			
			HttpClient httpClient = new HttpClient();
	        PostMethod postMethod = new PostMethod("http://api.1cloudsp.com/api/v2/send");
	        postMethod.getParams().setContentCharset("UTF-8");
	        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());

	        String accesskey = "1ORTEiO1JFw2obGz"; //用户开发key
	        String accessSecret = "Dk5vlIjJnDLoUIoWfuzvGdgXsxUdOPdF"; //用户开发秘钥

	        NameValuePair[] data = {
	                new NameValuePair("accesskey", accesskey),
	                new NameValuePair("secret", accessSecret),
//	                new NameValuePair("sign", "1665"),
	                new NameValuePair("sign", sign),
//	                new NameValuePair("templateId", "1818"),
	                new NameValuePair("mobile", phoneNos),
	                new NameValuePair("content", URLEncoder.encode(content, "utf-8"))
//	                new NameValuePair("content", URLEncoder.encode("先生##9:40##快递公司##1234567", "utf-8"))//（示例模板：{1}您好，您的订单于{2}已通过{3}发货，运单号{4}）
	        };
	        postMethod.setRequestBody(data);

	        int statusCode = httpClient.executeMethod(postMethod);
	        System.out.println("statusCode: " + statusCode + ", body: "
	                    + postMethod.getResponseBodyAsString());
	        
	        Map<String, Object> returnMap = new HashMap<String, Object>();
	        returnMap.put("isSuccess", 200 == statusCode);
	        return returnMap;
		}
		
		public static void main(String[] args) throws Exception {
			String content = "http://www.qilingyz.cn";
			content = "汇逛街邀您一起来逛街，搜罗全网优惠好物，购物享优惠返利，更有积分好礼等你来。http://www.qilingyz.cn";
			new SendMessageCryServiceImpl().sendMessage("15922041128,13821699236", content, "【955钱包】");
		}

}
