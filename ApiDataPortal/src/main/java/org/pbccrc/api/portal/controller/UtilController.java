package org.pbccrc.api.portal.controller;

import java.util.Map;

import javax.ws.rs.GET;

import org.pbccrc.api.base.service.LocalApiService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.base.util.StringUtil.JSON_TYPE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
public class UtilController {
	
	@Autowired
	private LocalApiService localApiService;

	/**
	 * 中英文转换
	 * @param service
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping("/change2Ch")
	public String change2Ch(String service, String jsonStr) throws Exception {
		
		Map<String, Object> localApi = localApiService.queryByService(service);
		// 获得返回值信息
		String returnParam = String.valueOf(localApi.get("returnParam"));
		JSONArray paramArray = JSONArray.parseArray(returnParam);
		
		String returnStr = Constants.BLANK;
		
		// 判断json类型
		JSON_TYPE jsonType = StringUtil.getJSONType(jsonStr);
		if (JSON_TYPE.JSON_TYPE_OBJECT == jsonType) {
			// JSONObject
			JSONObject jsonObject = JSONObject.parseObject(jsonStr);
			for (int i = 0; i < paramArray.size(); i++) {
				JSONObject object = (JSONObject) JSONObject.toJSON(paramArray.get(i));
				String enName = object.getString(Constants.EN_NAME);
				String chName = object.getString(Constants.CH_NAME);
				
				String value = jsonObject.getString(enName);
				jsonObject.put(chName, value);
				jsonObject.remove(enName);
			}
			
			returnStr = jsonObject.toJSONString();
		} else if (JSON_TYPE.JSON_TYPE_ARRAY == jsonType) {
			JSONArray retrunArray = new JSONArray();
			// JSONArray
			JSONArray jsonArray = JSONArray.parseArray(jsonStr);
			// 遍历jsonArray
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				for (int j = 0; j < paramArray.size(); j++) {
					JSONObject object = (JSONObject) JSONObject.toJSON(paramArray.get(j));
					String enName = object.getString(Constants.EN_NAME);
					String chName = object.getString(Constants.CH_NAME);
					
					String value = jsonObject.getString(enName);
					jsonObject.put(chName, value);
					jsonObject.remove(enName);
				}
				retrunArray.add(jsonObject);
			}
			returnStr = retrunArray.toJSONString();
		}
		
		return returnStr;
	}
}
