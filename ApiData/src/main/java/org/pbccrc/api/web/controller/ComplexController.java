package org.pbccrc.api.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;

import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.base.service.ComplexService;
import org.pbccrc.api.base.service.LocalApiService;
import org.pbccrc.api.base.service.SystemLogService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.Validator;
import org.pbccrc.api.base.util.pdf.PdfBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/r/complex")
public class ComplexController {
	
	@Autowired
	private LocalApiService localApiService;
	
	@Autowired
	private ComplexService complexService;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private PdfBuilder pdfBuilder;
	
	/**
	 * 失信人查询(生成pdf)
	 * @param identifier
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@ResponseBody
	@RequestMapping("/sxr")
	public JSONObject querySxr(String identifier, String queryItem, HttpServletRequest request) throws Exception{
		
		String[] queryItems = queryItem.split(Constants.COMMA);
		
		String bathPath = Constants.FILE_DOWNLOAD_SXR_PDF;
		
		Map<String, Object> queryResult = complexService.querySxr(identifier, queryItems);
		
		JSONObject result = new JSONObject();
		
		// 判断内码中是否存在被查询用户
		String isNull = "N";
		isNull = String.valueOf(queryResult.get("isNull"));
		if ("Y".equals(isNull)) {
			result.put("isNull", isNull);
			return result;
		}
		
		String filePath = pdfBuilder.getPDF(JSON.toJSONString(queryResult), queryItems, bathPath, request);
		result.put("filePath", "/ApiData" + bathPath + filePath);
		result.put("isNull", isNull);
		
		return result;
	}

	/**
	 * 查询pdf单项
	 * @param service
	 * @param identifier
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@ResponseBody
	@RequestMapping("/getPdfItem")
	public String query(String service, String identifier, HttpServletRequest request) throws Exception {
		
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		ResultContent resultContent = new ResultContent();
		resultContent.setErrNum(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		Map<String, Object> localApi = localApiService.queryByService(service);
		
		// 验证本地是否有该api
		if (null == localApi) {
			resultContent.setErrNum(Constants.ERR_NO_SERVICE);
			resultContent.setRetMsg(Constants.RET_MSG_NO_SERVICE);
			return JSONObject.toJSONString(resultContent);
		}
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, localApi, resultContent)) {
			return JSONObject.toJSONString(resultContent);
		}
		
		Map<String, Object> returnMap = complexService.getPdfItem(service, identifier, localApi);
		
		String isNull = String.valueOf(returnMap.get("isNull"));
		// 判断是否为空
		if ("Y".equals(isNull)) {
			resultContent.setErrNum(Constants.CODE_ERR_FAIL);
			resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
		} else {
			resultContent.setRetData(returnMap.get("returnStr"));
		}
		
		// 记录日志
		SystemLog systemLog = new SystemLog();
		// ip地址
		systemLog.setIpAddress(request.getRemoteAddr());
		// apiKey
		systemLog.setApiKey(apiKey);
		// localApiID
		systemLog.setLocalApiID(String.valueOf(localApi.get("ID")));
		// 参数
		JSONObject params = new JSONObject();
		params.put("identifier", identifier);
		systemLog.setParams(params.toJSONString());
		// 用户ID
		systemLog.setUserID(userID);
		// 是否成功
		systemLog.setIsSuccess(String.valueOf(!"Y".equals(isNull)));
		// 是否计费
		systemLog.setIsCount(String.valueOf(!"Y".equals(isNull)));
		// 查询时间
		systemLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		systemLogService.addLog(systemLog);
		
		return resultContent.toString();
	}
	
	/**
	 * 自定义查询pdf
	 * @param service
	 * @param identifier
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@ResponseBody
	@RequestMapping("/getPdfCustom")
	public String queryCustom(String service, String identifier, HttpServletRequest request) throws Exception {
		
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		ResultContent resultContent = new ResultContent();
		resultContent.setErrNum(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		Map<String, Object> localApi = localApiService.queryByService(service);
		
		// 验证本地是否有该api
		if (null == localApi) {
			resultContent.setErrNum(Constants.ERR_NO_SERVICE);
			resultContent.setRetMsg(Constants.RET_MSG_NO_SERVICE);
			return JSONObject.toJSONString(resultContent);
		}
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, localApi, resultContent)) {
			return JSONObject.toJSONString(resultContent);
		}
		
		Map<String, Object> returnMap = complexService.getPdfCustom(identifier, localApi);
		
		String isNull = String.valueOf(returnMap.get("isNull"));
		// 判断是否为空
		if ("Y".equals(isNull)) {
			resultContent.setErrNum(Constants.CODE_ERR_FAIL);
			resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
		} else {
			resultContent.setRetData(returnMap.get("returnStr"));
		}
		
		// 记录日志
		SystemLog systemLog = new SystemLog();
		// ip地址
		systemLog.setIpAddress(request.getRemoteAddr());
		// apiKey
		systemLog.setApiKey(apiKey);
		// localApiID
		systemLog.setLocalApiID(String.valueOf(localApi.get("ID")));
		// 参数
		JSONObject params = new JSONObject();
		params.put("identifier", identifier);
		systemLog.setParams(params.toJSONString());
		// 用户ID
		systemLog.setUserID(userID);
		// 是否成功
		systemLog.setIsSuccess(String.valueOf(!"Y".equals(isNull)));
		// 是否计费
		systemLog.setIsCount(String.valueOf(!"Y".equals(isNull)));
		// 查询时间
		systemLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		systemLogService.addLog(systemLog);
		
		return resultContent.toString();
	}
}
