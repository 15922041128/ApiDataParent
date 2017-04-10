package org.pbccrc.api.portal.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.pbccrc.api.base.bean.PersonLog;
import org.pbccrc.api.base.bean.User;
import org.pbccrc.api.base.service.CreditService;
import org.pbccrc.api.base.service.PBaseInfoService;
import org.pbccrc.api.base.service.PPersonService;
import org.pbccrc.api.base.service.PReditService;
import org.pbccrc.api.base.service.PersonLogService;
import org.pbccrc.api.base.service.ScoreService;
import org.pbccrc.api.base.service.UserService;
import org.pbccrc.api.base.service.ZhIdentificationService;
import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.RemoteApiOperator;
import org.pbccrc.api.base.util.StringUtil;
import org.pbccrc.api.base.util.SystemUtil;
import org.pbccrc.api.base.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("deprecation")
@Controller
public class CreditController {
	
	@Autowired
	private CreditService creditService;
	
	@Autowired
	private PersonLogService personLogService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ScoreService scoreService;
	
	@Autowired
	private PPersonService pPersonService;
	
	@Autowired
	private PBaseInfoService pBaseInfoService;
	
	@Autowired
	private PReditService pReditService;
	
	@Autowired
	private ZhIdentificationService zhIdentificationService;
	
	@Autowired
	private RemoteApiOperator remoteApiOperator;
	
	@Autowired
	private Validator validator;
	
	/**
	 * 信用风险信息报送step1
	 * @return
	 */
	@POST
	@ResponseBody
	@RequestMapping(value="/r/credit/add", produces={"application/json;charset=UTF-8"})
	public String add(
			@QueryParam("userID") String userID,
			@QueryParam("name") String name,
			@QueryParam("idCardNo") String idCardNo,
			@QueryParam("address") String address,
			@QueryParam("phoneNo") String phoneNo,
			@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		
		User user = userService.getUserByID(userID);
		
		String filePath = Constants.BLANK;
		filePath = request.getSession().getServletContext().getRealPath("/") + Constants.FILE_PATH_BASE + File.separator + Constants.FILE_PATH_PHOTO;
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		DiskFileUpload fu = new DiskFileUpload();
		fu.setSizeMax(10485760);
		fu.setSizeThreshold(4096);
		
		List<FileItem> fileItems = fu.parseRequest(request);
		
		File photo = null;
		FileItem item = fileItems.get(0);
		String fileName = item.getName();
		
		// 判断是否有文件上传
		if (!StringUtil.isNull(fileName)) {
			fileName = fileName.substring(fileName.indexOf(Constants.POINT), fileName.length());
			fileName = System.currentTimeMillis() + fileName;
			photo = new File(filePath + File.separator + fileName);
			item.write(photo);
		}
		
		JSONObject returnJson = creditService.addPerson(name, idCardNo, phoneNo, address, photo, user.getUserName());
		
		// 记录日志
		PersonLog personLog = new PersonLog();
		// ip地址
		personLog.setIpAddress(SystemUtil.getIpAddress(request));
		// 用户ID
		personLog.setUserID(userID);
		// 操作类型
		personLog.setOperatorType(Constants.OPERATOR_TYPE_ADD_STEP1);
		// 总操作数
		personLog.setTotalCount(Constants.STR_ONE);
		// 成功操作数
		personLog.setSuccessCount(Constants.STR_ONE);
		// 失败操作数
		personLog.setFailCount(Constants.STR_ZERO);
		// 查询时间
		personLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		// 备注
		personLog.setNote(returnJson.toJSONString());
		personLogService.addLog(personLog);
		
		return returnJson.getString("personID");
	}
	
	/**
	 * 信用风险信息报送step2
	 * @return
	 */
	@POST
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/r/credit/add2", produces={"text/html;charset=UTF-8"})
	public String add2(
			@QueryParam("userID") String userID,
			@QueryParam("contactDate") String contactDate,
			@QueryParam("hireDate") String hireDate,
			@QueryParam("expireDate") String expireDate,
			@QueryParam("type") String type,
			@QueryParam("loanUsed") String loanUsed,
			@QueryParam("totalAmount") String totalAmount,
			@QueryParam("balance") String balance,
			@QueryParam("status") String status,
			@QueryParam("personID") String personID,
			@Context HttpServletRequest request) throws Exception {
		
		String returnStr = Constants.RET_STAT_ERROR;
		
		User user = userService.getUserByID(userID);
		
		JSONObject returnJson = creditService.addPersonRedit(personID, hireDate, hireDate, expireDate, type, loanUsed, totalAmount, balance, status, user);
		
		if (returnJson.getBooleanValue("isSuccess")) {
			returnStr = Constants.RET_STAT_SUCCESS;
		}
		
		// 记录日志
		PersonLog personLog = new PersonLog();
		// ip地址
		personLog.setIpAddress(SystemUtil.getIpAddress(request));
		// 用户ID
		personLog.setUserID(userID);
		// 操作类型
		personLog.setOperatorType(Constants.OPERATOR_TYPE_ADD_STEP2);
		// 总操作数
		personLog.setTotalCount(Constants.STR_ONE);
		// 成功操作数
		personLog.setSuccessCount(Constants.STR_ONE);
		// 失败操作数
		personLog.setFailCount(Constants.STR_ZERO);
		// 查询时间
		personLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		// 备注
		personLog.setNote(returnJson.getJSONObject("pRedit").toJSONString());
		personLogService.addLog(personLog);
		
		return returnStr;
	}
	
	/**
	 * 信用风险信息查询
	 * @return
	 */
	@POST
	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="/r/credit/query", produces={"application/json;charset=UTF-8"})
	public JSONObject query(
			@QueryParam("userID") String userID,
			@QueryParam("name") String name,
			@QueryParam("idCardNo") String idCardNo,
			@Context HttpServletRequest request) throws Exception {
		
		JSONObject jsonObject = creditService.getReditList(name, idCardNo);
		
		// 记录日志
		PersonLog personLog = new PersonLog();
		// ip地址
		personLog.setIpAddress(request.getRemoteAddr());
		// 用户ID
		personLog.setUserID(String.valueOf(userID));
		// 操作类型
		personLog.setOperatorType(Constants.OPERATOR_TYPE_QUERY);
		// 总操作数
		personLog.setTotalCount(Constants.STR_ONE);
		// 判断是否查询成功
		if (StringUtil.isNull(jsonObject.getString("status"))) {
			// 查询成功
			// 成功操作数
			personLog.setSuccessCount(Constants.STR_ONE);
			// 失败操作数
			personLog.setFailCount(Constants.STR_ZERO);
		} else {
			// 查询失败
			// 成功操作数
			personLog.setSuccessCount(Constants.STR_ZERO);
			// 失败操作数
			personLog.setFailCount(Constants.STR_ONE);
		}
		// 查询时间
		personLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		// 备注
		JSONObject noteJson = new JSONObject();
		noteJson.put("name", name);
		noteJson.put("idCardNo", idCardNo);
		personLog.setNote(noteJson.toJSONString());
		personLogService.addLog(personLog);
		
		return jsonObject;
	}
	
	/**
	 * 模板文件导出
	 * @param type
	 * @param request
	 * @throws Exception
	 */
	@POST
	@CrossOrigin
	@RequestMapping(value="/r/credit/downloadTemplate", produces={"application/x-excel;charset=UTF-8"})
	public void downloadTemplate(
			@QueryParam("type") String type, 
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		
		String filePath;
		String fileName;
		
		// 根据类型返回不同模板路径
		if ("query".equals(type)) {
			filePath = request.getSession().getServletContext().getRealPath(Constants.BATCH_QUERY_TEMPLATE_FILE);
			fileName = "batch_query_template.xlsx";
		} else {
			filePath = request.getSession().getServletContext().getRealPath(Constants.BATCH_ADD_TEMPLATE_FILE);
			fileName = "batch_report_template.xlsm";
		}
		
		downloadFile(fileName, filePath, response);
	}
	
	/**
	 * 文件下载
	 * @param fileName
	 * @param filePath
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@POST
	@CrossOrigin
	@RequestMapping(value="/r/credit/downloadFile", produces={"application/x-excel;charset=UTF-8"})
	public void downloadFile(
			@QueryParam("fileName") String fileName, 
			@QueryParam("filePath") String filePath,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		
		this.downloadFile(fileName, filePath, response);
		
	}
	
	/**
	 * 文件下载
	 * @param fileName
	 * @param filePath
	 * @param response
	 * @throws Exception
	 */
	private void downloadFile(String fileName, String filePath, HttpServletResponse response) throws Exception {
		
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		fileName = URLEncoder.encode(fileName, "UTF-8"); 
		try {
			File file = new File(filePath);
			
			response.setCharacterEncoding("UTF-8");  
	        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);  
	        response.setHeader("Content-Length",String.valueOf(file.length()));
	        in = new BufferedInputStream(new FileInputStream(file));  
	        out = new BufferedOutputStream(response.getOutputStream());
	        byte[] data = new byte[1024];  
	        int len = 0;  
	        while (-1 != (len=in.read(data, 0, data.length))) {  
	            out.write(data, 0, len);  
	        }
		} catch (Exception e) {
			throw e;
		} finally {
			if (in != null) {  
                in.close();  
            }  
            if (out != null) {  
                out.close();  
            }  
		}
	}
	
	/**
	 * 信用风险信息批量查询
	 * @param request
	 * @throws Exception
	 */
	@POST
	@ResponseBody
	@RequestMapping(value="/r/credit/queryAll", produces={"application/json;charset=UTF-8"})
	public JSONObject queryAll(@QueryParam("userID") String userID,
			@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		
		DiskFileUpload fu = new DiskFileUpload();
		fu.setSizeMax(10485760);
		fu.setSizeThreshold(4096);
		List<FileItem> fileItems = fu.parseRequest(request);
		FileItem fileItem = fileItems.get(0);
		
		String basePath = request.getSession().getServletContext().getRealPath(Constants.BATCH_QUERY_FILE);
		
		Map<String, String> map = createExcelQuery(fileItem, basePath);
		
		String exportPath = map.get("exportPath");
		String fileName = exportPath.substring(exportPath.lastIndexOf("\\") + 1);
		JSONObject result = new JSONObject();

		result.put("filePath", exportPath);
		result.put("fileName", fileName);
		
		// 记录日志
		PersonLog personLog = new PersonLog();
		// ip地址
		personLog.setIpAddress(request.getRemoteAddr());
		// 用户ID
		personLog.setUserID(userID);
		// 操作类型
		personLog.setOperatorType(Constants.OPERATOR_TYPE_QUERYALL);
		// 总操作数
		personLog.setTotalCount(map.get("totalCount"));
		// 成功操作数
		personLog.setSuccessCount(map.get("successCount"));
		// 失败操作数
		personLog.setFailCount(map.get("failCount"));
		// 查询时间
		personLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		// 备注
		personLog.setNote(exportPath);
		personLogService.addLog(personLog);
		
		return result;
	}
	
	/**
	 * 信用风险信息批量报送
	 * @param request
	 * @throws Exception
	 */
	@POST
	@ResponseBody
	@RequestMapping(value="/r/credit/addAll", produces={"application/json;charset=UTF-8"})
	public JSONObject addAll(@QueryParam("userID") String userID,
			@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		
		DiskFileUpload fu = new DiskFileUpload();
		fu.setSizeMax(10485760);
		fu.setSizeThreshold(4096);
		List<FileItem> fileItems = fu.parseRequest(request);
		FileItem fileItem = fileItems.get(0);
		
		String basePath = request.getSession().getServletContext().getRealPath(Constants.BATCH_ADD_FILE);
		
		Map<String, String> map = createExcelAdd(fileItem, basePath, userID);
		
		String exportPath = map.get("exportPath");
		String fileName = exportPath.substring(exportPath.lastIndexOf("\\") + 1);
		JSONObject result = new JSONObject();

		result.put("filePath", exportPath);
		result.put("fileName", fileName);
		
		// 记录日志
		PersonLog personLog = new PersonLog();
		// ip地址
		personLog.setIpAddress(request.getRemoteAddr());
		// 用户ID
		personLog.setUserID(userID);
		// 操作类型
		personLog.setOperatorType(Constants.OPERATOR_TYPE_ADDALL);
		// 总操作数
		personLog.setTotalCount(map.get("totalCount"));
		// 成功操作数
		personLog.setSuccessCount(map.get("successCount"));
		// 失败操作数
		personLog.setFailCount(map.get("failCount"));
		// 查询时间
		personLog.setQueryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		// 备注
		personLog.setNote(exportPath);
		personLogService.addLog(personLog);
		
		return result;
	}
	
	/**
	 * 批量报送
	 * @param fileItem
	 * @throws Exception
	 */
	private Map<String, String> createExcelAdd(FileItem fileItem, String basePath, String userID) throws Exception {
		Map<String, String> returnMap = new HashMap<String, String>();
		InputStream inputStream = fileItem.getInputStream();
		// 获取excel文件
		XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
		// 获取sheet页(第一页)
		XSSFSheet workSheet = workBook.getSheetAt(0);
		
		// 总条数
		int totalCount = 0;
		// 成功条数
		int successCount = 0;
		// 失败条数
		int failCount = 0;
		
		// 错误文件
		XSSFWorkbook errorBook = new XSSFWorkbook();
		XSSFSheet errorSheet = errorBook.createSheet("上传文件错误信息");
		XSSFRow errorRow = errorSheet.createRow(failCount++);
		errorRow.createCell(0).setCellValue("姓名");
		errorRow.createCell(1).setCellValue("身份证号");
		errorRow.createCell(2).setCellValue("手机号");
		errorRow.createCell(3).setCellValue("省");
		errorRow.createCell(4).setCellValue("市");
		errorRow.createCell(5).setCellValue("区");
		errorRow.createCell(6).setCellValue("详细地址");
		errorRow.createCell(7).setCellValue("合同日期");
		errorRow.createCell(8).setCellValue("起租日");
		errorRow.createCell(9).setCellValue("到期日");
		errorRow.createCell(10).setCellValue("业务类型");
		errorRow.createCell(11).setCellValue("用途");
		errorRow.createCell(12).setCellValue("总金额");
		errorRow.createCell(13).setCellValue("余额");
		errorRow.createCell(14).setCellValue("状态");
		errorRow.createCell(15).setCellValue("错误信息");
		
		// 遍历第一页所有数据
		for (int i = 1; i <= workSheet.getLastRowNum(); i++) {
			 totalCount++;
			 XSSFRow row = workSheet.getRow(i);
			 if (null != row) {
				 // 姓名
				 String name = getValue(row.getCell(0));
				 // 身份证号
				 String idCardNo = getValue(row.getCell(1));
				 // 手机号
				 String tel = getValue(row.getCell(2));
				 // 省
				 String province = getValue(row.getCell(3));
				 // 市
				 String city = getValue(row.getCell(4));
				 // 区
				 String area = getValue(row.getCell(5));
				 // 详细地址
				 String address = getValue(row.getCell(6));
				 // 合同日期
				 String contactDate = getValue(row.getCell(7));
				 // 起租日
				 String hireDate = getValue(row.getCell(8));
				 // 到期日
				 String expireDate = getValue(row.getCell(9));
				 // 业务类型
				 String type = getValue(row.getCell(10));
				 // 用途
				 String loanUsed = getValue(row.getCell(11));
				 // 总金额
				 String totalAmount = getValue(row.getCell(12));
				 // 余额
				 String balance = getValue(row.getCell(13));
				 // 状态
				 String status = getValue(row.getCell(14));
				 

				 // 验证姓名
				 String message = validator.validateName(name);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证身份证号
				 message = validator.validateIDCard(idCardNo);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证手机号
				 message = validator.validateMobile(tel);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证省
				 message = validator.validateProvince(province);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证市
				 message = validator.validateCity(city);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证区
				 message = validator.validateArea(area);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证详细地址
				 message = validator.validateAddress(address);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证合同日期
				 message = validator.validateDate(contactDate);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证起租日
				 message = validator.validateDate(hireDate);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证到期日
				 message = validator.validateDate(expireDate);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证业务类型
				 message = validator.validateType(type);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证用途
				 message = validator.validateLoanUsed(loanUsed);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证总金额
				 message = validator.validateNumber(totalAmount);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 验证余额
				 message = validator.validateNumber(balance);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 // 余额小于总金额验证
				 if (Integer.parseInt(balance) > Integer.parseInt(totalAmount)) {
					 message = "余额应小于等于总金额。";
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 row.createCell(15).setCellValue(message);
					 continue;
				 }
				 
				 // 验证状态
				 message = validator.validateStatus(status);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, 
							 name, idCardNo, tel, 
							 province, city, area, address, 
							 contactDate, hireDate, expireDate, 
							 type, loanUsed, totalAmount, balance, status, message);
					 continue;
				 }
				 
				 address = province + city + area + address;
				 
				 // insertDB
				 // 获取当前时间
				 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 String currentDate = format.format(new Date());
				 // 获取当前用户
				 User currentUser = userService.getUserByID(userID);
				 Map<String, String> person = new HashMap<String, String>();
				 person.put("name", name);
				 person.put("idCardNo", idCardNo);

				 String personID;

				 /** p_person表操作 */
				 if (pPersonService.isExist(person) > 0) {
					// 当前person已存在,查询出personID
					Map<String, Object> resPerson = pPersonService.selectOne(person);
					personID = String.valueOf(resPerson.get(Constants.PERSON_ID));
				 } else {
					// 当前person不存在,插入person表数据并返回personID
					pPersonService.addPerson(person);
					personID = String.valueOf(person.get(Constants.PERSON_ID));
				 }

				 Map<String, Object> pBaseInfo = new HashMap<String, Object>();
				 pBaseInfo.put("personID", personID);
				 pBaseInfo.put("tel", tel);
				 pBaseInfo.put("address", address);
				 pBaseInfo.put("photo", Constants.BLANK);

				 /** p_baseInfo表操作 */
				 // 即使存在相同数据,依然保留
				 pBaseInfo.put("createUser", currentUser.getUserName());
				 pBaseInfo.put("createTime", currentDate);
				 pBaseInfo.put("updateUser", currentUser.getUserName());
				 pBaseInfo.put("updateTime", currentDate);
				 pBaseInfoService.addPBaseInfo(pBaseInfo);
				 
				 Map<String, Object> pRedit = new HashMap<String, Object>();
				 pRedit.put("personID", personID);
				 pRedit.put("contactDate",contactDate);
				 pRedit.put("hireDate", hireDate);
				 pRedit.put("expireDate", expireDate);
				 pRedit.put("type", type);
				 pRedit.put("loanUsed", loanUsed);
				 pRedit.put("totalAmount", totalAmount);
				 pRedit.put("balance", balance);
				 pRedit.put("status", status);
				 pRedit.put("bizOccurOrg", currentUser.getCompName());
				 pRedit.put("createUser", currentUser.getUserName());
				 pRedit.put("createTime", currentDate);
				 pRedit.put("updateUser", currentUser.getUserName());
				 pRedit.put("updateTime", currentDate);
				
				pReditService.addPRedit(pRedit);
				 
				 successCount++;
 			 }
		}
		
		errorRow = errorSheet.createRow(failCount++);
		errorRow.createCell(0).setCellValue("总计" + totalCount + "条");
		errorRow = errorSheet.createRow(failCount++);
		errorRow.createCell(0).setCellValue("成功" + successCount + "条");
		errorRow = errorSheet.createRow(failCount++);
		errorRow.createCell(0).setCellValue("失败" + (totalCount - successCount) + "条");
		
		String exportPath = Constants.BLANK;
		// 生成返回信息文件
		OutputStream os = null;
		try {
			File file = new File(basePath);
			if(!file.exists()){
				file.mkdirs();
			}
			exportPath = basePath + File.separator + System.currentTimeMillis() + ".xls";
			os = new FileOutputStream(exportPath);
			errorBook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		returnMap.put("exportPath", exportPath);
		returnMap.put("totalCount", String.valueOf(totalCount));
		returnMap.put("successCount", String.valueOf(successCount));
		returnMap.put("failCount", String.valueOf(totalCount - successCount));
		
		return returnMap;
	}
	
	/**
	 * 批量查询
	 * @param fileItem
	 * @param basePath
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> createExcelQuery(FileItem fileItem, String basePath) throws Exception{
		
		Map<String, String> returnMap = new HashMap<String, String>();
		
		InputStream inputStream = fileItem.getInputStream();
		// 获取excel文件
		XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
		// 获取sheet页(第一页)
		XSSFSheet workSheet = workBook.getSheetAt(0);
		
		// 总条数
		int totalCount = 0;
		// 成功条数
		int successCount = 0;
		// 失败条数
		int failCount = 0;
		// 基本信息记录数 
		int baseCount = 0;
		// 信用信息记录数
		int reditCount = 0;
		// 失信人被执行信息记录数
		int dishonestInfoCount = 0;
		
		// 返回查询文件
		XSSFWorkbook resultBook = new XSSFWorkbook();
		// 基本信息sheet
		XSSFSheet baseInfoSheet = resultBook.createSheet("基本信息");
		XSSFRow baseInfoRow = baseInfoSheet.createRow(baseCount++);
		baseInfoRow.createCell(0).setCellValue("姓名");
		baseInfoRow.createCell(1).setCellValue("身份证号");
		baseInfoRow.createCell(2).setCellValue("手机号");
		baseInfoRow.createCell(3).setCellValue("地址");
		baseInfoRow.createCell(4).setCellValue("信用评分");
		// 信用信息sheet
		XSSFSheet reditSheet = resultBook.createSheet("信用信息");
		XSSFRow reditRow = reditSheet.createRow(reditCount++);
		reditRow.createCell(0).setCellValue("姓名");
		reditRow.createCell(1).setCellValue("身份证号");
		reditRow.createCell(2).setCellValue("合同日期");
		reditRow.createCell(3).setCellValue("起租日");
		reditRow.createCell(4).setCellValue("到期日");
		reditRow.createCell(5).setCellValue("贷款用途");
		reditRow.createCell(6).setCellValue("总金额");
		reditRow.createCell(7).setCellValue("余额");
		reditRow.createCell(8).setCellValue("状态");
		reditRow.createCell(9).setCellValue("业务发生机构");
		reditRow.createCell(10).setCellValue("日期时间");
		// 失信人被执行信息sheet
		XSSFSheet dishonestInfoSheet = resultBook.createSheet("失信人被执行信息");
		XSSFRow dishonestInfoRow = dishonestInfoSheet.createRow(dishonestInfoCount++);
		dishonestInfoRow.createCell(0).setCellValue("被执行人姓名");
		dishonestInfoRow.createCell(1).setCellValue("被执行人身份证号码");
		dishonestInfoRow.createCell(2).setCellValue("省份");
		dishonestInfoRow.createCell(3).setCellValue("发布时间");
		dishonestInfoRow.createCell(4).setCellValue("执行依据号");
		dishonestInfoRow.createCell(5).setCellValue("被执行人的旅行情况");
		dishonestInfoRow.createCell(6).setCellValue("执行法院");
		dishonestInfoRow.createCell(7).setCellValue("生效法律文书确定的义务");
		dishonestInfoRow.createCell(8).setCellValue("失信被执行人行为具体情形");
		// 错误信息sheet
		XSSFSheet errorSheet = resultBook.createSheet("错误信息");
		XSSFRow errorRow = errorSheet.createRow(failCount++);
		errorRow.createCell(0).setCellValue("姓名");
		errorRow.createCell(1).setCellValue("身份证号");
		errorRow.createCell(2).setCellValue("错误信息");
		
		// 遍历第一页所有数据
		for (int i = 1; i <= workSheet.getLastRowNum(); i++) {
			 totalCount++;
			 XSSFRow row = workSheet.getRow(i);
			 if (null != row) {
				 // 姓名
				 String name = getValue(row.getCell(0));
				 // 身份证号
				 String idCardNo = getValue(row.getCell(1));
				 
				 // 验证姓名
				 String message = validator.validateName(name);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, name, idCardNo, message);
					 continue;
				 }
				 
				 // 验证身份证号
				 message = validator.validateIDCard(idCardNo);
				 if (!Constants.BLANK.equals(message)) {
					 createErrorRow(errorSheet, failCount++, name, idCardNo, message);
					 continue;
				 }
				 
				 // 获取用户ID
				 Map<String, String> person = new HashMap<String, String>();
				 person.put("name", name);
				 person.put("idCardNo", idCardNo);
				 Map<String, Object> resPerson = pPersonService.selectOne(person);
				 // 验证用户是否存在
				 if (null == resPerson) {
					 message = "该人员信息不存在。";
					 createErrorRow(errorSheet, failCount++, name, idCardNo, message);
					 continue;
				 }
				 String personID = String.valueOf(resPerson.get(Constants.PERSON_ID));
				 
				 // 获取用户基本信息
				 List<Map<String, Object>> pBaseInfoList = pBaseInfoService.queryByPersonID(personID);
				 baseInfoRow = baseInfoSheet.createRow(baseCount++);
				 if (null == pBaseInfoList || pBaseInfoList.size() == 0) {
					 baseInfoRow.createCell(0).setCellValue(Constants.BLANK);
					 baseInfoRow.createCell(1).setCellValue(Constants.BLANK);
					 baseInfoRow.createCell(2).setCellValue(Constants.BLANK);
					 baseInfoRow.createCell(3).setCellValue(Constants.BLANK);
				 } else {
					 Map<String, Object> pBaseInfoMap = pBaseInfoList.get(0);
					 baseInfoRow.createCell(0).setCellValue(name);
					 baseInfoRow.createCell(1).setCellValue(idCardNo);
					 baseInfoRow.createCell(2).setCellValue(StringUtil.null2Blank(String.valueOf(pBaseInfoMap.get("tel"))));
					 baseInfoRow.createCell(3).setCellValue(StringUtil.null2Blank(String.valueOf(pBaseInfoMap.get("address"))));
				 }
				 
				// 根据身份证号获取内码信息
				String innerID = null;
				Map<String, Object> insideCodeMap = zhIdentificationService.getInnerID(name, idCardNo);
				if (null != insideCodeMap) {
					innerID = String.valueOf(insideCodeMap.get("INNERID"));
				}
				
				// 获取用户信用评分信息
				String score = "暂无分数";
				if (!StringUtil.isNull(innerID)) {
					List<Map<String, Object>> scoreList = scoreService.getScore(innerID);
					if (null != scoreList && 0 != scoreList.size()) {
						Map<String, Object> scoreMap = scoreList.get(0);
						score = String.valueOf(scoreMap.get("SCORE"));
					}
				}
				baseInfoRow.createCell(4).setCellValue(score);
				 
				 // 获取用户信贷信息
				 List<Map<String, Object>> reditList = pReditService.queryAll(personID);
				 for (int j = 0 ; j < reditList.size(); j++) {
					 Map<String, Object> map = reditList.get(j);
					 reditRow = reditSheet.createRow(reditCount++);
					 reditRow.createCell(0).setCellValue(name);
					 reditRow.createCell(1).setCellValue(idCardNo);
					 reditRow.createCell(2).setCellValue(StringUtil.null2Blank(String.valueOf(map.get("contactDate"))));
					 reditRow.createCell(3).setCellValue(StringUtil.null2Blank(String.valueOf(map.get("hireDate"))));
					 reditRow.createCell(4).setCellValue(StringUtil.null2Blank(String.valueOf(map.get("expireDate"))));
					 reditRow.createCell(5).setCellValue(StringUtil.null2Blank(String.valueOf(map.get("loanUsed"))));
					 reditRow.createCell(6).setCellValue(StringUtil.null2Blank(String.valueOf(map.get("totalAmount"))));
					 reditRow.createCell(7).setCellValue(StringUtil.null2Blank(String.valueOf(map.get("balance"))));
					 reditRow.createCell(8).setCellValue(StringUtil.null2Blank(String.valueOf(map.get("status"))));
					 reditRow.createCell(9).setCellValue(StringUtil.null2Blank(String.valueOf(map.get("bizOccurOrg"))));
					 reditRow.createCell(10).setCellValue(StringUtil.null2Blank(String.valueOf(map.get("createTime"))));
				 }
				
				// 被失信人被执行信息
				StringBuffer url = new StringBuffer(Constants.WEB_URL + Constants.URL_LDB_GETSXR);
				url.append(Constants.URL_CONNECTOR);
				url.append("identifier");
				url.append(Constants.EQUAL);
				url.append(idCardNo);
				url.append(Constants.URL_PARAM_CONNECTOR);
				url.append("name");
				url.append(Constants.EQUAL);
				url.append(URLEncoder.encode(name, "UTF-8"));
				String sxr = remoteApiOperator.remoteAccept(url.toString());
				JSONObject sxrObject = JSONObject.parseObject(sxr);
				JSONArray array = (JSONArray) sxrObject.get("retData");
				
				for (int j = 0; j < array.size(); j++) {
					JSONObject object = (JSONObject) array.get(j);
					dishonestInfoRow = dishonestInfoSheet.createRow(dishonestInfoCount++);
					dishonestInfoRow.createCell(0).setCellValue(StringUtil.null2Blank(object.getString("INAME")));
					dishonestInfoRow.createCell(1).setCellValue(StringUtil.null2Blank(object.getString("CARDNUM")));
					dishonestInfoRow.createCell(2).setCellValue(StringUtil.null2Blank(object.getString("AREA_NAME")));
					dishonestInfoRow.createCell(3).setCellValue(StringUtil.null2Blank(object.getString("REG_DATE")));
					dishonestInfoRow.createCell(4).setCellValue(StringUtil.null2Blank(object.getString("GIST_CID")));
					dishonestInfoRow.createCell(5).setCellValue(StringUtil.null2Blank(object.getString("PERFORMANCE")));
					dishonestInfoRow.createCell(6).setCellValue(StringUtil.null2Blank(object.getString("COURT_NAME")));
					dishonestInfoRow.createCell(7).setCellValue(StringUtil.null2Blank(object.getString("DUTY")));
					dishonestInfoRow.createCell(8).setCellValue(StringUtil.null2Blank(object.getString("DISREPUT_TYPE_NAME")));
				}
				
				successCount++;
 			 }
		}
		
		errorRow = errorSheet.createRow(failCount++);
		errorRow.createCell(0).setCellValue("总计" + totalCount + "条");
		errorRow = errorSheet.createRow(failCount++);
		errorRow.createCell(0).setCellValue("成功" + successCount + "条");
		errorRow = errorSheet.createRow(failCount++);
		errorRow.createCell(0).setCellValue("失败" + (totalCount - successCount) + "条");
		
		String exportPath = Constants.BLANK;
		// 生成返回信息文件
		OutputStream os = null;
		try {
			File file = new File(basePath);
			if(!file.exists()){
				file.mkdirs();
			}
			exportPath = basePath + File.separator + System.currentTimeMillis() + ".xls";
			os = new FileOutputStream(exportPath);
			resultBook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		returnMap.put("exportPath", exportPath);
		returnMap.put("totalCount", String.valueOf(totalCount));
		returnMap.put("successCount", String.valueOf(successCount));
		returnMap.put("failCount", String.valueOf(totalCount - successCount));
		
		return returnMap;
	}
	
	/**
	 * 
	 * @param errorSheet
	 * @param failCount
	 * @param name
	 * @param idCardNo
	 * @param tel
	 * @param province
	 * @param city
	 * @param area
	 * @param address
	 * @param contactDate
	 * @param hireDate
	 * @param expireDate
	 * @param type
	 * @param loanUsed
	 * @param totalAmount
	 * @param balance
	 * @param status
	 * @param message
	 */
	private void createErrorRow(XSSFSheet errorSheet, int failCount, 
			String name, String idCardNo, String tel, 
			String province, String city, String area, String address, 
			String contactDate, String hireDate, String expireDate, 
			String type, String loanUsed, String totalAmount, String balance, String status, String message) {
		 XSSFRow errorRow = errorSheet.createRow(failCount);
		 errorRow.createCell(0).setCellValue(StringUtil.null2Blank(name));
		 errorRow.createCell(1).setCellValue(StringUtil.null2Blank(idCardNo));
		 errorRow.createCell(2).setCellValue(StringUtil.null2Blank(tel));
		 errorRow.createCell(3).setCellValue(StringUtil.null2Blank(province));
		 errorRow.createCell(4).setCellValue(StringUtil.null2Blank(city));
		 errorRow.createCell(5).setCellValue(StringUtil.null2Blank(area));
		 errorRow.createCell(6).setCellValue(StringUtil.null2Blank(address));
		 errorRow.createCell(7).setCellValue(StringUtil.null2Blank(contactDate));
		 errorRow.createCell(8).setCellValue(StringUtil.null2Blank(hireDate));
		 errorRow.createCell(9).setCellValue(StringUtil.null2Blank(expireDate));
		 errorRow.createCell(10).setCellValue(StringUtil.null2Blank(type));
		 errorRow.createCell(11).setCellValue(StringUtil.null2Blank(loanUsed));
		 errorRow.createCell(12).setCellValue(StringUtil.null2Blank(totalAmount));
		 errorRow.createCell(13).setCellValue(StringUtil.null2Blank(balance));
		 errorRow.createCell(14).setCellValue(StringUtil.null2Blank(status));
		 errorRow.createCell(15).setCellValue(StringUtil.null2Blank(message));
	}
	
	/**
	 * 
	 * @param errorSheet
	 * @param failCount
	 * @param name
	 * @param idCardNo
	 * @param message
	 */
	private void createErrorRow(XSSFSheet errorSheet, int failCount, String name, String idCardNo,  String message) {
		 XSSFRow errorRow = errorSheet.createRow(failCount);
		 errorRow.createCell(0).setCellValue(StringUtil.null2Blank(name));
		 errorRow.createCell(1).setCellValue(StringUtil.null2Blank(idCardNo));
		 errorRow.createCell(2).setCellValue(StringUtil.null2Blank(message));
	}
	
	@SuppressWarnings("static-access")
	private String getValue(XSSFCell cell) {
		if (null == cell) {
			return Constants.STR_NULL;
		}
		if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(cell.getNumericCellValue());
		} else {
			return String.valueOf(cell.getStringCellValue());
		}
	}
}
