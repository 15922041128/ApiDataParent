package org.pbccrc.api.portal.controller;

import javax.ws.rs.GET;

import org.pbccrc.api.base.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
public class DataManagerController {
	
	@Autowired
	private ProductService productService;

	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/dataManager", produces={"application/json;charset=UTF-8"})
	public JSONObject dataManager(String productID){
		
		return productService.getProductInfo(productID);  
	}
}
