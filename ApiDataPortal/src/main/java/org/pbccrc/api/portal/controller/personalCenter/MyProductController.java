package org.pbccrc.api.portal.controller.personalCenter;

import javax.ws.rs.GET;

import org.pbccrc.api.base.service.ProductService;
import org.pbccrc.api.base.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
public class MyProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private RelationService relationService;
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getProductApiArray", produces={"application/json;charset=UTF-8"})
	public JSONObject getMyProduct(String userID, String productID) {
		
		JSONObject object = new JSONObject();
		
		JSONArray apiArray = productService.getApiArray(productID);
		object.put("apiArray", apiArray);
		
		JSONObject relation = relationService.getRelation(userID, productID);
		object.put("relation", relation);
		
		return object;
	}
}
