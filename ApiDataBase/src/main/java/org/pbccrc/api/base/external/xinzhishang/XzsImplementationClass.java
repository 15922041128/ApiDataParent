package org.pbccrc.api.base.external.xinzhishang;

import java.net.URLEncoder;
import java.security.MessageDigest;

import org.pbccrc.api.base.util.StringUtil;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class XzsImplementationClass {
	
	private static String accountID = "xinzhishang";
	
	private static String privateKey = "871b77e2-8072-11e6-8e54-400b752b77d3";
	
	private static String select = "FX006";

	/**
	 * FX006-风险涉诉
	 * @param name
	 * @param cardID
	 * @param sign
	 */
	public static String getFX006(String name, String cardID, String sign) throws Exception {
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		StringBuilder url = new StringBuilder("http://api.shuzunbao.com/openapi/api/searchreport");
		url.append("?accountID=" + accountID);
		url.append("&cardID=" + cardID);
		url.append("&name=" +  URLEncoder.encode(name, "UTF-8"));
		url.append("&select=" + select);
		url.append("&sign=" + sign);
		
		WebResource resource = client.resource(url.toString());
		
		String result = resource.get(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		return result;
	}
	
	/**
	 * 计算sign
	 * @param name
	 * @param cardID
	 * @return
	 */
	public static String getSign(String name, String cardID) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("accountID" + accountID);
		sb.append("cardID" + cardID);
		sb.append("name" + name);
		sb.append("select" + select);
		sb.append(privateKey);
		
		return MD5(sb.toString());
	}
	
	/**
	 * MD5加密算法
	 * @param s
	 * @return
	 */
	private final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       

        try {
            byte[] btInput = s.getBytes("UTF-8");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
