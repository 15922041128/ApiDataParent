package org.pbccrc.api.portal.controller;

import javax.ws.rs.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
public class DataManagerController {

	@GET
	@ResponseBody
	@RequestMapping(value="/dataManager", produces={"application/json;charset=UTF-8"})
	public JSONObject isExist(String productID){
		
		JSONObject retrunJson = new JSONObject();
		retrunJson.put("productID", productID);
		
		return retrunJson;  
	}
}
