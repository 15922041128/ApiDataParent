package org.pbccrc.api.manage.controller.personalCenter;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import org.pbccrc.api.base.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
public class ApplyController {
	
	@Autowired
	private ApplyService applyService;
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/apply", produces={"application/json;charset=UTF-8"})
	public JSONObject apply(@QueryParam("userID") String userID, @QueryParam("productID") String productID) {
		
		JSONObject object = new JSONObject();
		
		boolean isSuccess = applyService.doApply(userID, productID);
		
		object.put("isSuccess", isSuccess ? "Y" : "N");
		
		return object;
	}

}
