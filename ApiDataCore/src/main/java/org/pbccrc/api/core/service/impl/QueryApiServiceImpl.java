package org.pbccrc.api.core.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.bean.DBEntity;
import org.pbccrc.api.base.bean.ResultContent;
import org.pbccrc.api.core.dao.LocalApiDao;
import org.pbccrc.api.base.service.ApiLogService;
import org.pbccrc.api.base.service.DBOperatorService;
import org.pbccrc.api.base.service.QueryApi;
import org.pbccrc.api.base.service.QueryApiService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RemoteApiOperator;
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
	
	@Autowired
	private RemoteApiOperator remoteApiOperator;
	
	@Autowired
	private ApiLogService apiLogService;
	
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, Object> query(String uuid, String userID, String service, Map urlParams) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		//  数据来源,记录日志用
		String dataFrom = Constants.DATA_FROM_LOCAL;
		
		// 返回信息对象
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		
		Object result = null;
		
		// 获取service标识
		String[] serviceArray = service.split(Constants.CONNECTOR_LINE);
		// 单个或多个api标识
		String isSingle = serviceArray[0];
		// 访问服务
		String target = serviceArray[1];
		
		// 获取本地api
		Map<String, Object> localApi = localApiDao.queryByService(service);
		
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
				resultContent.setRetData(retJson);
				result = resultContent;
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
			
			// 获取数据来源
			dataFrom = String.valueOf(map.get("dataFrom"));
		}
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userID);
		apiLog.setLocalApiID(String.valueOf(localApi.get("ID")));
		// 参数
		JSONObject params = new JSONObject();
		for (Object o : localParamArray) {
			JSONObject object = (JSONObject)o;
			String paramName = String.valueOf(object.get("paramName"));
			String paramType = String.valueOf(object.get("paramType"));
			if (Constants.PARAM_TYPE_URL.equals(paramType) 
					&& null != urlParams.get(paramName) && !StringUtil.isNull(((String[])urlParams.get(paramName))[0])) {
				params.put(paramName, ((String[])urlParams.get(paramName))[0]);
			}
		}
		apiLog.setParams(params.toJSONString());
		apiLog.setDataFrom(dataFrom);
		apiLog.setIsSuccess(String.valueOf(map.get("isSuccess")));
		apiLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		apiLogService.addLog(apiLog);
		
		return map;
	}
	
	public Map<String, Object> querySfz(String uuid, String userID, String name, String idCardNo) throws Exception {
		
		// 返回map
		Map<String, Object> map = new HashMap<String, Object>();
		// 返回信息对象
		ResultContent resultContent = new ResultContent();
		resultContent.setCode(Constants.CODE_ERR_SUCCESS);
		resultContent.setRetMsg(Constants.CODE_ERR_SUCCESS_MSG);
		// 返回信息
		JSONObject resultJson = new JSONObject();
		resultJson.put("ret_name", name);
		resultJson.put("ret_idCardNo", idCardNo);
		
		//  数据来源,记录日志用
		String dataFrom = Constants.DATA_FROM_LOCAL;
		
		// localApiID
		String localApiID = "1";
		// returnType
		String returnType = "2";
		// tableName
		String tableName = "d_m_sfzxx";
		
		// 初始化查询数据
		DBEntity entity = new DBEntity();
		entity.setTableName(tableName);
		entity.setSelectItems(new String[]{"ret_idCardNo", "ret_name", "ret_status"});
		entity.setSql(" and idCardNo = " + idCardNo);
		
		// 查询本地数据库
		Map<String, Object> queryData = dbOperatorService.queryBySql(entity);
		// 判断本地是否有数据
		if (null != queryData) {
			// 本地有数据
			JSONObject result = new JSONObject();
			result.put("ret_idCardNo", queryData.get("ret_idCardNo"));
			result.put("ret_name", queryData.get("ret_name"));
			String ret_name = String.valueOf(queryData.get("ret_name"));
			if (ret_name.equals(name)) {
				result.put("ret_status", "一致");
			} else {
				result.put("ret_status", "不一致");
			}
			resultContent.setRetData(result);
		} else {
			// 本地无数据,查询远程
			
			// 数据来源设置为qilingyz
			dataFrom = Constants.DATA_FROM_QILINGYZ;
			
			// 查询qilingyz
			String appkey = "jdwx991230";
			StringBuffer urlBuff = new StringBuffer("http://www.qilingyz.com/api.php?m=open.queryStar");
			urlBuff.append(Constants.URL_PARAM_CONNECTOR + "appkey=" + appkey);
			urlBuff.append(Constants.URL_PARAM_CONNECTOR + "identityCard=" + URLEncoder.encode(idCardNo, "utf-8"));
			urlBuff.append(Constants.URL_PARAM_CONNECTOR + "fullName=" + URLEncoder.encode(name, "utf-8"));
			String returnStr = remoteApiOperator.remoteAccept(urlBuff.toString());
			// 判断返回字符串是否为空
			if (!StringUtil.isNull(returnStr)) {
				// 如果返回字符串不为空,则插入数据库并返回
				// 解析返回数据
				JSONObject returnJson = JSONObject.parseObject(returnStr);
				String status = "一致";
				status = returnJson.getString("status");
				if ("一致".equals(status)) {
					insertDB(tableName, localApiID, returnType, name, idCardNo, status);
				}
				resultJson.put("ret_status", status);
				resultContent.setRetData(resultJson);
				map.put("result", resultContent);
				map.put("isSuccess", true);
			} else {
				// 返回字符串为空,则查询全联
				// 数据来源设置为qilingyz
				dataFrom = Constants.DATA_FROM_QL;
				String encryptKey = "B0779D235C3D0D2FB9C898C946AD3B98";
				appkey = "B9C898C946AD3B9";
				urlBuff = new StringBuffer("http://www.uniocredit.com/nuapi/UService.do?service=ucqiis");
				urlBuff.append(Constants.URL_PARAM_CONNECTOR + "appid=" + appkey);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("NAME", name);
				paramMap.put("IDENTITYCARD", idCardNo);
				returnStr = remoteApiOperator.qlRemoteAccept(encryptKey, urlBuff.toString(), paramMap);
				// 判断返回字符串是否为空
				if (StringUtil.isNull(returnStr)) {
					// 返回字符串为空,则返回查询失败信息
					resultContent.setCode(Constants.CODE_ERR_FAIL);
					resultContent.setRetMsg(Constants.CODE_ERR_FAIL_MSG);
					map.put("result", resultContent);
					map.put("isSuccess", false);
				} else {
					// 如果返回字符串不为空,则插入数据库并返回
					// 解析返回数据
					JSONObject returnJson = JSONObject.parseObject(returnStr);
					String status = "一致";
					status = returnJson.getJSONObject("Result").getString("COMPRESULT");
					resultJson.put("ret_status", status);
					resultContent.setRetData(resultJson);
					if ("一致".equals(status)) {
						insertDB(tableName, localApiID, returnType, name, idCardNo, status);
					}
				}
			}
		}
		
		map.put("result", resultContent);
		map.put("isSuccess", true);
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(uuid);
		apiLog.setUserID(userID);
		apiLog.setLocalApiID(Constants.API_ID_SFZRZ);
		// 参数
		JSONObject params = new JSONObject();
		params.put("name", name);
		params.put("idCardNo", idCardNo);
		apiLog.setParams(params.toJSONString());
		apiLog.setDataFrom(dataFrom);
		apiLog.setIsSuccess(String.valueOf(map.get("isSuccess")));
		apiLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		apiLogService.addLog(apiLog);
		
		return map;
	}

	private void insertDB(String tableName, String localApiID, String returnType, String name, String idCardNo, String status) {
		
		DBEntity entity = new DBEntity();
		entity.setTableName(tableName);
		List<String> fields = new ArrayList<String>();
		fields.add("localApiID");
		fields.add("returnTyp");
		fields.add("name");
		fields.add("idCardNo");
		fields.add("ret_name");
		fields.add("ret_idCardNo");
		fields.add("ret_status");
		fields.add("returnCode");
		entity.setFields(fields);
		List<String> values = new ArrayList<String>();
		values.add(localApiID);
		values.add(returnType);
		values.add(name);
		values.add(idCardNo);
		values.add(name);
		values.add(idCardNo);
		values.add(status);
		values.add(Constants.CODE_ERR_SUCCESS);
		entity.setValues(values);
		dbOperatorService.insertData(entity);
	}
}
