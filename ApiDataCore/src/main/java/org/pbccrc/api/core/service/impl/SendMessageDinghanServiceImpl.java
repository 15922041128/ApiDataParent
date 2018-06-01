package org.pbccrc.api.core.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.service.SendMessageCoreService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;

/**
 * 鼎汉
 * @author Administrator
 *
 */
@Service
public class SendMessageDinghanServiceImpl implements SendMessageCoreService{
	

	@Override
	public Map<String, Object> sendMessage(String telNos, String msgContent, String sign) throws Exception {
		
//		JSONObject object = JSONObject.parseObject(String.valueOf(RedisClient.get("sendMsgRef_" + "dinghan")));
		String[] ary = telNos.split(",");
		int subSize = 500;
		int count = ary.length % subSize == 0 ? ary.length / subSize: ary.length / subSize + 1;
		 for (int i = 0; i < count; i++) {
	          int index = i * subSize;
	          List<String> list = new ArrayList<String>();
	          int j = 0;
	              while (j < subSize && index < ary.length) {
	                  list.add(ary[index++]);
	                  j++;
	              }
	              String telStr = Joiner.on(",").join(list);  
	              Map<String, Object> resultMap = send(telStr, msgContent, sign);
	              if (!(boolean)resultMap.get("isSuccess")) {
	            	  resultMap.put("sendNum",  String.valueOf(i*500));
	            	  System.out.println(resultMap.get("responseBody"));
	            	  return resultMap;
				  }
	              System.out.println("已经发送"+telStr);
	              Thread.sleep(1000);
	    }
		 Map<String, Object> returnMap = new HashMap<String, Object>();
		 returnMap.put("sendNum", ary.length );
		 returnMap.put("responseBody","成功发送"+ary.length+"条短信");
	     returnMap.put("isSuccess", true);
		return returnMap;
		
	}
	
	private  Map<String, Object> send(String telNos, String msgContent, String sign) throws IOException{
		String host = "112.74.179.106:8080";
		String userCode = "lingxiao001";
		String userPwd = "lingxiao001";
//		String userCode = "xzs001";
//		String userPwd = "xzs001cxx";
//		String userCode = object.getString("userName");
//	    String userPwd = object.getString("password");
        String numbers = telNos;
//        sign = "【鼎汉】";
        msgContent = msgContent + sign;
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
			returnMap.put("responseBody", "鼎汉接口调用失败");
			return returnMap;
        }
        //返回值格式：[操作结果代码,批次,提交时间(格式为YYMMDDHHmmss)]
        System.out.println(result);
        returnMap.put("responseBody", result);
        returnMap.put("isSuccess", "1".equals(result.split(Constants.COMMA)[0]));
        return returnMap;
	}
	
	/**
     * splitAry方法<br>
     * @param ary 要分割的数组
     * @param subSize 分割的块大小
     * @return
     *
     */
    private static Object[] splitAry(int[] ary, int subSize) {
         int count = ary.length % subSize == 0 ? ary.length / subSize: ary.length / subSize + 1;
 
         List<List<Integer>> subAryList = new ArrayList<List<Integer>>();
 
         for (int i = 0; i < count; i++) {
          int index = i * subSize;
          List<Integer> list = new ArrayList<Integer>();
          int j = 0;
              while (j < subSize && index < ary.length) {
                  list.add(ary[index++]);
                  j++;
              }
          subAryList.add(list);
         }
          
         Object[] subAry = new Object[subAryList.size()];
          
         for(int i = 0; i < subAryList.size(); i++){
              List<Integer> subList = subAryList.get(i);
              int[] subAryItem = new int[subList.size()];
              for(int j = 0; j < subList.size(); j++){
                  subAryItem[j] = subList.get(j).intValue();
              }
              subAry[i] = subAryItem;
         }
          
         return subAry;
    }
	
	public static void main(String[] args) throws Exception {
		String content = "http://www.qilingyz.cn";
		content = "您的验证码是3123";
		new SendMessageDinghanServiceImpl().sendMessage("13821699236", content, "asd");
	}

	@Override
	public Integer getSendSize() {
		return 500;
	}
}
