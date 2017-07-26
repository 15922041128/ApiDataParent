package org.pbccrc.api.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.pbccrc.api.base.bean.LocalApi;
import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.base.service.CostService;
import org.pbccrc.api.base.service.LocalApiService;
import org.pbccrc.api.base.service.LocalDBService;
import org.pbccrc.api.base.service.SystemLogService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.base.util.SystemUtil;
import org.pbccrc.api.base.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/r/ldb")
public class LocalDBController {
	
	@Autowired
	private LocalDBService localDBService;
	
	@Autowired
	private LocalApiService localApiService;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private CostService costService;
	
	@Autowired
	private SystemLogService systemLogService;

	/**
	 * 根据身份证和姓名查询信贷信息
	 * @param name
	 * @param idCardNo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@ResponseBody
	@RequestMapping("/get")
	public String query(@QueryParam("name") String name, @QueryParam("idCardNo") String idCardNo, 
			@Context HttpServletRequest request) throws Exception {
		
		request.setCharacterEncoding("utf-8");
		
		name = request.getParameter("name");
		
		String result = Constants.BLANK;
		
		result = localDBService.query(name, idCardNo);
		
		return result;
	}
	
	/**
	 * 失信人查询(PDF用)
	 * @param idCardNo
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getSxr", produces={"application/json;charset=UTF-8"})
	public JSONObject getSxr(@QueryParam("name") String name, @QueryParam("identifier") String identifier) throws Exception {
		
		JSONObject object = new JSONObject();
		object.put("errNum", Constants.CODE_ERR_SUCCESS);
		object.put("retMsg", Constants.CODE_ERR_SUCCESS_MSG);
		object.put("retData", Constants.BLANK);
		
		List<Map<String, Object>> dishonestList = localDBService.getSxr(name, identifier);
		
		if (null == dishonestList || dishonestList.size() == 0) {
			object.put("errNum", Constants.ERR_NO_RESULT);
			object.put("retMsg", Constants.RET_MSG_NO_RESULT);
		}
		
		object.put("retData", dishonestList);
		
		return object;
	}
	
	@GET
	@ResponseBody
	@RequestMapping("/query")
	public JSONObject queryApiByInsideCode(@QueryParam("service") String service, @QueryParam("name") String name, @QueryParam("idCardNo") String idCardNo) throws Exception {
		
		Map<String, Object> resultMap = localDBService.queryApi(service, name, idCardNo);
		
		JSONObject result = new JSONObject();
		
		// 判断内码中是否存在被查询用户
		String isNull = "N";
		isNull = String.valueOf(resultMap.get("isNull"));
		if ("Y".equals(isNull)) {
			result.put("isNull", isNull);
			return result;
		}
		
		result.put("isNull", isNull);
		result.put("result", resultMap.get("result"));
		
		return result;
	}
	
	/**
	 * 综合查询本地数据
	 * @param name			姓名
	 * @param identifier	身份证号码
	 * @param telNum		电话号码
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getResult", produces={"application/json;charset=UTF-8"})
	public JSONObject getResult(String service, String name, String identifier, String telNum, String ipAddress, HttpServletRequest request) throws Exception {
		
		if (StringUtil.isNull(ipAddress)) {
			ipAddress = SystemUtil.getIpAddress(request);
		}
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		resultContent.setRetData(Constants.BLANK);
		
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 验证service格式
		if (StringUtil.isNull(service)) {
			resultContent.setCode(Constants.ERR_SERVICE);
			resultContent.setRetMsg(Constants.RET_MSG_SERVICE);
			return (JSONObject) JSONObject.toJSON(resultContent);
		}
		
		// 获取本地api
		LocalApi localApi = localApiService.queryByService(service);
		
		// 验证本地是否有该api
		if (null == localApi) {
			resultContent.setCode(Constants.ERR_NO_SERVICE);
			resultContent.setRetMsg(Constants.RET_MSG_NO_SERVICE);
			return (JSONObject) JSONObject.toJSON(resultContent);
		}
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, localApi, name, identifier, telNum, ipAddress, resultContent)) {
			return (JSONObject) JSONObject.toJSON(resultContent);
		}
		
		// 内码
		String innerID = Constants.BLANK;
		// 查询参数(记录日志用)
		JSONObject paramObj = new JSONObject();
		// 判断查询方式
		String params = localApi.getParams();
		JSONArray array = JSONArray.parseArray(params);
		if (array.size() == 1) {
			// size为1 则通过电话号码查询内码
			innerID = localDBService.getInnerIDByTelNum(telNum);
			paramObj.put("telNum", telNum);
		} else {
			// size为2 则通过两标查询内码
			innerID = localDBService.getInnerID(name, identifier);
			paramObj.put("name", name);
			paramObj.put("identifier", identifier);
		}
		
		// 生成UUID
		String uuid = StringUtil.createUUID();
		
		Map<String, Object> map = localDBService.getResult(uuid, userID, service, innerID, paramObj);
		
		// 是否成功
		boolean isSuccess = (boolean) map.get("isSuccess");
		
		// 判断是否成功
		if (isSuccess) {
			// 计费
			Map<String, Object> costRetMap = costService.cost(userID, apiKey);
			String queryCount = String.valueOf(costRetMap.get("queryCount"));
			// 查询次数
			resultContent.setQueryCount(queryCount);
			// 根据返回类型获取查询结果
			Object result = map.get("result");
			String returnType = String.valueOf(localApi.getReturnType());
			Object resultJson = null;
			if (Constants.RETURN_TYPE_ARRAY.equals(returnType)) {
				resultJson = JSONArray.toJSON(result);
			} else {
				resultJson = JSONObject.toJSON(result);
			}
			// 返回数据
			resultContent.setRetData(resultJson);
			
		} else {
			resultContent.setCode(Constants.ERR_NO_RESULT);
			resultContent.setRetMsg(Constants.RET_MSG_NO_RESULT);
		}
		
		// 记录日志
		SystemLog systemLog = new SystemLog();
		// uuid
		systemLog.setUuid(uuid);
		// ip地址
		systemLog.setIpAddress(ipAddress);
		// apiKey
		systemLog.setApiKey(apiKey);
		// 产品ID
		// 从缓存中获取relation对象
		JSONObject relation = JSONObject.parseObject(String.valueOf(RedisClient.get("relation_" + userID + Constants.UNDERLINE + apiKey)));
		systemLog.setProductID(relation.getString("productID"));
		// localApiID
		systemLog.setLocalApiID(String.valueOf(localApi.getId()));
		// 参数
		systemLog.setParams(paramObj.toJSONString());
		// 用户ID
		systemLog.setUserID(userID);
		// 是否成功
		systemLog.setIsSuccess(String.valueOf(isSuccess));
		// 是否计费
		systemLog.setIsCount(String.valueOf(isSuccess));
		// 查询时间
		systemLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FORMAT_SYSTEMLOG).format(new Date()));
		systemLogService.addLog(systemLog);
		
		return (JSONObject) JSONObject.toJSON(resultContent);
		
	}

}
