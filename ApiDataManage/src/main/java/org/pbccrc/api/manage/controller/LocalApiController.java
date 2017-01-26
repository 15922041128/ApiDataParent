package org.pbccrc.api.manage.controller;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import org.pbccrc.api.base.bean.LocalApi;
import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.service.DataBaseService;
import org.pbccrc.api.base.service.LocalApiService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/localApi")
public class LocalApiController {
	
	@Autowired
	private LocalApiService localApiService;
	
	@Autowired
	private DataBaseService dataBaseService;

	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/queryApiByPage", produces={"application/json;charset=UTF-8"})
	public Pagination queryApiByPage(LocalApi localApi, Pagination pagination){
		Pagination pagination2 = localApiService.queryApiByPage(localApi, pagination);
		return pagination2;
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/isExist", produces={"application/json;charset=UTF-8"})
	public Integer isExist(String service){
		return localApiService.isExist(service);
	}
	
	@POST
	@ResponseBody
	@RequestMapping(value="/saveOrUpdateLocalApi", produces={"application/json;charset=UTF-8"})
	public JSONObject saveOrUpdateLocalApi(HttpServletResponse response, LocalApi localApi){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		JSONObject result = new JSONObject();
		if(localApi.getId()==0){
			localApiService.addLocalApi(localApi);
		}else{
			localApiService.updateLocalApi(localApi);
		}
		result.put("isSuccess", Constants.RET_STAT_SUCCESS);
	    return result;
	}
	
	@POST
	@ResponseBody
	@RequestMapping(value="/saveOrUpdateProduct", produces={"application/json;charset=UTF-8"})
	public JSONObject saveOrUpdateProduct(HttpServletResponse response, LocalApi localApi){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		JSONObject result = new JSONObject();
//		if(product.getID()==0){
//			productService.addProduct(product);
//		}else {
//			productService.updateProduct(product);
//		}
		result.put("isSuccess", Constants.RET_STAT_SUCCESS);
	    return result;
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/queryAllTable", produces={"application/json;charset=UTF-8"})
	public JSONArray queryAllTable() {
		
		return dataBaseService.queryAllTable();
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/queryColumnByTable", produces={"application/json;charset=UTF-8"})
	public JSONArray queryColumnByTable(String tableName) {
		
		return dataBaseService.queryColumnByTable(tableName);
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/queryApiByIds", produces={"application/json;charset=UTF-8"})
	public JSONArray queryApiByIds(String ids){
		JSONArray apis = new JSONArray();
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			JSONObject api = JSONObject.parseObject(String.valueOf(RedisClient.get("localApi_"+id)));
			apis.add(api);
		}
		return apis;
	}
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getLocalApiByID", produces={"application/json;charset=UTF-8"})
	public JSONObject getLocalApiByID(String apiID){
		JSONObject api = JSONObject.parseObject(String.valueOf(RedisClient.get("localApi_"+apiID)));
		return api;
	}

}
