package org.pbccrc.api.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.base.service.CostService;
import org.pbccrc.api.base.service.LocalApiService;
import org.pbccrc.api.base.service.QueryApiService;
import org.pbccrc.api.base.service.SystemLogService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RemoteApiOperator;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.base.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/r/queryApi")
public class QueryApiController {

	@Autowired
	private QueryApiService queryApiService;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private LocalApiService localApiService;
	
	@Autowired
	private CostService costService;
	
	@Autowired
	private RemoteApiOperator remoteApiOperator;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@GET
	@RequestMapping(value="/get", produces={"application/json;charset=UTF-8"})
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String query(String service, HttpServletRequest request) throws Exception {
		
		Object result = Constants.BLANK;
		
		ResultContent resultContent = new ResultContent();
		
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 获得请求url参数
		Map urlParams = request.getParameterMap();

		// 验证service格式
		if (StringUtil.isNull(service) || service.split(Constants.CONNECTOR_LINE).length != 2) {
			resultContent.setCode(Constants.ERR_SERVICE);
			resultContent.setRetMsg(Constants.RET_MSG_SERVICE);
			return JSONObject.toJSONString(resultContent);
		}
		
		// 获取service标识
		String[] serviceArray = service.split(Constants.CONNECTOR_LINE);
		// 单个或多个api标识
		String isSingle = serviceArray[0];
		// 访问服务
		String target = serviceArray[1];
		
		// 获取本地api
		Map<String, Object> localApi = localApiService.queryByService(isSingle + Constants.CONNECTOR_LINE + target);
		
		// 验证本地是否有该api
		if (null == localApi) {
			resultContent.setCode(Constants.ERR_NO_SERVICE);
			resultContent.setRetMsg(Constants.RET_MSG_NO_SERVICE);
			return JSONObject.toJSONString(resultContent);
		}
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, localApi, urlParams, resultContent)) {
			return JSONObject.toJSONString(resultContent);
		}
		
		Map<String, Object> map = queryApiService.query(service, urlParams);
		result = map.get("result");
		JSONObject resultJson = (JSONObject) JSONObject.toJSON(result);
		
		// 计费
		boolean isCost = true;
		// 判断是否计费
		// 获取localApi计费条件
		String costConditionStr = String.valueOf(localApi.get("costCondition"));
		JSONObject costCondition = JSONObject.parseObject(costConditionStr);
		// 判断是否计费
		boolean isCount = costCondition.getBooleanValue("isCount");
		if (isCount) {
			// 获取计费条件
			JSONArray conditionArray = costCondition.getJSONArray("conditionArray");
			// 遍历计费条件,只有当所有条件都成立时,才会进行计费
			for (Object o : conditionArray) {
				JSONObject condition = (JSONObject)o;
				String conditionName = condition.getString("conditionName");
				String conditionValue = condition.getString("conditionValue");
				String conditionType = condition.getString("conditionType");
				
				// 获取判断用返回value值
				String resultValue = Constants.BLANK;
				String[] keyArray = conditionName.split(Constants.CONNECTOR_LINE);
				// 判断条件是否为多层
				if (keyArray.length == 1) {
					// 单层
					String key = keyArray[0];
					resultValue = resultJson.getString(key);
				} else {
					// 多层
					JSONObject jsonObject = new JSONObject();
					for (int i = 0; i < keyArray.length - 1; i++) {
						if (i == 0) {
							jsonObject = resultJson.getJSONObject(keyArray[0]);
						} else {
							jsonObject = jsonObject.getJSONObject(keyArray[i]);
						}
					}
					String key = keyArray[keyArray.length - 1];
					resultValue = jsonObject.getString(key);
				}
				
				// 判断条件类型
				if (Constants.CONDITION_TYPE_NOTNULL.equals(conditionType)) {
					// notNull类型
					if (StringUtil.isNull(resultValue)) {
						isCost = false;
						break;
					}
				} else if (Constants.CONDITION_TYPE_REGEX.equals(conditionType)) {
					// 正则类型
					Pattern pattern = Pattern.compile(conditionValue);
					Matcher matcher = pattern.matcher(resultValue);
					if (!matcher.matches()) {
						isCost = false;
						break;
					}
				} else {
					// 默认为文本类型 (文本类型判断方式为equal)
					if (!resultValue.equals(conditionValue)) {
						isCost = false;
						break;
					}
				}
			}
			if (isCost) {
				costService.cost(userID, apiKey, String.valueOf(localApi.get("ID")));	
			}
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
		String localParams = String.valueOf(localApi.get("params"));
		JSONArray localParamArray = JSONArray.parseArray(localParams);
		for (Object o : localParamArray) {
			JSONObject object = (JSONObject)o;
			String paramName = String.valueOf(object.get("paramName"));
			String paramType = String.valueOf(object.get("paramType"));
			if (Constants.PARAM_TYPE_URL.equals(paramType) 
					&& null != urlParams.get(paramName) && !StringUtil.isNull(((String[])urlParams.get(paramName))[0])) {
				params.put(paramName, ((String[])urlParams.get(paramName))[0]);
			}
		}
		systemLog.setParams(params.toJSONString());
		// 用户ID
		systemLog.setUserID(userID);
		// 是否成功
		systemLog.setIsSuccess(String.valueOf(map.get("isSuccess")));
		// 是否计费
		systemLog.setIsCount(String.valueOf(isCount));
		// 查询时间
		systemLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		systemLogService.addLog(systemLog);
		
		return JSONObject.toJSONString(result);
	}
	
	@GET
	@RequestMapping(value="/score", produces={"application/json;charset=UTF-8"})
	@Produces(MediaType.APPLICATION_JSON)
	public String score(@Context HttpServletRequest request) throws Exception {
		
		ResultContent content = new ResultContent();
		content.setCode(Constants.CODE_ERR_SUCCESS);
		content.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		String url = Constants.REMOTE_URL_SCORE;
		
		String queryString = new String(request.getQueryString().getBytes("ISO-8859-1"), "utf-8");
		
		String appKey = "&appkey=SVXcpvaHNw";
		
		String result = remoteApiOperator.remoteAccept(url + Constants.URL_PARAM_CONNECTOR + queryString + appKey);
		
		JSONObject obj = JSONObject.parseObject(result);
		String score = obj.getString("score");
		if (StringUtil.isNull(score)) {
			content.setCode(Constants.CODE_ERR_FAIL);
			content.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
		} else {
			content.setRetData(result);
		}
		
		return JSONObject.toJSONString(content);
	}
	
	@GET
	@RequestMapping(value="/querySfz", produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String querySfz(@Context HttpServletRequest request) throws Exception {
		
		String name = request.getParameter("name");
		String idCardNo = request.getParameter("idCardNo");
		
		ResultContent resultContent = new ResultContent();
		
		Object result = null;
		
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, "1", resultContent)) {
			return JSONObject.toJSONString(resultContent);
		}
		
		Map<String, Object> map = queryApiService.querySfz(name, idCardNo);
		result = map.get("result");
		JSONObject resultJson = (JSONObject) JSONObject.toJSON(result);
		
		// 判断是否计费
		if(Constants.CODE_ERR_SUCCESS.equals(resultJson.getString("code"))) {
			costService.cost(userID, apiKey, "1");
		}
		
		// 记录日志
		SystemLog systemLog = new SystemLog();
		// ip地址
		systemLog.setIpAddress(request.getRemoteAddr());
		// apiKey
		systemLog.setApiKey(apiKey);
		// localApiID
		systemLog.setLocalApiID("1");
		// 参数
		JSONObject params = new JSONObject();
		params.put("name", name);
		params.put("idCardNo", idCardNo);
		systemLog.setParams(params.toJSONString());
		// 用户ID
		systemLog.setUserID(userID);
		// 是否成功
		systemLog.setIsSuccess(String.valueOf(map.get("isSuccess")));
		// 是否计费
		systemLog.setIsCount("true");
		// 查询时间
		systemLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		systemLogService.addLog(systemLog);
		
		return JSONObject.toJSONString(result);
	}
}
