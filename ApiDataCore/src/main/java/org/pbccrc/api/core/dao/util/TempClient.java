package org.pbccrc.api.core.dao.util;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.pbccrc.api.base.util.StringUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class TempClient {
	
	public String remoteAccept(String url) {
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		WebResource resource = client.resource(url);
		
		String result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).header("userID", "9999").get(String.class);
		
		return StringUtil.decodeUnicode(result);
	}
	
	public static void test4 () {
		TempClient test = new TempClient();
		String idCardNo = "120103198603292638";
		String url = "http://localhost:8080/ApiData/r/queryApi/get?service=s-ucquanlian&identityCard=" + idCardNo;
		String result = test.remoteAccept(url);
		System.out.println(result);
	}
	
	public static void test5 () {
		TempClient test = new TempClient();
		String idCardNo = "120103198603292638";
//		String idCardNo = "342401197606205431";
//		String url = "http://localhost:8080/ApiData/r/ldb/query?service=credit&idCardNo=" + idCardNo;
//		String url = "http://localhost:8080/ApiData/r/complex/quota?identifier=" + idCardNo;
		String url = "http://localhost:8080/ApiData/r/queryApi/score?IDcard=510823199912202453&fullName=杜清君";
		String result = test.remoteAccept(url);
		System.out.println(result);
	}
	
	public static void main(String[] args) throws Exception{
//		test1();
//		test2();
//		test3();
//		test4();
//		test5();
//		test6();
//		test7();
//		test8();
		test9();
		
	}
	
	public static void test3() {
		
		String apiKey = "17f11ef84b714581b64d5bd38a56a0e6";
		String userID = "27";

		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);

		StringBuffer url = new StringBuffer();
//		url.append("http://127.0.0.1:8080/ApiData/r/queryApi/get");
//		url.append("http://120.25.230.224:8080/ApiData/r/queryApi/get");
//		url.append("http://192.168.62.47:8080/ApiData/r/queryApi/get");
//		url.append("http://120.25.230.224:8080/ApiData/r/queryApi/get");
//		url.append("http://127.0.0.1:8080/ApiData/r/queryApi/get");	
//		url.append("http://120.25.230.224:8080/ApiData/r/ldb/getSxr");
//		url.append("http://127.0.0.1:8080/ApiData/r/ldb/getSxr");
//		url.append("?service=s-ucquanlian");
//		url.append("&identityCard=120103198603292638");
//		url.append("?service=s-qdxsyss");
//		url.append("&NAME=王梓");
//		url.append("&PHONE=18902069662");
//		url.append("&IDCARD=120103198603292638");
//		url.append("http://192.168.62.47:8080/ApiData/r/queryApi/get");
//		url.append("?service=m-sfzxx");
//		url.append("&name=李晓明");
//		url.append("&idCardNo=132529198103104417");
		url.append("http://192.168.62.47:8080/ApiData/r/queryApi/querySfz");
		url.append("?name=李晓明");
		url.append("&idCardNo=132529198103104417");
//		url.append("http://192.168.62.47:8080/ApiData/r/queryApi/get");
//		url.append("?service=s-pernosql");
//		url.append("&name=林波");
//		url.append("&maskID=330402198208210638");
//		url.append("&name=王梓");
//		url.append("&idCardNo=120103198603292638");
//		url.append("&interCode=10000165-X");
//		url.append("&entname=中国邮电器材集团公司");
//		url.append("&tipcode=UC-PR6055");
//		url.append("http://120.25.230.224:8080/ApiData/r/queryApi/get");
//		url.append("?service=s-ucaccindWDHMD");
//		url.append("&pname=刘善波");
//		url.append("&idcardNo=150430198201132878");
//		url.append("&pageno=2");
//		url.append("&range=10");
//		url.append("&entname=王梓");
//		url.append("&interCode=10000165-X");
//		url.append("&KeyNo=00915755fa78d8e8f554fb37a94dabba");
//		url.append("&fqy=北京首钢建设集团有限公司");
//		url.append("&pname=王梓");
//		url.append("&idcardNo=120103198603292638");
//		url.append("&pname=吕少兵");
//		url.append("&idcardNo=342401197606205431");
//		url.append("&punishContent=绵阳佳成建设有限公司");
//		url.append("&newid=446");
//		url.append("&year=2015");
//		url.append("&companyId=1921");
//		url.append("&companyName=广西桂禹工程咨询有限公司");
//		url.append("&unitName=广西桂禹工程咨询有限公司");
//		url.append("&trademarkid=SQSTUMRULNOR");
//		url.append("&ent_name=中国邮电器材集团公司");
//		url.append("&KeyNo=00915755fa78d8e8f554fb37a94dabba");
//		url.append("&KeyNo=天津金星投资有限公司");
//		url.append("?service=s-qsyss");
//		url.append("&company_name=广西桂禹工程咨询有限公司");
//		url.append("&entname=中国邮电器材集团公司");
//		url.append("&interCode=10000165-X");
//		url.append("&gettime=0");
//		url.append("&keyword=王健林");
//		url.append("&name=刘善波");
//		url.append("&maskID=150430198201132878");
//		url.append("&gettime=0");
//		url.append("&CertType=0");
//		url.append("&maskID=150430198201132878");
//		url.append("?service=m-sfzxx");
//		url.append("&NAME=王梓");
//		url.append("&IDCARDNUM=120103198603292638");
//		url.append("&ACCOUNTNO=6214830223501445");
//		url.append("&BANKPREMOBILE=15922041128");
//		url.append("&name=王梓");
//		url.append("&idCardNo=120103198603292638");
//		url.append("&identityCard=120103198603292638");
//		url.append("&idCardNo=120107198909304510");
		
		System.out.println(url.toString());
		
		WebResource resource = client.resource(url.toString());
		
		String result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).header("apiKey", apiKey).header("userID", userID).get(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		System.out.println(result);
	}
	
	public static void test2() {
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);

//		StringBuffer url = new StringBuffer();
//		url.append("http://127.0.0.1:8080/ApiData/r/queryApi/get");
//		url.append("?service=s-qlexecute");
//		url.append("&exeName=王健林");
		
		StringBuffer url = new StringBuffer();
		
		url.append("http://222.128.127.219:8989/ApiData/r/complex/getPdfItem");
//		url.append("http://127.0.0.1:8080/ApiData/r/complex/getPdfItem");
		url.append("?service=zh-creditcard");
		url.append("&identifier=340104197210133559&name=庄传礼");
		
//		url.append("http://127.0.0.1:8080/ApiData/r/complex/getPdfItem");
//		url.append("?service=zh-person");
//		url.append("&identifier=342401197606205431");
		
//		url.append("&personName=王梓");
//		url.append("&idCardNo=120103198603292638");
//		url.append("&sortName=总监理工程师");
//		url.append("&accountNo=6214830223501445");
//		url.append("&bankPreMobile=15922041128");
		
		
//		url.append("http://127.0.0.1:8080/ApiData/r/queryApi/get");
//		url.append("?service=m-sfzxx");
//		url.append("&name=王梓");
//		url.append("&idCardNo=120103198603292638");
		WebResource resource = client.resource(url.toString());
		
//		WebResource resource = client.resource("http://192.168.62.47:8080/ApiData/r/user/login?userName=wangzi&password=111111");
		
		String result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).header("apiKey", "08611099f84345e6be2b096ae94b11c1").header("userID", "37").get(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		System.out.println(result);
	}
	
	public static void test1() {
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		StringBuffer url = new StringBuffer();
		url.append("http://127.0.0.1:8080/ApiData/r/queryApi/get");
//		url.append("http://www.qilingyz.cn:8080/ApiData/r/queryApi/get");
		url.append("?service=s-queryScore");
//		String name = "王梓";
		String idCardNo = "120103198603292638";
		
//		url.append("&fullName=" + name);
		url.append("&identityCard=" + idCardNo);
		
		String str = url.toString();
		System.out.println(str);
		
		WebResource resource = client.resource(str);
		String result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).header("apiKey", "X37EF162524F265").header("userID", "1").get(String.class);
		System.out.println(result);
	}
	
	public static void test6() {
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		StringBuffer url = new StringBuffer();
		url.append("http://127.0.0.1:8080/ApiData/bankCardAuth/auth");
//		url.append("http://222.128.127.219:8989/ApiData/bankCardAuth/auth");
//		url.append("http://192.168.62.102:8080/ApiData/bankCardAuth/auth");
		url.append("?acc_no=6214830223501445");
		url.append("&id_card=120103198603292638");
		url.append("&id_holder=王梓");
		url.append("&mobile=15922041128");
		url.append("&verify_element=1234");
		
		String str = url.toString();
		System.out.println(str);
		
		WebResource resource = client.resource(str);
		String result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).header("apiKey", "4055fc8ce1724519afc1631438aa6fa4").header("userID", "27").get(String.class);
		System.out.println(result);
	}
	
	public static void test7() {
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		StringBuffer url = new StringBuffer();
		url.append("http://127.0.0.1:8080/ApiData/creditModel/get");
		url.append("?accountNo=6214830223501445");
		url.append("&idCardNo=120103198603292638");
		url.append("&name=王梓");
		url.append("&mobile=15922041128");
		
		String str = url.toString();
		System.out.println(str);
		
		WebResource resource = client.resource(str);
		String result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).header("apiKey", "17f11ef84b714581b64d5bd38a56a0e5").header("userID", "27").get(String.class);
		System.out.println(result);
	}
	
	public static void test8() {
		String apiKey = "17f11ef84b714581b64d5bd38a56a0e6";
		String userID = "27";

		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		StringBuffer url = new StringBuffer();
		url.append("http://192.168.62.47:8080/ApiData/r/queryApi/querySfz");
		url.append("?name=李晓明");
		url.append("&idCardNo=132529198103104417");
		
		System.out.println(url.toString());
		
		WebResource resource = client.resource(url.toString());
		
		String result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).header("apiKey", apiKey).header("userID", userID).get(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		System.out.println(result);
	}
	
	public static void test9() throws Exception {
		
		String apiKey = "3fdfcd7fa7bc4948820e6f5822f64e98";
//		String apiKey = "3fdfcd7fa7bc4948820e6f5822f64e97";
		String userID = "27";
		
		JSONObject json = new JSONObject();
		json.put("province", "天津");
		json.put("operator", "cm");
		json.put("productType", "xjd");
		json.put("age_max", "38");
		json.put("content", "京东iphone8,优惠享不停！ http://dwz.cn/6HIBuI 退订回T【云信留客】");
		json.put("sendNum", "2");
		json.put("smsTunnel", "yunxin");
		
		
		json.put("loanInfos", getC2());
		
		String str = json.toJSONString();
//		str = new BASE64Encoder().encode(json.toJSONString().getBytes("UTF-8"));
		str = new String(new Base64().encode(json.toJSONString().getBytes("UTF-8")));
		str = URLEncoder.encode(str);
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		StringBuffer url = new StringBuffer();
//		url.append("http://www.qilingyz.com:8989/ApiData/creditModel/getResultParam");
//		url.append("http://localhost:8080/ApiData/marketing/getMarketeeCount");
		url.append("http://localhost:8080/ApiData/marketing/sendMesg");
		url.append("?requestStr=" + str );
		
		URL u = new URL(url.toString());
		URI uri = new URI(u.getProtocol(), u.getHost() + ":" + u.getPort(), u.getPath(), u.getQuery(), null);
		
		WebResource resource = client.resource(uri);
		
		String result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).header(
				"apiKey", apiKey).header("userID", userID).post(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		System.out.println(result);
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
	
	
	private static JSONArray getB() {
		
		JSONArray array = new JSONArray();
		
		JSONObject obj1 = new JSONObject();
		obj1.put("borrowType", "1");
		obj1.put("borrowState", "2");
		obj1.put("borrowAmount", 5);
		obj1.put("contractDate", "1425168000000");
		obj1.put("loanPeriod", "12");
		obj1.put("repayState", "9");
		obj1.put("arrearsAmount", "0");
		obj1.put("companyCode", "3");
		
		JSONObject obj2 = new JSONObject();
		obj2.put("borrowType", "1");
		obj2.put("borrowState", "2");
		obj2.put("borrowAmount", 3);
		obj2.put("contractDate", "1430438400000");
		obj2.put("loanPeriod", "12");
		obj2.put("repayState", "0");
		obj2.put("arrearsAmount", "0");
		obj2.put("companyCode", "5");
		
		JSONObject obj3 = new JSONObject();
		obj3.put("borrowType", "1");
		obj3.put("borrowState", "1");
		obj3.put("borrowAmount", 0);
		obj3.put("contractDate", "1427846400000");
		obj3.put("loanPeriod", "0");
		obj3.put("repayState", "0");
		obj3.put("arrearsAmount", "0");
		obj3.put("companyCode", "6");
		
		JSONObject obj4 = new JSONObject();
		obj4.put("borrowType", "1");
		obj4.put("borrowState", "1");
		obj4.put("borrowAmount", 0);
		obj4.put("contractDate", "1489449600000");
		obj4.put("loanPeriod", "0");
		obj4.put("repayState", "0");
		obj4.put("arrearsAmount", "0");
		obj4.put("companyCode", "371");
		
//		array.add(obj1);
		array.add(obj2);
		array.add(obj3);
		array.add(obj4);
		
		return array;
	}
	
	private static JSONArray getC() {
		
		JSONArray array = new JSONArray();
		
		JSONObject obj1 = new JSONObject();
		obj1.put("borrowType", "1");
		obj1.put("borrowState", "1");
		obj1.put("borrowAmount", 0);
		obj1.put("contractDate", "1427846400000");
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
		
		JSONObject obj4 = new JSONObject();
		obj4.put("borrowType", "1");
		obj4.put("borrowState", "1");
		obj4.put("borrowAmount", 0);
		obj4.put("contractDate", "1470268800000");
		obj4.put("loanPeriod", "0");
		obj4.put("repayState", "0");
		obj4.put("arrearsAmount", "0");
		obj4.put("companyCode", "26");
		
		JSONObject obj5 = new JSONObject();
		obj5.put("borrowType", "1");
		obj5.put("borrowState", "2");
		obj5.put("borrowAmount", -5);
		obj5.put("contractDate", "1472601600000");
		obj5.put("loanPeriod", "6");
		obj5.put("repayState", "2");
		obj5.put("arrearsAmount", "56885000");
		obj5.put("companyCode", "371");
		
		JSONObject obj6 = new JSONObject();
		obj6.put("borrowType", "1");
		obj6.put("borrowState", "1");
		obj6.put("borrowAmount", 0);
		obj6.put("contractDate", "1470009600000");
		obj6.put("loanPeriod", "0");
		obj6.put("repayState", "0");
		obj6.put("arrearsAmount", "0");
		obj6.put("companyCode", "9105");
		
		JSONObject obj7 = new JSONObject();
		obj7.put("borrowType", "1");
		obj7.put("borrowState", "1");
		obj7.put("borrowAmount", 0);
		obj7.put("contractDate", "1472688000000");
		obj7.put("loanPeriod", "0");
		obj7.put("repayState", "0");
		obj7.put("arrearsAmount", "0");
		obj7.put("companyCode", "9105");
		
		JSONObject obj8 = new JSONObject();
		obj8.put("borrowType", "1");
		obj8.put("borrowState", "1");
		obj8.put("borrowAmount", 0);
		obj8.put("contractDate", "1480550400000");
		obj8.put("loanPeriod", "0");
		obj8.put("repayState", "0");
		obj8.put("arrearsAmount", "0");
		obj8.put("companyCode", "9105");
		
		JSONObject obj9 = new JSONObject();
		obj9.put("borrowType", "1");
		obj9.put("borrowState", "1");
		obj9.put("borrowAmount", 0);
		obj9.put("contractDate", "1485907200000");
		obj9.put("loanPeriod", "0");
		obj9.put("repayState", "0");
		obj9.put("arrearsAmount", "0");
		obj9.put("companyCode", "9105");
		
		array.add(obj1);
		array.add(obj2);
		array.add(obj3);
		array.add(obj4);
		array.add(obj5);
		array.add(obj6);
		array.add(obj7);
		array.add(obj8);
		array.add(obj9);
		
		return array;
	}
}
