package org.pbccrc.api.base.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@ControllerAdvice
public class BaseController {
	
	@ExceptionHandler
	@ResponseBody
	public String exp(HttpServletRequest request, Exception ex) {

		String errMessage = "服务器内部错误";
		
		JSONObject object = new JSONObject();
		object.put("success", false);
		object.put("data", null);
		object.put("code", "100106");
		object.put("retMsg", errMessage);
		
		ex.printStackTrace();
		
		return object.toJSONString();
	}
}
