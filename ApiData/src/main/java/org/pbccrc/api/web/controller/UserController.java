package org.pbccrc.api.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.pbccrc.api.base.bean.User;
import org.pbccrc.api.base.service.UserService;
import org.pbccrc.api.base.util.CacheUtil;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.MyCookie;
import org.pbccrc.api.base.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CacheUtil cacheUtil;
	
	@GET
	@ResponseBody
	@RequestMapping(value="/r/user/register", produces={"text/html;charset=UTF-8"})
	public String register(
			@QueryParam("userName") String userName, 
			@QueryParam("password") String password,
			@QueryParam("compName") String compName,
			@QueryParam("compTel") String compTel,
			@QueryParam("contactName") String contactName,
			@QueryParam("contactTel") String contactTel){
		
		User user = new User();
		user.setUserName(userName);
		user.setPassword(StringUtil.string2MD5(password));
		user.setCompName(compName);
		user.setCompTel(compTel);
		user.setContactName(contactName);
		user.setContactTel(contactTel);
		
		userService.addUser(user);
		
		return "";
	}
	
	
	@GET
	@ResponseBody
	@RequestMapping(value="/r/user/isExist", produces={"text/html;charset=UTF-8"})
	public String isExist(@QueryParam("userName") String userName){
		
		String retData = "N";
		
		if(userService.isExist(userName)) {
			
			retData = "Y";
		}
		
		return retData;
	}
	
	@GET
	@ResponseBody
	@RequestMapping(value="/login", produces={"application/json;charset=UTF-8"})
	public JSONObject login(@QueryParam("userName") String userName, @QueryParam("password") String password, 
			@Context HttpServletRequest request, @Context HttpServletResponse response){
		
		JSONObject retrunJson = new JSONObject();
		
		String retData = Constants.RET_STAT_ERROR;
		
		User user = userService.login(userName, password);
		
		if (null != user && null != user.getID()) {
			retData = Constants.RET_STAT_SUCCESS;
			retrunJson.put("loginUser", user);
			
			// 将用户信息存入缓存
			MyCookie.addCookie(Constants.COOKIE_USERID, String.valueOf(user.getID()), true, response);
			cacheUtil.setObj(Constants.CACHE_USER + Constants.UNDERLINE + user.getID(), user, -1);
		}
		
		retrunJson.put("isSuccess", retData);
		
		return retrunJson;
	}
	
	@GET
	@ResponseBody
	@RequestMapping(value="/r/user/resetPassword", produces={"text/html;charset=UTF-8"})
	public String resetPassword(@QueryParam("password") String password, @Context HttpServletRequest request){
		
		String retData = Constants.RET_STAT_ERROR;
		
		String userID = MyCookie.getCookie(Constants.COOKIE_USERID, true, request);
		
		userService.resetPassword(Integer.parseInt(userID), password);
		
		return retData;
	}
	
	@GET
	@ResponseBody
	@RequestMapping(value="/r/user/getUser", produces={"application/json;charset=UTF-8"})
	public String getUser(@Context HttpServletRequest request){
		
		String userID = MyCookie.getCookie(Constants.COOKIE_USERID, true, request);
		User currentUser = (User)cacheUtil.getObj(Constants.CACHE_USER + Constants.UNDERLINE  + userID);
		
		return JSONObject.toJSONString(currentUser);
	}
	
	@GET
	@ResponseBody
	@RequestMapping(value="/r/user/modifyUser", produces={"text/html;charset=UTF-8"})
	public String modifyUser(
			@QueryParam("compName") String compName,
			@QueryParam("compTel") String compTel,
			@QueryParam("contactName") String contactName,
			@QueryParam("contactTel") String contactTel, @Context HttpServletRequest request){
		
		String retData = Constants.RET_STAT_ERROR;
		
		String userID = MyCookie.getCookie(Constants.COOKIE_USERID, true, request);
		
		User user = new User();
		user.setID(Integer.parseInt(userID));
		user.setCompName(compName);
		user.setCompTel(compTel);
		user.setContactName(contactName);
		user.setContactTel(contactTel);
		
		userService.modifyUser(user);
		
		return retData;
	}
	
	@GET
	@RequestMapping(value="/r/user/loginOut")
	public void loginOut(@Context HttpServletRequest request) {
		
		String userID = MyCookie.getCookie(Constants.COOKIE_USERID, true, request);
		cacheUtil.delObj(Constants.CACHE_USER + Constants.UNDERLINE  + userID);
	}
}
