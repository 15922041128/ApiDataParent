package org.pbccrc.api.manage.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;

import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/log")
public class LogController {
	
	@Autowired
	private SystemLogService systemLogService;
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/querySumLog", produces={"application/json;charset=UTF-8"})
	public Pagination getUserProduct(String userName, String productName, String dateRange, Pagination pagination){
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("userName", userName);
		queryMap.put("productName", productName);
		
		String[] dataArray = dateRange.split(",");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(dataArray.length > 1){
			queryMap.put("startDate", format.format(new Date(Long.parseLong(dataArray[0]))));
			queryMap.put("endDate", format.format(new Date(Long.parseLong(dataArray[1]))));
		}
		return systemLogService.sumLog(queryMap, pagination);
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/querySumApiLog", produces={"application/json;charset=UTF-8"})
	public Pagination querySumApiLog(String userID, String productID, String apiId, String dateRange, Pagination pagination){
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("userID", userID);
		queryMap.put("productID", productID);
		queryMap.put("localApiID", apiId);
		
		String[] dataArray = dateRange.split(",");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(dataArray.length > 1){
			queryMap.put("startDate", format.format(new Date(Long.parseLong(dataArray[0]))));
			queryMap.put("endDate", format.format(new Date(Long.parseLong(dataArray[1]))));
		}
		return systemLogService.sumApiLog(queryMap, pagination);
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getLogDetail", produces={"application/json;charset=UTF-8"})
	public Pagination getLogDetail(String userID, String productID, String dateRange, String isSuccess, Pagination pagination){
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("userID", userID);
		queryMap.put("productID", productID);
		queryMap.put("isSuccess", isSuccess);
		
		String[] dataArray = dateRange.split(",");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(dataArray.length > 1){
			queryMap.put("startDate", format.format(new Date(Long.parseLong(dataArray[0]))));
			queryMap.put("endDate", format.format(new Date(Long.parseLong(dataArray[1]))));
		}
		return systemLogService.queryLogDetail(queryMap, pagination);
	}
	
}
