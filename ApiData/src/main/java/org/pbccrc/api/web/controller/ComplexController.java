package org.pbccrc.api.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;

import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.base.service.ComplexService;
import org.pbccrc.api.base.service.CostService;
import org.pbccrc.api.base.service.LocalApiService;
import org.pbccrc.api.base.service.SystemLogService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.base.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
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
	private CostService costService;
	
//	/**
//	 * 失信人查询验证
//	 * @param identifier
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@GET
//	@CrossOrigin
//	@ResponseBody
//	@RequestMapping("/validateSxr")
//	public String validateSxr(String identifier) throws Exception{
//		
//		return complexService.validateSxr(identifier);
//	}
	
//	/**
//	 * 失信人查询(生成pdf)
//	 * @param identifier
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@GET
//	@CrossOrigin
//	@RequestMapping("/sxr")
//	public ResponseEntity<byte[]> querySxr(String identifier, String queryItem, HttpServletRequest request) throws Exception{
//		
//		String[] queryItems = queryItem.split(Constants.COMMA);
//		
//		// 获取当前userID
//		String userID = MyCookie.getCookie(Constants.COOKIE_USERID, true, request);
//		
//		// pdf路径
//		String path = Constants.FILE_DOWNLOAD_SXR_PDF;
//		
//		// 生成UUID
//		String uuid = StringUtil.createUUID();
//		
//		Map<String, Object> queryResult = complexService.querySxr(uuid, userID, identifier, queryItems);
//		
//		String fileName = pdfBuilder.getPDF(JSON.toJSONString(queryResult), queryItems, path, request);
//		
//		File file = new File(request.getSession().getServletContext().getRealPath(path) + File.separator + fileName);
//		
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentDispositionFormData("attachment", fileName);   
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); 
//        
// 		// 获取是否成功
// 		String isNull = String.valueOf(queryResult.get("isNull"));
// 		
//        // 记录日志
// 		SystemLog systemLog = new SystemLog();
// 		// uuid
// 		systemLog.setUuid(uuid);
// 		// ip地址
// 		systemLog.setIpAddress(request.getRemoteAddr());
// 		// apiKey
// 		systemLog.setApiKey(Constants.BLANK);
// 		// localApiID
// 		systemLog.setLocalApiID(Constants.API_ID_PAGE_PDF);
// 		// 参数
// 		JSONObject params = new JSONObject();
// 		params.put("identifier", identifier);
// 		params.put("queryItems", queryItems);
// 		systemLog.setParams(params.toJSONString());
// 		// 用户ID
// 		systemLog.setUserID(userID);
// 		// 是否成功
// 		systemLog.setIsSuccess(String.valueOf(!"Y".equals(isNull)));
// 		// 是否计费
// 		systemLog.setIsCount(String.valueOf(!"Y".equals(isNull)));
// 		// 查询时间
// 		systemLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
// 		systemLogService.addLog(systemLog);
//		
//		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
//                headers, HttpStatus.OK);
//	}
	

//	/**
//	 * 查询pdf单项
//	 * @param service
//	 * @param identifier
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@GET
//	@CrossOrigin
//	@ResponseBody
//	@RequestMapping(value="/getPdfItem", produces={"application/json;charset=UTF-8"})
//	public JSONObject query(String service, String name, String identifier, String telNum, HttpServletRequest request) throws Exception {
//		
//		// 获取apiKey
//		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
//		// 获得用ID
//		String userID = request.getHeader(Constants.HEAD_USER_ID);
//		
//		ResultContent resultContent = new ResultContent();
//		resultContent.setCode(Constants.ERR_NO_RESULT);
//		resultContent.setRetMsg(Constants.RET_MSG_NO_RESULT);
//		
//		Map<String, Object> localApi = localApiService.queryByService(service);
//		
//		// 验证本地是否有该api
//		if (null == localApi) {
//			resultContent.setCode(Constants.ERR_NO_SERVICE);
//			resultContent.setRetMsg(Constants.RET_MSG_NO_SERVICE);
//			return (JSONObject) JSONObject.toJSON(resultContent);
//		}
//		
//		// 请求参数验证
//		if (!validator.validateRequest(userID, apiKey, localApi, name, identifier, telNum, resultContent)) {
//			return (JSONObject) JSONObject.toJSON(resultContent);
//		}
//		
//		// 生成UUID
//		String uuid = StringUtil.createUUID();
//		
//		Map<String, Object> returnMap = complexService.getPdfItem(uuid, userID, service, name, identifier, telNum, localApi);
//		
//		String isNull = String.valueOf(returnMap.get("isNull"));
//		// 判断是否为空
//		if ("N".equals(isNull)) {
//			JSONArray jsonArray = (JSONArray) JSONArray.toJSON(returnMap.get("returnStr"));
//			if (!jsonArray.isEmpty()) {
//				resultContent.setCode(Constants.CODE_ERR_SUCCESS);
//				resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);				
//			}
//			resultContent.setRetData(returnMap.get("returnStr"));
//			// 计费
//			Map<String, Object> costRetMap = costService.cost(userID, apiKey);
//			String queryCount = String.valueOf(costRetMap.get("queryCount"));
//			// 查询次数
//			resultContent.setQueryCount(queryCount);
//		} else {
//			resultContent.setCode(Constants.ERR_NO_RESULT);
//			resultContent.setRetMsg(Constants.RET_MSG_NO_RESULT);
//		}
//		
//		// 记录日志
//		SystemLog systemLog = new SystemLog();
//		// uuid
//		systemLog.setUuid(uuid);
//		// ip地址
//		systemLog.setIpAddress(request.getRemoteAddr());
//		// apiKey
//		systemLog.setApiKey(apiKey);
//		// localApiID
//		systemLog.setLocalApiID(String.valueOf(localApi.get("ID")));
//		// 参数
//		JSONObject params = new JSONObject();
//		params.put("identifier", identifier);
//		systemLog.setParams(params.toJSONString());
//		// 用户ID
//		systemLog.setUserID(userID);
//		// 是否成功
//		systemLog.setIsSuccess(String.valueOf(!"Y".equals(isNull)));
//		// 是否计费
//		systemLog.setIsCount(String.valueOf(!"Y".equals(isNull)));
//		// 查询时间
//		systemLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		systemLogService.addLog(systemLog);
//		
//		return (JSONObject) JSONObject.toJSON(resultContent);
//	}
	
//	/**
//	 * 自定义查询pdf
//	 * @param service
//	 * @param identifier
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@GET
//	@CrossOrigin
//	@ResponseBody
//	@RequestMapping(value="/getPdfCustom", produces={"application/json;charset=UTF-8"})
//	public String queryCustom(String service, String name, String identifier, HttpServletRequest request) throws Exception {
//		
//		// 获取apiKey
//		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
//		// 获得用ID
//		String userID = request.getHeader(Constants.HEAD_USER_ID);
//		
//		ResultContent resultContent = new ResultContent();
//		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
//		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
//		
//		Map<String, Object> localApi = localApiService.queryByService(service);
//		
//		// 验证本地是否有该api
//		if (null == localApi) {
//			resultContent.setCode(Constants.ERR_NO_SERVICE);
//			resultContent.setRetMsg(Constants.RET_MSG_NO_SERVICE);
//			return JSONObject.toJSONString(resultContent);
//		}
//		
//		// 请求参数验证
//		if (!validator.validateRequest(userID, apiKey, String.valueOf(localApi.get("ID")), resultContent)) {
//			return JSONObject.toJSONString(resultContent);
//		}
//		
//		// 生成UUID
//		String uuid = StringUtil.createUUID();
//		
//		Map<String, Object> returnMap = complexService.getPdfCustom(uuid, userID, name, identifier, localApi);
//		
//		String isNull = String.valueOf(returnMap.get("isNull"));
//		// 判断是否为空
//		if ("Y".equals(isNull)) {
//			resultContent.setCode(Constants.CODE_ERR_FAIL);
//			resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
//		} else {
//			resultContent.setRetData(returnMap.get("returnStr"));
//		}
//		
//		// 记录日志
//		SystemLog systemLog = new SystemLog();
//		// uuid
//		systemLog.setUuid(uuid);
//		// ip地址
//		systemLog.setIpAddress(request.getRemoteAddr());
//		// apiKey
//		systemLog.setApiKey(apiKey);
//		// localApiID
//		systemLog.setLocalApiID(String.valueOf(localApi.get("ID")));
//		// 参数
//		JSONObject params = new JSONObject();
//		params.put("identifier", identifier);
//		systemLog.setParams(params.toJSONString());
//		// 用户ID
//		systemLog.setUserID(userID);
//		// 是否成功
//		systemLog.setIsSuccess(String.valueOf(!"Y".equals(isNull)));
//		// 是否计费
//		systemLog.setIsCount(String.valueOf(!"Y".equals(isNull)));
//		// 查询时间
//		systemLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		systemLogService.addLog(systemLog);
//		
//		return JSONObject.toJSONString(resultContent);
//	}
}
