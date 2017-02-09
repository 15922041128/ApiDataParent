package org.pbccrc.api.manage.controller;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;

import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.bean.Product;
import org.pbccrc.api.base.service.DataBaseService;
import org.pbccrc.api.base.service.ProductService;
import org.pbccrc.api.base.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private DataBaseService dataBaseService;

	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/queryProductByPage", produces={"application/json;charset=UTF-8"})
	public Pagination queryProductByPage(Product product, Pagination pagination){
		Pagination pagination2 = productService.queryProductByPage(product, pagination);
		return pagination2;
	}
	
	
	@POST
	@ResponseBody
	@RequestMapping(value="/saveOrUpdateProduct", produces={"application/json;charset=UTF-8"})
	public JSONObject saveOrUpdateProduct(HttpServletResponse response, Product product){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		JSONObject result = new JSONObject();
		if(product.getId()==0){
			productService.addProduct(product);
		}else {
			productService.updateProduct(product);
		}
		result.put("isSuccess", Constants.RET_STAT_SUCCESS);
	    return result;
	}
	

	@POST
	@ResponseBody
	@RequestMapping(value="/updateProduct", produces={"application/json;charset=UTF-8"})
	public JSONObject updateProduct(HttpServletResponse response, Product product){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		JSONObject result = new JSONObject();
		productService.updateProduct(product);
		result.put("isSuccess", Constants.RET_STAT_SUCCESS);
	    return result;
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getProductByID", produces={"application/json;charset=UTF-8"})
	public JSONObject getProductByID(@QueryParam("productID") String productID){
		
		JSONObject product = productService.getProductInfo(productID);
		return product;
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/deleteProduct", produces={"application/json;charset=UTF-8"})
	public JSONObject deleteProduct(@QueryParam("productID") String productID){
		Product product = new Product();
		product.setStatus(Constants.DELETE);
		productService.updateProduct(product);
		JSONObject result = new JSONObject();
		result.put("isSuccess", Constants.RET_STAT_SUCCESS);
		return result;
	}
	

}
