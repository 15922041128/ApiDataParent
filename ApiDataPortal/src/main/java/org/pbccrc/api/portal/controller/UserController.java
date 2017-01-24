package org.pbccrc.api.portal.controller;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.pbccrc.api.base.bean.User;
import org.pbccrc.api.base.service.UserService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@SuppressWarnings("deprecation")
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
			@QueryParam("contactTel") String contactTel){
		
		User user = new User();
		user.setUserName(userName);
		user.setPassword(StringUtil.string2MD5(password));
		user.setCompName(compName);
		user.setCompTel(compTel);
		user.setContactName(contactName);
		user.setContactTel(contactTel);
		user.setAuth(Constants.AUTH_STATUS_NO);
		
		userService.addUser(user);
		
		return "";
	}
	
	
	@GET
	@CrossOrigin
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
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/login", produces={"application/json;charset=UTF-8"})
	public JSONObject login(@QueryParam("userName") String userName, @QueryParam("password") String password, 
			@Context HttpServletRequest request, @Context HttpServletResponse response){
		
		JSONObject retrunJson = new JSONObject();
		
		String retData = Constants.RET_STAT_ERROR;
		
		User user = userService.login(userName, password);
		
		if (null != user && null != user.getId()) {
			if(user.getUserState() != 1){
				retrunJson.put("errorMsg", "用户已被停用，请联系管理员");
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
		
		userService.resetPassword(Integer.parseInt(userID), password);
		
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
	@RequestMapping(value="/r/user/getUserByID", produces={"application/json;charset=UTF-8"})
	public String getUserByID(@QueryParam("userID") String userID) {
		
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
		user.setId(Integer.parseInt(userID));
		user.setCompName(compName);
		user.setCompTel(compTel);
		user.setContactName(contactName);
		user.setContactTel(contactTel);
		
		userService.modifyUser(user);
		
		return retData;
	}
	
	@GET
	@CrossOrigin
	@RequestMapping(value="/r/user/loginOut")
	public void loginOut(@Context HttpServletRequest request) {
		
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
	
	/**
	 * 实名认证
	 * @return
	 */
	@POST
	@ResponseBody
	@RequestMapping(value="/r/user/certify", produces={"application/json;charset=UTF-8"})
	public JSONObject certify(
			@QueryParam("userID") String userID,
			@QueryParam("province") String province,
			@QueryParam("city") String city,
			@QueryParam("area") String area,
			@QueryParam("address") String address,
			@QueryParam("license") String license,
			@QueryParam("taxRegCertify") String taxRegCertify,
			@QueryParam("org") String org,
			@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", Constants.RET_STAT_SUCCESS);
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		
		String filePath = Constants.BLANK;
		filePath = request.getSession().getServletContext().getRealPath("/") + Constants.FILE_PATH_BASE + File.separator + Constants.FILE_PATH_LICENSE;
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		DiskFileUpload fu = new DiskFileUpload();
		fu.setSizeMax(10485760);
		fu.setSizeThreshold(4096);
		
		List<FileItem> fileItems = fu.parseRequest(request);
		
		File photo = null;
		FileItem item = fileItems.get(0);
		String fileName = item.getName();
		if (!StringUtil.isNull(fileName)) {
			fileName = fileName.substring(fileName.indexOf(Constants.POINT), fileName.length());
			fileName = System.currentTimeMillis() + fileName;
			photo = new File(filePath + File.separator + fileName);
			item.write(photo);
		}
		
		User user = userService.getUserByID(userID);
		user.setProvince(province);
		user.setCity(city);
		user.setArea(area);
		user.setAddress(address);
		user.setLicense(license);
		user.setTaxRegCertify(taxRegCertify);
		user.setOrg(org);
		user.setAuth(Constants.AUTH_STATUS_WAIT);
		// 如果用户重新上传图片,则覆盖当前图片
		if (!StringUtil.isNull(fileName)) {
			user.setLicenseImg(photo.getName());
		}
		
		userService.modifyUser(user);
		user = userService.getUserByID(userID);
		
		jsonObject.put("user", user);
		
		return jsonObject;
	}
	
}
