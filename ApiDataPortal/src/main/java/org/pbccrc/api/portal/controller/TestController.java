package org.pbccrc.api.portal.controller;

import javax.ws.rs.GET;

import org.pbccrc.api.base.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

@Controller
public class TestController {
	
	@Autowired
	private TestService testService;

	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/queryAllApi", produces={"application/json;charset=UTF-8"})
	public JSONArray queryAllApi(String productID){
		
		return testService.queryAllApi(productID);
	}
	
}
