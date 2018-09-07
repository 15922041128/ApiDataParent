package org.pbccrc.api.portal.controller.personalCenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;

import org.pbccrc.api.base.adapter.JsonAdapter;
import org.pbccrc.api.base.bean.QueryType;
import org.pbccrc.api.base.service.ProductService;
import org.pbccrc.api.base.service.QueryTypeService;
import org.pbccrc.api.base.service.RelationService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.RemoteApiOperator;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.base.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/myProduct/getResult", produces={"application/json;charset=UTF-8"})
	public JSONObject getResult(String apiId, String params, HttpServletRequest request) throws Exception{
		
		// 返回对象
		JSONObject resultObject = new JSONObject();
		// 返回数据集合
		JSONArray resultArray = new JSONArray();
		// 返回提示信息
		String resultMessage = Constants.BLANK;
		
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		// 查询参数
		Map<String, String> paramMap = JSON.parseObject(params, Map.class);
		
		// 获取ip
		String ipAddress = SystemUtil.getIpAddress(request);
		
		boolean hasData = false;
		// 从缓存中读取localApi
		JSONObject localApi = JSONObject.parseObject(String.valueOf(RedisClient.get("localApi_" + apiId)));
		// 获取service
		String service = localApi.getString("service");
		String url = Constants.WEB_URL + String.valueOf(localApi.get("url"));
		JSONObject returnJson = null;
		returnJson = JSONObject.parseObject(remoteApiOperator.insideAccess(userID, apiKey, url, service, ipAddress, paramMap));
		// 返回参数英文转中文
		String retData = returnJson.getString("retData");
		
		if (StringUtil.isNull(retData)) {
			returnJson.put("retData", new JSONArray());
			resultMessage = returnJson.getString("retMsg");
		} else {
			hasData = true;
			retData = jsonAdapter.change2Ch(service, retData);
			returnJson.put("retData", JSONObject.parse(retData));
		}
		
		// 返回类型
		String queryType = String.valueOf(localApi.get("returnType"));
		returnJson.put("returnType", queryType);
		
		// table名称
		String apiName = String.valueOf(localApi.get("apiName"));
		returnJson.put("tableName", apiName);
		
		resultArray.add(returnJson);
		if (!hasData) {
			resultObject.put("resultMessage", resultMessage);
		}
		resultObject.put("resultArray", resultArray);
		
		return resultObject;
	}
}
