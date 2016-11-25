package org.pbccrc.api.core.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.pbccrc.api.base.service.SMSService;
import org.springframework.stereotype.Service;

/** 短信服务接口(短信王实现) */
@Service("dxw_smsServiceImpl")
public class DXW_SMSServiceImpl implements SMSService{

		//账号
		static String name = "yingze";
		//密码
		static String password = "1216B8CF79FB34846C458E062840";
		// 统一编码
		static final String CHARSET_UTF8 = "UTF-8";

		@Override
		public void query(String phoneNo, String vCode) throws Exception {
			
			/*
			// 模板内容
			String content = "您好，您的手机动态验证码为：" + vCode + "。该码10分钟内有效且只能输入1次，若10分钟内未输入，需重新获取。";
			// 模板签名
			String sign="鹰泽评分";
			
			// 创建StringBuffer对象用来操作字符串
			StringBuffer sb = new StringBuffer("http://web.duanxinwang.cc/asmx/smsservice.aspx?");
			// 向StringBuffer追加用户名
			sb.append("name=" + name);
			// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
			sb.append("&pwd=" + password);
			// 向StringBuffer追加手机号码
			sb.append("&mobile=" + phoneNo);
			// 向StringBuffer追加消息内容转URL标准码
//			sb.append("&content=" + URLEncoder.encode(content,"UTF-8"));
			sb.append("&content=" + content);
			//追加发送时间，可为空，为空为及时发送
			sb.append("&stime=");
			//加签名
//			sb.append("&sign=" + URLEncoder.encode(sign,"UTF-8"));
			sb.append("&sign=" + sign);
			//type为固定值pt  extno为扩展码，必须为数字 可为空
			sb.append("&type=pt&extno=");
			URL url = new URL(sb.toString());
			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			// 发送
			url.openStream();
			*/
			
			// 模板内容
			String content = "您好，您的手机动态验证码为：" + vCode + "。该码10分钟内有效且只能输入1次，若10分钟内未输入，需重新获取。";
			// 模板签名
			String sign="鹰泽评分";
			
			String url = "http://web.duanxinwang.cc/asmx/smsservice.aspx?";
			
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("name", name);
			paramsMap.put("pwd", password);
			paramsMap.put("mobile", phoneNo);
			paramsMap.put("content", content);
			paramsMap.put("sign", sign);
			paramsMap.put("type", "pt");
			
			
			sendHttpPost(url, paramsMap);
		}
		
		private static String sendHttpPost(String apiUrl, Map<String, String> paramsMap) {
			String responseText = "";
			StringBuilder params = new StringBuilder();
			Iterator<Map.Entry<String, String>> iterator = paramsMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				params.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			
			try {
				URL url = new URL(apiUrl);
				URLConnection connection = url.openConnection();
				connection.setDoOutput(true);
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), CHARSET_UTF8);
				out.write(params.toString()); //post的关键所在！
				out.flush();
				out.close();
				//读取响应返回值
				InputStream is = connection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, CHARSET_UTF8));
				String temp = "";
				while (( temp = br.readLine()) != null) {
					responseText += temp;
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return responseText;
		}
		
		public static void main(String[] args) throws Exception {
			new DXW_SMSServiceImpl().query("15922041128", "8888");
		}

}
