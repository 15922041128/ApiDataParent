package org.pbccrc.api.portal.controller.personalCenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;

import org.pbccrc.api.base.bean.QueryType;
import org.pbccrc.api.base.service.ProductService;
import org.pbccrc.api.base.service.QueryTypeService;
import org.pbccrc.api.base.service.RelationService;
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
			JSONArray apis = new JSONArray();
			for (Object obj : allApi) {
				JSONObject api = (JSONObject) JSONObject.toJSON(obj);
				String queryType = api.getString("queryType");
				if (key.equals(queryType)) {
					apis.add(obj);
				}
			}
			apiArray.add(apis);
		} 
		object.put("apiArray", apiArray);
		
		// relation
		JSONObject relation = relationService.getRelation(userID, productID);
		object.put("relation", relation);
		
		return object;
	}
}
