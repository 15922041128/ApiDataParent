package org.pbccrc.api.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.DBEntity;
import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.core.dao.LocalApiDao;
import org.pbccrc.api.base.service.DBOperatorService;
import org.pbccrc.api.base.service.QueryApi;
import org.pbccrc.api.base.service.QueryApiService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class QueryApiServiceImpl implements QueryApiService{

	private QueryApi queryApi;
	
	@Autowired 
	private QueryApi queryApiSingle;
	
	@Autowired
	private QueryApi queryApiMultiple;
	
	@Autowired
	private DBOperatorService dbOperatorService;
	
	@Autowired
	private LocalApiDao localApiDao;
	
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, Object> query(String service, Map urlParams) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 返回信息对象
		ResultContent resultContent = new ResultContent();
		resultContent.setErrNum(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		String result = Constants.BLANK;
		
		// 获取service标识
		String[] serviceArray = service.split(Constants.CONNECTOR_LINE);
		// 单个或多个api标识
		String isSingle = serviceArray[0];
		// 访问服务
		String target = serviceArray[1];
		
		// 获取本地api
		Map<String, Object> localApi = localApiDao.queryByService(isSingle + Constants.CONNECTOR_LINE + target);
		
		// 本地api参数
		String localParams = String.valueOf(localApi.get("params"));
		JSONArray localParamArray = JSONArray.parseArray(localParams);
		
		// 本地api返回参数
		String[] returnParam = String.valueOf(localApi.get("returnParam")).split(Constants.COMMA);
		
		// DB操作对象
		DBEntity entity = new DBEntity();
		entity.setTableName("d_" + isSingle + Constants.UNDERLINE + target);
		List<String> fields = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		// 设置查询参数
		for (Object o : localParamArray) {
			JSONObject object = (JSONObject)o;
			String paramName = String.valueOf(object.get("paramName"));
			String paramType = String.valueOf(object.get("paramType"));
			if (Constants.PARAM_TYPE_URL.equals(paramType) 
					&& null != urlParams.get(paramName) && !StringUtil.isNull(((String[])urlParams.get(paramName))[0])) {
				fields.add(paramName);
			}
		}
		entity.setFields(fields);
		for (Object o : localParamArray) {
			JSONObject object = (JSONObject)o;
			String paramName = String.valueOf(object.get("paramName"));
			String paramType = String.valueOf(object.get("paramType"));
			if (Constants.PARAM_TYPE_URL.equals(paramType) 
					&& null != urlParams.get(paramName) && !StringUtil.isNull(((String[])urlParams.get(paramName))[0])) {
				values.add(((String[])urlParams.get(paramName))[0]);
			}
		}
		entity.setValues(values);
		
		// 查询本地api
		Map<String, Object> queryData = dbOperatorService.queryData(entity);
		
		if (null != queryData) {
			// 本地api有数据 直接返回本地数据
			if (Constants.PREFIX_SINGLE.equals(isSingle)) {
				// 唯一外部api
				String returnVal = String.valueOf(queryData.get("returnVal"));
				result = returnVal;
			} else {
				// 多个外部api
				JSONObject retJson = new JSONObject();
				for (String param : returnParam) {
					retJson.put(param, queryData.get(param));
				}
				result = retJson.toJSONString();
				resultContent.setRetData(result);
				result = resultContent.toString();
			}
			
			map.put("result", result);
			map.put("isSuccess", true);
			
		} else {
			// 本地api无数据 查询外部api
			
			// 根据前缀获取具体实现类
			if (Constants.PREFIX_SINGLE.equals(isSingle)) {
				// 唯一外部api
				queryApi = queryApiSingle;
			} else {
				// 多个外部api
				queryApi = queryApiMultiple;
			}
			
			map = queryApi.query(localApi, urlParams);
		}
		
		return map;
	}

}
