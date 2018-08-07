package org.pbccrc.api.base.external.huludata;

import javax.ws.rs.core.MediaType;

import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class HuluImplementation {
	
	// 【运营商数据源采集接口】和 【主动获取原始数据和报告接口】
	private static String companyAccount = "xlk_CRAWLER";
	
	// 【主动获取原始数据和报告接口】
	private static String signature = "8dea16f0a7bf4e92b9ef4438f9a8e273";
	
	public static void main(String[] args) throws Exception {
		
		String name = "张三";
		String identity_card_number = "120103198603292638";
		String cell_phone_number = "15922041128";
		
		/** 【运营商数据源采集接口】部分 */
		
		// 【运营商数据源采集接口】2.1获取token
//		String collectToken = getCollectToken(name, identity_card_number, cell_phone_number);
//		System.out.println(collectToken);
		
	    // 【运营商数据源采集接口】3.1申请重置密码 ,返回验证码
//	    resetPassword(collectToken, null, null);
		
		// 【运营商数据源采集接口】3.2重置密码
//		String token = "a9333c662a3b4fbbb9e1777abd2890b0";
//		resetPassword(collectToken, "jay790118", "809267");
		
		// 【运营商数据源采集接口】2.2提交登陆
		// 536369为15922041128服务密码
//		collect(collectToken, "536369", null, null);
		
		// 【运营商数据源采集接口】2.3 提交短信验证码
//		collect(collectToken, null, "695171", null);
		
		/** 【主动获取原始数据和报告接口】部分 */
		// 【主动获取原始数据和报告接口】2.1获取数据令牌access_token接口
//		String accessToken = getAccessToken();
		
		// 【主动获取原始数据和报告接口】3.1获取原始数据
//		String collectToken = "03d6e1b7089d42f0bd81796fc049b5bd";
//		String result = getRawdatas(accessToken, collectToken);
		
		String result = service_getRawdata(name, identity_card_number, cell_phone_number, "536369");
		System.out.println(result);
	}
	
	/** service-获取动态码 */
	public static String service_getDynamicCode(String name, String idCard, String phone) {
		
		/** 【运营商数据源采集接口】部分 */
		// 【运营商数据源采集接口】2.1获取token
		String collectToken = getCollectToken(name, idCard, phone);
		
		 // 【运营商数据源采集接口】3.1申请重置密码 ,返回验证码
		resetPassword(collectToken, null, null);
		
		return collectToken;
	}
	
	/**
	 * service-重置密码
	 * @param token      service-获取动态码返回的token
	 * @param password   要修改的密码
	 * @param captcha    手机动态验证码
	 * @return
	 */
	public static String service_resetPassword(String token, String password, String captcha) {
		return resetPassword(token, password, captcha);
	}
	
	/** service-获取原始数据 */
	public static String service_getRawdata(String name, String idCard, String phone, String phonePassword) throws Exception {
		
		/** 【运营商数据源采集接口】部分 */
		// 【运营商数据源采集接口】2.1获取token
		String collectToken = getCollectToken(name, idCard, phone);
		// 判断 collectToken是否成功获取
		if (StringUtil.isNull(collectToken)) {
			return Constants.BLANK;
		}
		
		// 【运营商数据源采集接口】2.2提交登陆
		collect(collectToken, phonePassword, null, null);
		
		/** 【主动获取原始数据和报告接口】部分 */
		// 【主动获取原始数据和报告接口】2.1获取数据令牌access_token接口
		String accessToken = getAccessToken();
		
		// 【主动获取原始数据和报告接口】3.1获取原始数据
		return getRawdatas(accessToken, collectToken);
	}
	

	/** 【运营商数据源采集接口】 */
	
	// 【运营商数据源采集接口】
	// 2.1获取collectToken
	private static String getCollectToken(String name, String identity_card_number, String cell_phone_number) {
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		String url = "https://collect.hulushuju.com/api/applications/mobile";
		
        JSONObject params = new JSONObject();
        params.put("company_account", companyAccount);
        params.put("name", name);
        params.put("identity_card_number", identity_card_number);
        params.put("cell_phone_number", cell_phone_number);
        
		WebResource resource = client.resource(url);
		
		String result = resource.entity(params.toJSONString(), MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).post(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		System.out.println(result);
		
		JSONObject object = JSON.parseObject(result);
		
		JSONObject data = object.getJSONObject("data");
		
		if (null == data) {
			return Constants.BLANK;
		}
		
		String collectToken = data.getString("token");
		
		return collectToken;
	}
	
	// 【运营商数据源采集接口】
	// 2.2提交登陆
	// 2.3提交短信验证码
	// 2.4提交查询密码
	// 当password不为空时为2.2,当captcha不为空时为2.3,当queryPassword不为空时为2.4
	private static void collect(String collectToken, String password, String captcha, String queryPassword) {
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		String url = "https://collect.hulushuju.com/api/authorize/mobile/collect";
		
        JSONObject params = new JSONObject();
        params.put("token", collectToken);
        if (null != password) {
        	params.put("password", password);
        }
        if (null != captcha) {
        	params.put("captcha", captcha);
        }
        if (null != queryPassword) {
        	params.put("query_password", queryPassword);
        }
        
		WebResource resource = client.resource(url);
		
		String result = resource.entity(params.toJSONString(), MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).post(String.class);
		
		result = StringUtil.decodeUnicode(result);
	}
	
	// 【运营商数据源采集接口】
	// 3.1 申请重置密码
	// 3.2 重置密码申请
	// 3.3 重置密码
	// 当password和captcha均为空时为3.1
	// 当password和captcha均不为空时为3.2
	// 当password为空,captcha不为空时为3.3
	private static String resetPassword(String collectToken, String password, String captcha) {
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		String url = "https://collect.hulushuju.com/api/authorize/password/reset";
		
		JSONObject params = new JSONObject();
        params.put("token", collectToken);
        
        // 判断密码和动态码是否为空
        if (null != password && null != captcha) {
        	// 3.2
        	params.put("password", password);
            params.put("captcha", captcha);
        } else if (null == password && null!= captcha) {
       	 	// 3.3
       	 	params.put("captcha", captcha);
        }
        
		WebResource resource = client.resource(url);
		
		String result = resource.entity(params.toJSONString(), MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).post(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		return result;
	}
	
	/** 【主动获取原始数据和报告接口】  */
	
	// 【主动获取原始数据和报告接口】2.1获取数据令牌access_token接口
	private static String getAccessToken() {
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		String url = "https://data.hulushuju.com/api/companies/" + companyAccount + "/access_token?signature=" + signature;
		
		WebResource resource = client.resource(url);
		
		String result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		JSONObject object = JSON.parseObject(result);
		
		JSONObject data = object.getJSONObject("data");
		
		String accessToken = data.getString("access_token");
		
		return accessToken;
	}
	
	// 【主动获取原始数据和报告接口】3.1获取原始数据
	private static String getRawdatas(String accessToken, String collectToken) {
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		String url = "https://data.hulushuju.com/api/data/rawdatas/old/" + collectToken + "?companyAccount=" + companyAccount + "&accessToken=" + accessToken;
		
		WebResource resource = client.resource(url);
		
		String result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).get(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		return result;
	}
	
	/** service-登陆 获取token,并提交登陆*/
	public static String service_getLoadCollectToken(String name, String idCard, String phone,String password) {
		
		/** 【运营商数据源采集接口】部分 */
		// 【运营商数据源采集接口】2.1获取token
		String collectToken = getCollectToken(name, idCard, phone);	
		//数据验证码，发送短信
		collect(collectToken, password, null, null);
		return collectToken;
	}
	
	/** service-第一次登陆，需要提交短信验证码授权*/
	public static String service_getLoadResult(String collectToken, String captcha) {
		
		// 【运营商数据源采集接口】2.3 提交短信验证码
		collect(collectToken, null, captcha, null);
		/** 【主动获取原始数据和报告接口】部分 */
		// 【主动获取原始数据和报告接口】2.1获取数据令牌access_token接口
		String accessToken = getAccessToken();
		
		// 【主动获取原始数据和报告接口】3.1获取原始数据
		return getRawdatas(accessToken, collectToken);
	}
	
}
