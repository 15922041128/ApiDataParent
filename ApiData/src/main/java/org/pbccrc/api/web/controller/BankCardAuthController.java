package org.pbccrc.api.web.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.pbccrc.api.base.bean.ApiLog;
import org.pbccrc.api.base.service.ApiLogService;
import org.pbccrc.api.base.service.BankCardAuthService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.DesUtils;
import org.pbccrc.api.base.util.HttpUtil;
import org.pbccrc.api.base.util.RedisClient;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.base.util.rsa.RsaCodingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/bankCardAuth")
public class BankCardAuthController{
	
	@Autowired
	private BankCardAuthService bankCardAuthService;
	
	@Autowired
	private ApiLogService apiLogService;
	
	@GET
	@ResponseBody
	@RequestMapping(value="/auth",produces={"application/json;charset=UTF-8"})
	public String auth(
			@QueryParam("verify_element") String verify_element, 
			@QueryParam("id_holder") String id_holder, 
			@QueryParam("id_card") String id_card, 
			@QueryParam("acc_no") String acc_no, 
			@QueryParam("mobile") String mobile,
			@QueryParam("card_type") String card_type,
			@QueryParam("valid_date_year") String valid_date_year,
			@QueryParam("valid_date_month") String valid_date_month,
			@QueryParam("valid_no") String valid_no,
			@Context HttpServletRequest request) throws Exception {
		
		String result = Constants.BLANK;
		
		// 获取apiKey
		String apiKey = request.getHeader(Constants.HEAD_APIKEY);
		// 获得用ID
		String userID = request.getHeader(Constants.HEAD_USER_ID);
		
		String errMessage = Constants.BLANK;
		
		String dataFrom = Constants.DATA_FROM_LOCAL;
		String isSuccess = "true";
		
		
		/** 验证 */
		boolean countErr = false;
		// 访问次数
		int count = Integer.parseInt(String.valueOf(RedisClient.get("xinyanCount")));
		if (0 >= count) {
			errMessage = "查询次数已达上限";
			
			JSONObject object = new JSONObject();
			object.put("success", false);
			object.put("data", null);
			object.put("errorCode", "100105");
			object.put("errorMsg", errMessage);
			
			result = object.toJSONString();
			
			countErr = true;
		}
		
		if (!"4055fc8ce1724519afc1631438aa6fa4".equals(apiKey) ||
				!"27".equals(userID)) {
			errMessage = "apiKey与用户不匹配";
			
			JSONObject object = new JSONObject();
			object.put("success", false);
			object.put("data", null);
			object.put("errorCode", "100102");
			object.put("errorMsg", errMessage);
			
			result = object.toJSONString();
			
		} else {
			
			if (!countErr) {
				Map<String, String> resultMap = bankCardAuthService.auth(verify_element, id_holder, id_card, acc_no, mobile, card_type, valid_date_year, valid_date_month, valid_no);
				dataFrom = resultMap.get("dataFrom");
				isSuccess = resultMap.get("isSuccess");
				result = resultMap.get("result");
				
				if (Boolean.parseBoolean(isSuccess)) {
					count--;
					RedisClient.set("xinyanCount", count);
				}
			}
		}
		
		// 记录日志
		ApiLog apiLog = new ApiLog();
		// uuid
		apiLog.setUuid(StringUtil.createUUID());
		apiLog.setUserID(userID);
		apiLog.setLocalApiID("99999999998");
		// 参数
		JSONObject params = new JSONObject();
		params.put("id_holder", id_holder);
		params.put("id_card", id_card);
		params.put("acc_no", acc_no);
		if (!StringUtil.isNull(valid_date_year)) {
			params.put("valid_date_year", valid_date_year);	
		}
		if (!StringUtil.isNull(valid_date_month)) {
			params.put("valid_date_month", valid_date_month);	
		}
		if (!StringUtil.isNull(valid_no)) {
			params.put("valid_no", valid_no);	
		}
		if (!StringUtil.isNull(card_type)) {
			params.put("card_type", card_type);	
		}
		apiLog.setParams(params.toJSONString());
		apiLog.setDataFrom(dataFrom);
		apiLog.setIsSuccess(isSuccess);
		apiLog.setQueryDate(new SimpleDateFormat(Constants.DATE_FROMAT_APILOG).format(new Date()));
		apiLogService.addLog(apiLog);
		
		return result;
	}

	// test
	public static void main(String[] args) throws Exception{
		
		// 私钥密码
		String PFX_PWD = "100000178_384015";
		// 访问url
		String URL = "http://tgw.baofoo.com/bankcard/v3/auth";
		
		String bankCardNo = "6214830223501445";
		String idCardNo = "120103198603292638";
		String name = "王梓";
		String mobile = "15922041128";
		
		/** 1、 商户号 **/
		String member_id = "100000178";
		/**2、终端号 **/
		String terminal_id = "100001164";
		/** 3、 加密数据类型 **/
		String data_type = "json";
		/** 4、加密数据 **/
		// 商户订单号
		String trans_id = "" + System.currentTimeMillis();
		// 订单日期
		String trade_date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		/** 组装参数  **/
		Map<Object, Object> ArrayData = new HashMap<Object, Object>();
		// 商户号
		ArrayData.put("member_id", member_id);
		// 终端号
		ArrayData.put("terminal_id", terminal_id);
		// 银行卡号
		ArrayData.put("acc_no", bankCardNo);
		// 身份证号
		ArrayData.put("id_card", idCardNo);
		// 姓名
		ArrayData.put("id_holder", name);
		// 订单号
		ArrayData.put("trans_id", trans_id);
		// 订单日期
		ArrayData.put("trade_date", trade_date);
		// 预留电话
		ArrayData.put("mobile", mobile);
		
		// 卡片类型
		ArrayData.put("card_type", "");
		// 卡有效期年
		ArrayData.put("valid_date_year", "");
		// 卡有效期月
		ArrayData.put("valid_date_month", "");
		// 信用卡安全码
		ArrayData.put("valid_no", "");
		// 验证类型
		ArrayData.put("verify_element", "1234");
		// 固定参数
		ArrayData.put("industry_type", "A1");
		ArrayData.put("product_type", "0");
		
		// 请求数据
		JSONObject requestData = (JSONObject) JSONObject.toJSON(ArrayData);
		// 请求数据字符串
		String requestDataStr = "";
		requestDataStr = requestData.toJSONString(); 
		
		String base64str = DesUtils.Base64Encode(requestDataStr);
		
		String filePath = "d:/bfkey_100000178@@100001164.pfx";
		
		File pfxfile = new File(filePath);
		if (!pfxfile.exists()) {
			System.out.println("私钥文件不存在！");
			return;
		}
		
		String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, filePath, PFX_PWD);//加密数据
		
		Map<String,String> HeadPostParam = new HashMap<String,String>();   
		HeadPostParam.put("member_id", member_id);
	    HeadPostParam.put("terminal_id", terminal_id);
	    HeadPostParam.put("data_type", data_type);
        HeadPostParam.put("data_content",data_content);
        String PostString = HttpUtil.RequestForm(URL, HeadPostParam);
        
        System.out.println(PostString);
	}
}
