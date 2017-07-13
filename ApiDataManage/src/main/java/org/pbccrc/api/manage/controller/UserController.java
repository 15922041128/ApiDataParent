package org.pbccrc.api.manage.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.bean.User;
import org.pbccrc.api.base.service.UserService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/r/user/register", produces={"text/html;charset=UTF-8"})
	public String register(
			@QueryParam("userName") String userName, 
			@QueryParam("password") String password,
			@QueryParam("compName") String compName,
			@QueryParam("compTel") String compTel,
			@QueryParam("contactName") String contactName,
			@QueryParam("contactTel") String contactTel) throws Exception {
		
		User user = new User();
		user.setUserName(userName);
		user.setPassword(StringUtil.string2MD5(password));
		user.setCompName(compName);
		user.setCompTel(compTel);
		user.setContactName(contactName);
		user.setContactTel(contactTel);
		user.setAuth(Constants.AUTH_STATUS_NO);
		user.setUserState(Constants.USER_STATE_OPEN); // 设置用户状态为启用
		
		userService.addUser(user);
		
		return "";
	}
	
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/r/user/isExist", produces={"text/html;charset=UTF-8"})
	public String isExist(@QueryParam("userName") String userName) throws UnsupportedEncodingException{
		
		String retData = "N";
		
		if(userService.isExist(new String(userName.getBytes("utf-8")))) {
			
			retData = "Y";
		}
		
		return retData;
	}
	
	@POST
	@ResponseBody
	@RequestMapping(value="/login", produces={"application/json;charset=UTF-8"})
	public JSONObject login(@QueryParam("userName") String userName, @QueryParam("password") String password, 
			@Context HttpServletRequest request, @Context HttpServletResponse response){
		
	    response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		
		JSONObject retrunJson = new JSONObject();
		
		String retData = Constants.RET_STAT_ERROR;
		
		User user = userService.login(userName, password);
		
		if (null != user && null != user.getId()) {
			if(user.getUserState() != 1){
				retrunJson.put("errorMsg", "用户已被停用，请联系管理员");
			}else if(!user.getRole().equals("9999")){
				retrunJson.put("errorMsg", "用户权限不足");
			}else{
				retData = Constants.RET_STAT_SUCCESS;
				retrunJson.put("loginUser", user);
			}
		}else{
			retrunJson.put("errorMsg", "用户名密码不正确");
		}
		retrunJson.put("isSuccess", retData);
		return retrunJson;
		
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/r/user/resetPassword", produces={"text/html;charset=UTF-8"})
	public String resetPassword(@QueryParam("userID") String userID, @QueryParam("password") String password){
		
		userService.resetPassword(userID, password);
		
		return Constants.RET_STAT_SUCCESS;
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/r/user/getUser", produces={"application/json;charset=UTF-8"})
	public String getUser(String userID, @Context HttpServletRequest request){
		
		User currentUser = userService.getUserByID(userID);
		
		return JSONObject.toJSONString(currentUser);
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/r/user/getUserByPage", produces={"application/json;charset=UTF-8"})
	public Pagination getUserByPage(@Context HttpServletRequest request,@ModelAttribute Pagination pagination,@ModelAttribute User user){
		
		return  userService.queryAllUser(user, pagination);
		
	}
	
	@POST
	@ResponseBody
	@RequestMapping(value="/r/user/test", produces={"application/json;charset=UTF-8"})
	public Pagination test(HttpServletRequest request, HttpServletResponse response,Pagination pagination, User user){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		return  userService.queryAllUser(user, pagination);
		
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/r/user/getUserByID", produces={"application/json;charset=UTF-8"})
	public String getUserByID(@QueryParam("userID") String userID){
		
		User currentUser = userService.getUserByID(userID);
		return JSONObject.toJSONString(currentUser);
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/r/user/modifyUser", produces={"text/html;charset=UTF-8"})
	public String modifyUser(
			@QueryParam("userID") String userID,
			@QueryParam("compName") String compName,
			@QueryParam("compTel") String compTel,
			@QueryParam("contactName") String contactName,
			@QueryParam("contactTel") String contactTel, @Context HttpServletRequest request){
		
		String retData = Constants.RET_STAT_ERROR;
		
		User user = new User();
		user.setId(userID);
		user.setCompName(compName);
		user.setCompTel(compTel);
		user.setContactName(contactName);
		user.setContactTel(contactTel);
		
		userService.modifyUser(user);
		
		return retData;
	}
		
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getApiKey", produces={"application/json;charset=UTF-8"})
	public JSONObject getApiKey(@QueryParam("userID") String userID, @QueryParam("productID") String productID){
		
		JSONObject object = new JSONObject();
		
		// apiKey
		String apiKey = Constants.BLANK;
		
		// 根据用户ID获取当前用户已购买的产品关系
		List<Map<String, Object>> relationList = RedisClient.fuzzyQuery("relation_" + userID + "_");
		for (Map<String, Object> relation : relationList) {
			// 判断当前产品ID是否和传入的相等
			
			Set<String> keySet = relation.keySet();
			
			for (String key : keySet) {
				JSONObject relationObj = JSONObject.parseObject(String.valueOf(relation.get(key)));
				if (productID.equals(relationObj.getString("productID"))) {
					// 如果相等则获取当前relation的apiKey
					apiKey = String.valueOf(relationObj.get("apiKey"));
					break;
				}
			}
		}
		
		object.put("apiKey", apiKey);
		
		return object;
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/r/user/passwordIsTrue", produces={"application/json;charset=UTF-8"})
	public JSONObject passwordIsTrue(@QueryParam("userID") String userID, @QueryParam("password") String password){
		
		JSONObject object = new JSONObject();
		
		boolean passwordIsTrue = userService.passwordIsTrue(userID, password);
		
		if (passwordIsTrue) {
			object.put("passwordIsTrue", "Y");
		} else {
			object.put("passwordIsTrue", "N");
		}
		
		return object;
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/r/user/getApiUser", produces={"application/json;charset=UTF-8"})
	public JSONObject getApiUser(@QueryParam("userID") String userID){
		
		return JSONObject.parseObject(String.valueOf(RedisClient.get("apiUser_" + userID)));
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/r/user/updateUserState", produces={"text/html;charset=UTF-8"})
	public String updateUserState(@QueryParam("userState") String id, @QueryParam("userState") Integer userState){
		String retData = Constants.RET_STAT_ERROR;
		if(userState != null && (userState == 1|| userState == 0)){
			User user = new User();
			user.setId(id);
			user.setUserState(userState);
			userService.modifyUser(user);
			retData = Constants.RET_STAT_SUCCESS;
		}
		return retData;
	}}