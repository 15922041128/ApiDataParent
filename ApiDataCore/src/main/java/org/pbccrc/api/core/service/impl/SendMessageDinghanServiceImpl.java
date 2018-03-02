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
import org.pbccrc.api.base.util.RedisClient;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * 鼎汉
 * @author Administrator
 *
 */
@Service
public class SendMessageDinghanServiceImpl implements SendMessageCoreService{
	

	@Override
	public Map<String, Object> sendMessage(String telNos, String msgContent, String sign) throws Exception {
		
		JSONObject object = JSONObject.parseObject(String.valueOf(RedisClient.get("sendMsgRef_" + "dinghan")));
		
		String host = "112.74.179.106:8080";
//      String userCode = "xzs001";
//      String userPwd = "xzs001cxx";
		String userCode = object.getString("userName");
	    String userPwd = object.getString("password");
        String numbers = telNos;
<<<<<<< HEAD
=======
        sign = "【鼎汉】";
        msgContent = msgContent + sign;
>>>>>>> ea99450892d2be5994d66f7ad823d33ac5625fec
        String charset = "GBK";

        StringBuffer urlSb = new StringBuffer();
        urlSb.append("http://").append(host).append("/Message.sv?method=sendMsg");
        urlSb.append("&userCode=").append(userCode);
        urlSb.append("&userPwd=").append(userPwd);
        urlSb.append("&numbers=").append(numbers);
        urlSb.append("&msgContent=").append(URLEncoder.encode(msgContent, charset));
        urlSb.append("&charset=").append(charset);

        Map<String, Object> returnMap = new HashMap<String, Object>();
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
			out.close();
			reader.close();
			returnMap.put("isSuccess", false);
			return returnMap;
        }
        //返回值格式：[操作结果代码,批次,提交时间(格式为YYMMDDHHmmss)]
        returnMap.put("feedBack", result);
        returnMap.put("isSuccess", "1".equals(result.split(Constants.COMMA)));
        return returnMap;
	}

	@Override
	public Integer getSendSize() {
		return 500;
	}
}
