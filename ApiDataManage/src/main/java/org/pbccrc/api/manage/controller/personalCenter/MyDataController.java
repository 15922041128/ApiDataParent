package org.pbccrc.api.manage.controller.personalCenter;

import javax.ws.rs.GET;

import org.pbccrc.api.base.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

@Controller
public class MyDataController {
	
	@Autowired
	private ProductService productService;

	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getMyProduct", produces={"application/json;charset=UTF-8"})
	public JSONArray getMyProduct(String userID){
		
		return productService.queryByUser(userID);
	}
}
