package org.pbccrc.api.portal.controller.personalCenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;

import org.pbccrc.api.base.adapter.JsonAdapter;
import org.pbccrc.api.base.bean.QueryType;
import org.pbccrc.api.base.service.LocalApiService;
import org.pbccrc.api.base.service.ProductService;
import org.pbccrc.api.base.service.QueryTypeService;
import org.pbccrc.api.base.service.RelationService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RemoteApiOperator;
import org.pbccrc.api.base.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
public class MyProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private RelationService relationService;
	
	@Autowired
	private QueryTypeService queryTypeService;
	
	@Autowired
	private LocalApiService localApiService;
	
	@Autowired
	private RemoteApiOperator remoteApiOperator;
	
	@Autowired
	private JsonAdapter jsonAdapter;
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/getProductApiArray", produces={"application/json;charset=UTF-8"})
	public JSONObject getMyProduct(String userID, String productID) {
		
		// 返回json对象
		JSONObject object = new JSONObject();
		
		// 根据产品ID查询所有api
		JSONArray allApi = productService.getApiArray(productID);
		
		// 获取所有查询方式
		List<QueryType> queryTypeList = queryTypeService.queryAll();
		// 获取该产品中有几种查询方式并保存到queryTypeMap中
		Map<String, String> queryTypeMap = new HashMap<String, String>();
		for (Object obj : allApi) {
			JSONObject api = (JSONObject) JSONObject.toJSON(obj);
			String queryType = api.getString("queryType");
			for (int i = 0; i < queryTypeList.size(); i++) {
				QueryType queryTypeBean = queryTypeList.get(i);
				if (queryType.equals(String.valueOf(queryTypeBean.getTypeCode()))) {
					queryTypeMap.put(String.valueOf(queryTypeBean.getTypeCode()), queryTypeBean.getTypeName());
				}
			}
		}
		// 将查询方式转为json对象
		JSONArray queryTypeArray = new JSONArray();
		for (Map.Entry<String, String> entry : queryTypeMap.entrySet()) {
			JSONObject obj = new JSONObject();
			obj.put("typeCode", entry.getKey());
			obj.put("typeName", entry.getValue());
			queryTypeArray.add(obj);
		} 
		object.put("queryTypeArray", queryTypeArray);
		
		// 根据queryType为api分组
		JSONArray apiArray = new JSONArray();
		for (String key : queryTypeMap.keySet()) {  
			JSONObject apisInfo = new JSONObject();
			JSONArray apis = new JSONArray();
			for (Object obj : allApi) {
				JSONObject api = (JSONObject) JSONObject.toJSON(obj);
				String queryType = api.getString("queryType");
				if (key.equals(queryType)) {
					apis.add(obj);
				}
			}
			apisInfo.put("array", apis);
			apisInfo.put("queryType", key);
			apiArray.add(apisInfo);
		}
		object.put("apiArray", apiArray);
		
		// relation
		JSONObject relation = relationService.getRelation(userID, productID);
		object.put("relation", relation);
		
		return object;
	}
	
	@GET
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/myProduct/getResult", produces={"application/json;charset=UTF-8"})
	public JSONArray getResult(String serviceStr, HttpServletRequest request) throws Exception{
		
		JSONArray resultArray = new JSONArray();
		
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 获取参数
		// 两标
		String name = request.getParameter("name");
		String identifier = request.getParameter("identifier");
		// 电话号码
		String telNum = request.getParameter("telNum");
		
		
		// 查询参数(记录日志用)
		// 查询参数(查询用)
		Map<String, String> paramMap = new HashMap<String, String>();
		// 判断查询方式
		if (!StringUtil.isNull(telNum)) {
			paramMap.put("telNum", telNum);
		} else {
			paramMap.put("name", name);
			paramMap.put("identifier", identifier);
		}
		
		// 遍历service
		String[] services = serviceStr.split(Constants.COMMA);
		for (String service : services) {
			Map<String, Object> localApi = localApiService.queryByService(service);
			String url = String.valueOf(localApi.get("url"));
			JSONObject returnJson = null;
			returnJson = JSONObject.parseObject(remoteApiOperator.insideAccess(userID, apiKey, url, service, paramMap));
			// 返回参数英文转中文
			String retData = returnJson.getString("retData");
			retData = jsonAdapter.change2Ch(service, retData);
			returnJson.put("retData", retData);
			// 返回类型
			returnJson.put("returnType", String.valueOf(localApi.get("returnType")));
			
			resultArray.add(returnJson);
		}
		
		return resultArray;
	}
}
