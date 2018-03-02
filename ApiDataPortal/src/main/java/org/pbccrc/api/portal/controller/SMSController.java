package org.pbccrc.api.portal.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import org.pbccrc.api.base.service.SMSService;
import org.pbccrc.api.base.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/r/SMS")
public class SMSController {

	@Autowired
	@Qualifier("dxw_smsServiceImpl")
	private SMSService smsService;
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getVCode", produces={"application/json;charset=UTF-8"})
	public String query(@QueryParam("phoneNo") String phoneNo) throws Exception {
		
		// 生成随机4位验证码
		String vCode = StringUtil.createVCode4();
		
		// 将验证码发送至指定用户
		smsService.send(phoneNo, vCode);
		
		return vCode;
	}
}
