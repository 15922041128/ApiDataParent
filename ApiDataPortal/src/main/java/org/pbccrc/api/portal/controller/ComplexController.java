package org.pbccrc.api.portal.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;

import org.pbccrc.api.base.bean.SystemLog;
import org.pbccrc.api.base.service.ComplexService;
import org.pbccrc.api.base.service.SystemLogService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.base.util.SystemUtil;
import org.pbccrc.api.base.util.pdf.PdfBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
public class ComplexController {
	
	@Autowired
	private ComplexService complexService;
	
	@Autowired
	private PdfBuilder pdfBuilder;
	
	@Autowired
	private SystemLogService systemLogService;
	
	/**
	 * 失信人查询验证
	 * @param identifier
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/complex/validateSxr", produces={"application/json;charset=UTF-8"})
	public JSONObject validateSxr(String name, String identifier) throws Exception{
		
		JSONObject object = new JSONObject();
		String isSuccess = "N";
		
		isSuccess = complexService.validateSxr(name, identifier);
		
		object.put("isSuccess", isSuccess);
		return object;
	}
	
	/**
	 * 失信人查询(生成pdf)
	 * @param identifier
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/complex/sxr", produces={"application/json;charset=UTF-8"})
	public JSONObject querySxr(String userID, String name, String identifier, String queryItem, HttpServletRequest request) throws Exception{
		
		JSONObject object = new JSONObject();
		
		String[] queryItems = queryItem.split(Constants.COMMA);
		
		String bathPath = Constants.FILE_DOWNLOAD_SXR_PDF;
		
		// 生成UUID
		String uuid = StringUtil.createUUID();
		
		Map<String, Object> queryResult = complexService.querySxr(uuid, userID, name, identifier, queryItems);
		
 		// 获取是否成功
 		String isNull = String.valueOf(queryResult.get("isNull"));
 		object.put("isNull", isNull);
 		
 		String filePath = pdfBuilder.getPDF(JSON.toJSONString(queryResult), queryItems, bathPath, request);
 		object.put("filePath", Constants.PDF_FILE_PATH + bathPath + File.separator + filePath);
 		
        // 记录日志
 		SystemLog systemLog = new SystemLog();
 		// uuid
 		systemLog.setUuid(uuid);
 		// ip地址
 		systemLog.setIpAddress(SystemUtil.getIpAddress(request));
 		// apiKey
 		systemLog.setApiKey(Constants.BLANK);
 		// 产品ID
 		systemLog.setProductID(Constants.PRODUCT_ID_PAGE_PDF);
 		// localApiID
 		systemLog.setLocalApiID(Constants.API_ID_PAGE_PDF);
 		// 参数
 		JSONObject params = new JSONObject();
 		params.put("identifier", identifier);
 		params.put("queryItems", queryItems);
 		systemLog.setParams(params.toJSONString());
 		// 用户ID
 		systemLog.setUserID(userID);
 		// 是否成功
 		systemLog.setIsSuccess(String.valueOf(!"Y".equals(isNull)));
 		// 是否计费
 		systemLog.setIsCount(String.valueOf(!"Y".equals(isNull)));
 		// 查询时间
 		systemLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FORMAT_SYSTEMLOG).format(new Date()));
 		systemLogService.addLog(systemLog);
		
		return object;
	}

}
