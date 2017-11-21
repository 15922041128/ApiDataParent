package org.pbccrc.api.core.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.pbccrc.api.base.service.SendMessageCoreService;
import org.pbccrc.api.base.util.Constants;
import org.springframework.stereotype.Service;

/**
 * 鼎汉
 * @author Administrator
 *
 */
@Service
public class SendMessageDinghanServiceImpl implements SendMessageCoreService{

	@Override
	public Map<String, Object> sendMessage(String telNos, String msgContent) throws Exception {
		
		String host = "112.74.179.106:8080";
        String userCode = "xzs001";
        String userPwd = "xzs001cxx";
        String numbers = telNos;
        msgContent = msgContent + "【鼎汉】";
        String charset = "GBK";

        StringBuffer urlSb = new StringBuffer();
        urlSb.append("http://").append(host).append("/Message.sv?method=sendMsg");
        urlSb.append("&userCode=").append(userCode);
        urlSb.append("&userPwd=").append(userPwd);
        urlSb.append("&numbers=").append(numbers);
        urlSb.append("&msgContent=").append(URLEncoder.encode(msgContent, charset));
        urlSb.append("&charset=").append(charset);

        String fullUrlStr = urlSb.toString();
        String result = "-1";
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        try {
            int idx = fullUrlStr.indexOf("?");
            String str = fullUrlStr.substring(0, idx);
            String param = fullUrlStr.substring(idx + 1);
            URL url = new URL(str);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            out = new OutputStreamWriter(con.getOutputStream(), charset);
            out.write(param);
            out.flush();
            out.close();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            result = reader.readLine();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
				out.close();
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
        
        result = result.split(Constants.COMMA)[0];
        
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("isSuccess", "1".equals(result));
        return returnMap;
	}
}
