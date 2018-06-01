

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.pbccrc.api.base.util.StringUtil;

import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import sun.misc.BASE64Encoder;
/**
 * 凭安
 * @author charles
 *
 */
public class Test3 {
	
	public static void main2(String[] args) throws Exception {
		System.out.println("王梓1111111112222");
	}
	
	
	public static void main(String[] args) throws Exception {
		
		// blacklist
		String userID = "27";
		String apiKey = "1d55fc8ce1724519aff1611438aa6ff8";
		String url = "http://www.qilingyz.com:8989/ApiData/external/pa/score";
//		String url = "http://www.qilingyz.com:8989/ApiData/external/pa/shixin";
		
		List<String> list = readFile("d://17.csv");
		
//		String[] line = list.get(0).split(",");
//		String idCard = line[0];
//		String name = line[1];
//		String phone = line[2];
//		String result = "result";
//		
//		StringBuilder sb = new StringBuilder();
//		sb.append(idCard + ",");
//		sb.append(name + ",");
//		sb.append(phone + ",");
//		sb.append(result);
//		sb.append("\r\n");
//		sb.append(idCard + ",");
//		sb.append(name + ",");
//		sb.append(phone + ",");
//		sb.append(result);
//		writeFile(sb.toString(), "33");
		
		StringBuilder sb = new StringBuilder();
		String[] line = null;
		String idCard = null;
		String name = null;
		String phone = null;
		String result = null;
		JSONObject object = null;
		
		long begin = System.currentTimeMillis();
		
		for (int i = 0; i < list.size(); i++) {
//			line = list.get(i).split(",");
//			idCard = line[0];
//			name = line[1];
//			phone = line[2];
			idCard = "120103198603292638";
			name = "王梓";
			phone = list.get(i);
			sb.append(idCard + ",");
			sb.append(name + ",");
			sb.append(phone + ",");
			
			object = new JSONObject();
			object.put("name", name);
			object.put("idCard", idCard);
			object.put("phone", phone);
			
			result = invoke(url, userID, apiKey, object);
			
			System.out.println((i + 1) + result);
			
			sb.append(result);
			sb.append("\r\n");
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("耗时:" + (end - begin));
		
		writeFile(sb.toString(), "申请人属性");
	}
	
	public static String invoke(String urlStr, String userID, String apiKey, JSONObject param) throws Exception {
		
		String str = new BASE64Encoder().encode(param.toJSONString().getBytes("UTF-8"));
		str = URLEncoder.encode(str);
		
		
		ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 10 * 1000);
		Client client = Client.create(config);
		
		StringBuffer url = new StringBuffer();
		url.append(urlStr);
		url.append("?requestStr=" + str);
		
//		System.out.println(str);
		
		URL u = new URL(url.toString());
		URI uri = new URI(u.getProtocol(), u.getHost() + ":" + u.getPort(), u.getPath(), u.getQuery(), null);
		
		WebResource resource = client.resource(uri);
		
		String result = resource.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).header(
				"apiKey", apiKey).header("userID", userID).post(String.class);
		
		result = StringUtil.decodeUnicode(result);
		
		return result;
	}
	
	public static void writeFile(String line, String fileName) throws Exception {
		FileOutputStream outSTr = null;
		BufferedOutputStream Buff = null;
		
		 outSTr = new FileOutputStream(new File("d://" + fileName + ".txt"));
         Buff = new BufferedOutputStream(outSTr);
         long begin0 = System.currentTimeMillis();
         Buff.write(line.getBytes());
         Buff.flush();
         Buff.close();
         long end0 = System.currentTimeMillis();
//         System.out.println("耗时:" + (end0 - begin0));
	}
	
	// 读取文件
	public static List<String> readFile(String filePath) throws IOException{
		List<String> list = new ArrayList<String>();
    
		LineIterator iterator = FileUtils.lineIterator(new File(filePath), "UTF-8");
		 try {
		   while (iterator.hasNext()) {
		     String line = iterator.nextLine();
		     list.add(line);
		   }
		 } finally {
		   LineIterator.closeQuietly(iterator);
		 }
		
        return list;
    }
}
