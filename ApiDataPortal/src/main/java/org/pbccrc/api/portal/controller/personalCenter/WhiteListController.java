package org.pbccrc.api.portal.controller.personalCenter;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import org.pbccrc.api.base.service.RelationService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
public class WhiteListController {
	
	@Autowired
	private RelationService relationService;

	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getWhiteList", produces={"application/json;charset=UTF-8"})
	public JSONArray getWhiteList(@QueryParam("userID") String userID){
		
		JSONArray whiteList = new JSONArray();
		
		JSONArray relationArray = relationService.getRelation(userID);
		
		for (Object object : relationArray) {
			JSONObject jsonObject = new JSONObject();
			JSONObject obj = (JSONObject) JSONObject.toJSON(object);
			jsonObject.put("productName", ((JSONObject)obj.get("product")).getString("name"));
			String allowIps = obj.getString("allowIps");
			if (StringUtil.isNull(allowIps)) {
				allowIps = Constants.BLANK;
			} else {
				allowIps = allowIps.replace(Constants.ENTER, Constants.COMMA);
			}
			jsonObject.put("allowIps", allowIps);
			whiteList.add(jsonObject);
		}
		
		return whiteList;
	}
}
