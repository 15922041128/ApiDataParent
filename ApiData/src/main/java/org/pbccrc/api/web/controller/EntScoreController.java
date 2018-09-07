package org.pbccrc.api.web.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;

import org.pbccrc.api.base.bean.LocalApi;
import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.base.service.CostService;
import org.pbccrc.api.base.service.LocalApiService;
import org.pbccrc.api.base.service.SystemLogService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.DesUtils;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.SystemUtil;
import org.pbccrc.api.base.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/entScore")
public class EntScoreController {
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private CostService costService;
	
	@Autowired
	private LocalApiService localApiService;
	
	/** 
	 * 企业评分
	 * @param requestStr
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getScore", produces={"application/json;charset=UTF-8"})
	public JSONObject getResult(String requestStr, HttpServletRequest request) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		// 获取IP地址
		String ipAddress = SystemUtil.getIpAddress(request);
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		requestStr = DesUtils.Base64Decode(URLDecoder.decode(requestStr));
		
		JSONObject json = null;
		// 验证json格式
		try {
			json = JSONObject.parseObject(requestStr);
		} catch (Exception e) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_FORMAT);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_FORMAT_MSG);
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 获取本地api
		LocalApi localApi = localApiService.queryByService(Constants.API_SERVICE_ENT_SCORE);
		
		// 请求参数验证
		if (!validator.validateRequest(userID, apiKey, localApi, json, ipAddress, resultContent)) {
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 获取type
		String type = json.getString("type");
		
		// 获取ls,并进行数字判断
		String lsStr = json.getString("ls");
		if (!(isDecimal(lsStr) || isNumber(lsStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "ls");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double ls = Double.parseDouble(lsStr);
		
		// 获取ld,并进行数字判断
		String ldStr = json.getString("ld");
		if (!(isDecimal(ldStr) || isNumber(ldStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "ld");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double ld = Double.parseDouble(ldStr);
		
		if (ld == 0) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER_ZERO);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_ZERO_MSG + "ld");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 获取li,并进行数字判断
		String liStr = json.getString("li");
		if (!(isDecimal(liStr) || isNumber(liStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "li");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double li = Double.parseDouble(liStr);
		
		// 获取sa,并进行数字判断
		String saStr = json.getString("sa");
		if (!(isDecimal(saStr) || isNumber(saStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "sa");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double sa = Double.parseDouble(saStr);
		
		// 获取re,并进行数字判断
		String reStr = json.getString("re");
		if (!(isDecimal(reStr) || isNumber(reStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "re");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double re = Double.parseDouble(reStr);
		
		// 获取no,并进行数字判断
		String noStr = json.getString("no");
		if (!(isDecimal(noStr) || isNumber(noStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "no");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double no = Double.parseDouble(noStr);
		
		// 获取pr,并进行数字判断
		String prStr = json.getString("pr");
		if (!(isDecimal(prStr) || isNumber(prStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "pr");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double pr = Double.parseDouble(prStr);
		
		// 获取de,并进行数字判断
		String deStr = json.getString("de");
		if (!(isDecimal(deStr) || isNumber(deStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "de");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double de = Double.parseDouble(deStr);
		
		// 获取am,并进行数字判断
		String amStr = json.getString("am");
		if (!(isDecimal(amStr) || isNumber(amStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "am");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double am = Double.parseDouble(amStr);
		
		// 获取fi,并进行数字判断
		String fiStr = json.getString("fi");
		if (!(isDecimal(fiStr) || isNumber(fiStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "fi");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double fi = Double.parseDouble(fiStr);
		
		// 获取tr,并进行数字判断
		String trStr = json.getString("tr");
		if (!(isDecimal(trStr) || isNumber(trStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "tr");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double tr = Double.parseDouble(trStr);
		
		// 获取di,并进行数字判断
		String diStr = json.getString("di");
		if (!(isDecimal(diStr) || isNumber(diStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "di");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double di = Double.parseDouble(diStr);
		
		// 获取ac,并进行数字判断
		String acStr = json.getString("ac");
		if (!(isDecimal(acStr) || isNumber(acStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "ac");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double ac = Double.parseDouble(acStr);
		
		// 获取pa,并进行数字判断
		String paStr = json.getString("pa");
		if (!(isDecimal(paStr) || isNumber(paStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "pa");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double pa = Double.parseDouble(paStr);
		
		// 获取bo,并进行数字判断
		String boStr = json.getString("bo");
		if (!(isDecimal(boStr) || isNumber(boStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "bo");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double bo = Double.parseDouble(boStr);
		
		// 获取ts,并进行数字判断
		String tsStr = json.getString("ts");
		if (!(isDecimal(tsStr) || isNumber(tsStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "ts");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double ts = Double.parseDouble(tsStr);
		
		if (ts == 0) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER_ZERO);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_ZERO_MSG + "ts");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 获取lo,并进行数字判断
		String loStr = json.getString("lo");
		if (!(isDecimal(loStr) || isNumber(loStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "lo");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double lo = Double.parseDouble(loStr);
		
		// 获取la,并进行数字判断
		String laStr = json.getString("la");
		if (!(isDecimal(laStr) || isNumber(laStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "la");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double la = Double.parseDouble(laStr);
		
		if (la == 0) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER_ZERO);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_ZERO_MSG + "la");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		// 获取ow,并进行数字判断
		String owStr = json.getString("ow");
		if (!(isDecimal(owStr) || isNumber(owStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "ow");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double ow = Double.parseDouble(owStr);
		
		// 获取aa,并进行数字判断
		String aaStr = json.getString("aa");
		if (!(isDecimal(aaStr) || isNumber(aaStr))) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_MSG + "aa");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		double aa = Double.parseDouble(aaStr);
		
		if ((fi + bo) == 0) {
			resultContent.setCode(Constants.CODE_ERR_PARAM_NUMBER_ZERO);
			resultContent.setRetMsg(Constants.CODE_ERR_PARAM_NUMBER_ZERO_MSG + "fi + bo");
			return (JSONObject)JSONObject.toJSON(resultContent);
		}
		
		JSONObject returnJson = new JSONObject();
		String retMsg = Constants.CODE_ERR_SUCCESS_MSG;
		String code = Constants.CODE_ERR_SUCCESS;
		
		double score = 0;
		
		switch (type) {
			case "gt":
				score += calcValue(5 * (ls / ld -1) / (1.5 - 1) * 2, 10);
				score += calcValue(5 * ((ls - li) / ld - 0.5) / (1 - 0.5), 5);
				score += calcValue(5 * (sa / (re + no) - 2) / (8 - 2) * 2, 10);
				score += calcValue(5 * ((pr + de + am + fi - (tr + di - ac - pa)) / (fi + bo) - 1) / (1.5 - 1), 5);
				score += calcValue(5 * (((pr - fi) / ts) - 0.02) / (0.07 - 0.02) * 2, 10);
				score += calcValue(5 * ((lo / la) - 0.8) / (1 - 0.8), 5);
				score += calcValue(5 * (ow / aa - 0.85) / (0.65 - 0.85), 5);
				score *= 2;
				break;
			case "zz":
				score += calcValue(5 * (ls / ld - 1) / (1.5 - 1) * 2, 10);
				score += calcValue(5 * ((ls - li) / ld -0.5) / (1 - 0.5), 5);
				score += calcValue(5 * (sa / (re + no) - 1) / (8 - 1) * 2, 10);
				score += calcValue(5 * ((pr + de + am + fi - (tr + di - ac - pa)) / (fi + bo) - 1) / (1.5 - 1), 5);
				score += calcValue(5 * (((pr - fi) / ts) - 0.02) / (0.07 - 0.02) * 2, 10);
				score += calcValue(5 * ((lo / la) - 0.8) / (1 - 0.8), 5);
				score += calcValue(5 * (ow / aa - 0.85) / (0.65 - 0.85), 5);
				score *= 2;
				break;
			case "dc":
				score += calcValue(5 * (ls / ld -1) / (1.5 - 1) * 2, 10);
				score += calcValue(5 * ((ls - li) / ld - 0.5) / (1 - 0.5), 5);
				score += calcValue(5 * (sa / (re + no) - 0.1) / (0.8 - 0.1) * 2, 10);
				score += calcValue(5 * ((pr + de + am + fi - (tr + di - ac - pa)) / (fi + bo) -1) / (1.5 - 1), 5);
				score += calcValue(5 * (((pr - fi) / ts) * - 0.02) / (0.08 - 0.02) * 2, 10);
				score += calcValue(5* ((lo / la) - 0.8) / (1 - 0.8), 5);
				score += calcValue(5* (ow / aa - 0.9) / (0.7 - 0.9), 5);
				score *= 2;
				break;
			default:
				retMsg = Constants.CODE_ERR_ENT_SOCE_FAIL;
				code = Constants.CODE_ERR_ENT_SOCE_FAIL_MSG;
		}
		
		long retScore = Math.round(score);
		
		returnJson.put("retMsg", retMsg);
		returnJson.put("code", code);
		JSONObject retData = new JSONObject();
		retData.put("score", retScore);
		returnJson.put("retData", retData);
		
		boolean isSuccess = score > 0;
		isSuccess = true;
		
		if (isSuccess) {
			costService.cost(userID, apiKey);
		}
		
		long endTime = System.currentTimeMillis();
		
		// 记录日志
		SystemLog systemLog = new SystemLog();
		// uuid为空
		systemLog.setUuid("");
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
		JSONObject paramJson = new JSONObject();
		paramJson.put("type", type);
		paramJson.put("ls", ls);
		paramJson.put("ld", ld);
		paramJson.put("li", li);
		paramJson.put("sa", sa);
		paramJson.put("re", re);
		paramJson.put("no", no);
		paramJson.put("pr", pr);
		paramJson.put("de", de);
		paramJson.put("am", am);
		paramJson.put("fi", fi);
		paramJson.put("tr", tr);
		paramJson.put("di", di);
		paramJson.put("ac", ac);
		paramJson.put("pa", pa);
		paramJson.put("bo", bo);
		paramJson.put("ts", ts);
		paramJson.put("lo", lo);
		paramJson.put("la", la);
		paramJson.put("ow", ow);
		paramJson.put("aa", aa);
		systemLog.setParams(paramJson.toJSONString());
		// 用户ID
		systemLog.setUserID(userID);
		// 是否成功
		systemLog.setIsSuccess(String.valueOf(isSuccess));
		// 是否计费
		systemLog.setIsCount(String.valueOf(isSuccess));
		// 查询时间
		systemLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FORMAT_SYSTEMLOG).format(new Date()));
		// 查询用时
		systemLog.setQueryTime(endTime - startTime);
		// 返回数据
		if (score == -90) {
			systemLog.setReturnData(returnJson.getString("errorMessage"));
			retData.put("score", Constants.BLANK);
			returnJson.put("retData", retData);
		} else {
			systemLog.setReturnData(String.valueOf(score));
		}
		systemLogService.addLog(systemLog);
		
		return returnJson;
	}
	
	private static boolean isMatch(String regex, String orginal){  
	    if (orginal == null || orginal.trim().equals("")) {  
	        return false;  
	    }  
	    Pattern pattern = Pattern.compile(regex);  
	    Matcher isNum = pattern.matcher(orginal);  
	    return isNum.matches();  
	}  
	
	public static boolean isDecimal(String orginal){  
        return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);  
    }
	
	public static boolean isNumber(String str) {
		
		for (int i = 0; i < str.length(); i++) {  
            if (Character.isDigit(str.charAt(i)) == false) {  
                return false;  
            }  
        }  
		
		return true;
	}
	
	public double calcValue(double score, double value) {
		
		if (score < 0) {
			return 0;
		}
		
		if (score > value) {
			score = value;
		}
		return score;
	}
}
