package org.pbccrc.api.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.pbccrc.api.base.bean.User;
import org.pbccrc.api.base.service.UserService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/r/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GET
	@ResponseBody
	@RequestMapping(value="/register", produces={"application/json;charset=UTF-8"})
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
	@RequestMapping(value="/isExist", produces={"text/html;charset=UTF-8"})
	public String isExist(@QueryParam("userName") String userName){
		
		String retData = "N";
		
		if(userService.isExist(userName)) {
			
			retData = "Y";
		}
		
		return retData;
	}
	
	@GET
	@ResponseBody
	@RequestMapping(value="/login", produces={"text/html;charset=UTF-8"})
	public String login(@QueryParam("userName") String userName, @QueryParam("password") String password, @Context HttpServletRequest request){
		
		String retData = Constants.RET_STAT_ERROR;
		
		User user = userService.login(userName, password);
		
		if (null != user && null != user.getID()) {
			retData = Constants.RET_STAT_SUCCESS;
			request.getSession().setAttribute(Constants.CURRENT_USER, user);
		}
		
		return retData;
	}
	
	@GET
	@ResponseBody
	@RequestMapping(value="/resetPassword", produces={"text/html;charset=UTF-8"})
	public String resetPassword(@QueryParam("password") String password, @Context HttpServletRequest request){
		
		String retData = Constants.RET_STAT_ERROR;
		
		User user = (User) request.getSession().getAttribute(Constants.CURRENT_USER);
		
		userService.resetPassword(user.getID(), password);
		
		return retData;
	}
	
	@GET
	@ResponseBody
	@RequestMapping(value="/getUser", produces={"application/json;charset=UTF-8"})
	public String getUser(@Context HttpServletRequest request){
		
		User currentUser = (User) request.getSession().getAttribute(Constants.CURRENT_USER);
		
		return JSONObject.toJSONString(currentUser);
	}
	
	@GET
	@ResponseBody
	@RequestMapping(value="/modifyUser", produces={"text/html;charset=UTF-8"})
	public String modifyUser(
			@QueryParam("compName") String compName,
			@QueryParam("compTel") String compTel,
			@QueryParam("contactName") String contactName,
			@QueryParam("contactTel") String contactTel, @Context HttpServletRequest request){
		
		String retData = Constants.RET_STAT_ERROR;
		
		User currentUser = (User) request.getSession().getAttribute(Constants.CURRENT_USER);
		
		User user = new User();
		user.setID(currentUser.getID());
		user.setCompName(compName);
		user.setCompTel(compTel);
		user.setContactName(contactName);
		user.setContactTel(contactTel);
		
		userService.modifyUser(user);
		
		return retData;
	}
}
