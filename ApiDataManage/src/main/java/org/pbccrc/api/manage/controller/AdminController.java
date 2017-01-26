package org.pbccrc.api.manage.controller;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import org.pbccrc.api.base.bean.User;
import org.pbccrc.api.base.service.ProductService;
import org.pbccrc.api.base.service.UserService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;

	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getUserProduct", produces={"application/json;charset=UTF-8"})
	public JSONArray getUserProduct(String userID){
		
		return productService.queryByUser(userID);
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getRelation", produces={"application/json;charset=UTF-8"})
	public JSONObject getRelation(String id){
		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + id)));	
		return relation;
	}
	
	@POST
	@ResponseBody
	@RequestMapping(value="/updataUserRelation", produces={"application/json;charset=UTF-8"})
	public JSONObject updataUserRelation(HttpServletResponse response,String id, String relation){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		JSONObject result = new JSONObject();
		RedisClient.set("relation_" + id, JSON.parseObject(relation));
		result.put("isSuccess", Constants.RET_STAT_SUCCESS);
	    return result;
	}
	
	@POST
	@ResponseBody
	@RequestMapping(value="/auditAuth", produces={"application/json;charset=UTF-8"})
	public JSONObject audit(HttpServletResponse response, String id, String auth, String message){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		JSONObject result = new JSONObject();
		User user = new User();
		user.setId(Integer.valueOf(id));
		user.setAuth(auth);
		user.setMessage(message);
		userService.modifyUser(user);
		result.put("isSuccess", Constants.RET_STAT_SUCCESS);
	    return result;
	}
}
