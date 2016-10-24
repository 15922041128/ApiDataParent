package org.pbccrc.api.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.pbccrc.api.base.service.LocalDBService;
import org.pbccrc.api.base.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/r/ldb")
public class LocalDBController {
	
	@Autowired
	private LocalDBService localDBService;

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
	 * 失信人查询
	 * @param idCardNo
	 * @return
	 * @throws Exception
	 */
	@GET
	@ResponseBody
	@RequestMapping("/getSxr")
	public JSONObject getSxr(@QueryParam("idCardNo") String idCardNo) throws Exception {
		
		JSONObject object = new JSONObject();
		object.put("errNum", Constants.CODE_ERR_SUCCESS);
		object.put("retMsg", Constants.CODE_ERR_SUCCESS_MSG);
		
		List<Map<String, Object>> dishonestList = localDBService.getSxr(idCardNo);
		
		if (null == dishonestList || dishonestList.size() == 0) {
			object.put("errNum", Constants.ERR_NO_RESULT);
			object.put("retMsg", Constants.RET_MSG_NO_RESULT);
			return object;
		}
		
		object.put("retData", dishonestList);
		
		return object;
	}
	
	@GET
	@ResponseBody
	@RequestMapping("/query")
	public JSONObject queryApiByInsideCode(@QueryParam("service") String service, @QueryParam("idCardNo") String idCardNo) throws Exception {
		
		Map<String, Object> resultMap = localDBService.queryApi(service, idCardNo);
		
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

}
