package org.pbccrc.api.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.DBEntity;
import org.pbccrc.api.base.service.BankCardAuthService;
import org.pbccrc.api.base.service.HttpService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.DesUtils;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.base.util.rsa.RsaCodingUtil;
import org.pbccrc.api.core.dao.DBOperatorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class BankCardAuthServiceImpl implements BankCardAuthService{
	
	// 认证类型:三要素认证
	private static String TYPE3 = "123";
	// 商户私钥
	private static String PFX_NAME = "1107605@32915_pri.pfx";
	// 私钥密码
	private static String PFX_PWD = "xy623439";
	// 访问url
	private static String URL = "https://api.xinyan.com/bankcard/v3/auth";
	// 商户号
	private static String member_id = "1107605";
	// 终端号
	private static String terminal_id = "32915";
	// 加密数据类型
	private static String data_type = "json";
	
	@Autowired
	private DBOperatorDao dbOperatorDao;
	
	@Autowired
	private HttpService httpService;
	
	
	/**
	 * 认证
	 * @param verify_element     验证类型(三要素/四要素)
	 * @param id_holder			  姓名	
	 * @param id_card			  身份证号
	 * @param acc_no			  银行卡号
	 * @param mobile			  预留电话
	 * @param card_type			  卡类型(信用卡/储蓄卡)
	 * @param valid_date_year    卡有效期年
	 * @param valid_date_month   卡有效期月
	 * @param valid_no			   信用卡安全码
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> auth(String verify_element, String id_holder, String id_card, String acc_no, String mobile,
			String card_type, String valid_date_year, String valid_date_month, String valid_no) throws Exception {
		
		Map<String, String> retrunMap = new HashMap<String, String>();
		
		String result = Constants.BLANK;
		String dataFrom = Constants.DATA_FROM_LOCAL;
		
		/** 查询本地数据库 */
		
		DBEntity dbEntity = new DBEntity();
		dbEntity.setTableName("bankCardAuth");
		List<String> fields = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		// 姓名 身份证号 卡号 卡类型
		fields.add("id_holder");
		fields.add("id_card");
		fields.add("acc_no");
		values.add(id_holder);
		values.add(id_card);
		values.add(acc_no);
		
		if (!StringUtil.isNull(valid_date_year)) {
			fields.add("valid_date_year");
			values.add(valid_date_year);
		}
		if (!StringUtil.isNull(valid_date_month)) {
			fields.add("valid_date_month");
			values.add(valid_date_month);
		}
		if (!StringUtil.isNull(valid_no)) {
			fields.add("valid_no");
			values.add(valid_no);
		}
		if (!StringUtil.isNull(card_type)) {
			fields.add("card_type");
			values.add(card_type);
		}
		
		// 判断认证方式(三要素,四要素)
		if (TYPE3.equals(verify_element)) {
			// 三要素
		} else {
			// 四要素
			// 预留电话
			fields.add("mobile");
			values.add(mobile);
		}
		
		dbEntity.setFields(fields);
		dbEntity.setValues(values);
		
		Map<String, Object> queryData = dbOperatorDao.queryData(dbEntity);
		
		
		if (null != queryData) {
			// 本地有数据 直接返回
			result = String.valueOf(queryData.get("result_data"));
			retrunMap.put("isSuccess", "true");
		} else {
			// 本地无数据 访问远程并记录到本地
			
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
			ArrayData.put("acc_no", acc_no);
			// 身份证号
			ArrayData.put("id_card", id_card);
			// 姓名
			ArrayData.put("id_holder", id_holder);
			// 订单号
			ArrayData.put("trans_id", trans_id);
			// 订单日期
			ArrayData.put("trade_date", trade_date);
			// 卡片类型
			ArrayData.put("card_type", StringUtil.null2Blank(card_type));
			// 卡有效期年
			ArrayData.put("valid_date_year", StringUtil.null2Blank(valid_date_year));
			// 卡有效期月
			ArrayData.put("valid_date_month", StringUtil.null2Blank(valid_date_month));
			// 信用卡安全码
			ArrayData.put("valid_no", StringUtil.null2Blank(valid_no));
			// 验证类型
			ArrayData.put("verify_element", verify_element);
			// 固定参数
			ArrayData.put("industry_type", "A1");
			ArrayData.put("product_type", "0");
			
			// 判断认证方式(三要素,四要素)
			if (TYPE3.equals(verify_element)) {
				// 三要素
			} else {
				// 四要素
				// 预留电话
				ArrayData.put("mobile", mobile);
			}
			
			// 请求数据
			JSONObject requestData = (JSONObject) JSONObject.toJSON(ArrayData);
			// 请求数据字符串
			String requestDataStr = "";
			requestDataStr = requestData.toJSONString(); 
			
			String base64str = DesUtils.Base64Encode(requestDataStr);
			
			String filePath = this.getClass().getClassLoader().getResource(PFX_NAME).getPath();
			
//			File pfxfile = new File(filePath);
//			if (!pfxfile.exists()) {
//				System.out.println("私钥文件不存在！");
//				return null;
//			}
			
			String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, filePath, PFX_PWD);//加密数据
			
			Map<String,String> HeadPostParam = new HashMap<String,String>();   
			HeadPostParam.put("member_id", member_id);
		    HeadPostParam.put("terminal_id", terminal_id);
		    HeadPostParam.put("data_type", data_type);
	        HeadPostParam.put("data_content",data_content);
	        String PostString = httpService.RequestForm(URL, HeadPostParam);
	        
	        // 判断是否查询成功
	        JSONObject object = JSONObject.parseObject(PostString);
	        boolean success = Boolean.valueOf(String.valueOf(object.get("success")));
			if (success) {
				fields.add("result_data");
				values.add(PostString);
				dbEntity.setFields(fields);
				dbEntity.setValues(values);

				dbOperatorDao.insertData(dbEntity);

				retrunMap.put("isSuccess", "true");
			} else {
				retrunMap.put("isSuccess", "false");
			}
	        
			result = PostString;
			
			dataFrom = Constants.DATA_FROM_XINYAN;
		}
		
		retrunMap.put("result", result);
		retrunMap.put("dataFrom", dataFrom);
		
		return retrunMap;
	}

}
