package org.pbccrc.api.portal.controller.personalCenter;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import org.pbccrc.api.base.bean.ProductType;
import org.pbccrc.api.base.service.ProductService;
import org.pbccrc.api.base.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
public class MyDataController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductTypeService productTypeService;

	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getMyProduct", produces={"application/json;charset=UTF-8"})
	public JSONArray getMyProduct(String userID){
		
		return productService.queryByUser(userID);
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getAllProductType", produces={"application/json;charset=UTF-8"})
	public JSONArray getAllProductType(){
		
		JSONArray jsonArray = new JSONArray();
		
		List<ProductType> productTypeList = productTypeService.queryAll();
		
		for (ProductType productType : productTypeList) {
			jsonArray.add(JSONObject.toJSON(productType));
		}
		
		return jsonArray;
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getProductByType", produces={"application/json;charset=UTF-8"})
	public JSONArray getProductByType(@QueryParam("productType") String productType){
		
		return productService.getProductByType(productType);
	}
}
