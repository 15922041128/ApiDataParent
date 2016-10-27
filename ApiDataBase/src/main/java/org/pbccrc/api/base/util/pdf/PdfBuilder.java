package org.pbccrc.api.base.util.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import javax.servlet.http.HttpServletRequest;

import org.pbccrc.api.base.util.Constants;
import org.pbccrc.api.base.util.pdf.pubElements;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfBuilder {
	
	public static String txt2String() {
		File file = new File("D:/pdftest/1.txt");
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	public static void main(String[] args) throws Exception{
		String jsonString = txt2String();
		String outPath = "D:\\pdftest\\YingzeReport.PDF";
		new PdfBuilder().getPDF(jsonString, new String[]{"creditCard", "loan"}, outPath, null);
	}

	public String getPDF(String jsonString, String[] queryItems, String outPath, HttpServletRequest request) throws Exception{
		
//		String imagePath = "D:/pdftest/yingze.jpg";
//		String imagePath2 = "D:/pdftest/boder.jpg";
		String imagePath = request.getSession().getServletContext().getRealPath("/images/yingze.jpg");
		String imagePath2 = request.getSession().getServletContext().getRealPath("/images/boder.jpg");

		JSONObject obj = JSONObject.parseObject(jsonString);
		
		// 信用评分
		String score = obj.getString("score");
		
		String basePath = request.getSession().getServletContext().getRealPath(outPath);
//		String basePath = "D:/pdftest";
		
		File file = new File(basePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		String fileName = System.currentTimeMillis() + Constants.FILE_TYPE_PDF;

		basePath = basePath + File.separator + fileName;
		
		FileOutputStream out = new FileOutputStream(basePath);
		
		Document document = new Document(PageSize.A4, 10, 10, 20, 20);
		PdfWriter.getInstance(document, out);
		document.open();
		
		// 添加Header
		document.add(pubElements.Pheader(imagePath));

		// 添加信用评分
		PdfPTable table9 = new PdfPTable(3);
		int width9[] = { 30, 50, 20 };
		table9.setWidths(width9);
		PdfPCell cell91 = new PdfPCell(new Paragraph("", pubElements.FontChinese12));
		PdfPCell cell92 = new PdfPCell(new Paragraph("信用评分", pubElements.FontChinese16));
		PdfPCell cell93 = new PdfPCell(new Paragraph(score, pubElements.FontChinese18));
		cell92.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell92.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell93.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell93.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell91.setBorder(0);
		cell92.setBorder(0);
		cell93.setBorder(0);
		table9.addCell(cell91);
		table9.addCell(cell92);
		table9.addCell(cell93);
		document.add(table9);

		// 加入空行
		document.add(pubElements.emptyRow());
		
		// 遍历查询项(基本信息)
		PdfPTable obj2Table = null;
		boolean hasHead = false;
		for (String queryItem : queryItems) {
			// 基本信息
			if (Constants.ITEM_PERSON.equals(queryItem) ||
					Constants.ITEM_ADDRESS.equals(queryItem) ||
					Constants.ITEM_EMPLOYMENT.equals(queryItem)) {
				// 判断是否已添加头信息
				if (!hasHead) {
					// 添加基本信息块
					document.add(pubElements.PsubTitle("基本信息", pubElements.FontChinese16, imagePath2));
					document.add(pubElements.emptyRow());
					hasHead = true;
				}
				
				// 用户基本信息
				if (Constants.ITEM_PERSON.equals(queryItem)) {
					String[] personCnBase = {"姓名", "证件号码", "出生日期", "性别", "学历", "学位", "婚姻状况", "手机号码", "电子邮箱"};
					String[] personKeyBase = {"name", "idCardNo", "BIRTHDAY", "GENDER", "EDULEVEL", "EDUDEGREE", "MARRIAGE", "MOBILETEL", "EMAIL"};
					obj2Table = pubElements.obj2Table(obj2Table, obj.getJSONObject("person"), personCnBase, personKeyBase);
					continue;
				}
				
				// 职业信息
				if (Constants.ITEM_EMPLOYMENT.equals(queryItem)) {
					String[] employmentCnBase = {"职业", "行业", "开始工作年份", "职务", "单位名称", "年收入"};
					String[] employmentKeyBase = {"OCCUPATION", "INDUSTRY", "STARTYEAR", "DUTY", "COMPANY", "ANNUALINCOME"};
					obj2Table = pubElements.obj2Table(obj2Table, obj.getJSONObject("employment"), employmentCnBase, employmentKeyBase);
					continue;
				}
				
				// 居住信息
				if (Constants.ITEM_ADDRESS.equals(queryItem)) {
					String[] addressCnBase = {"居住信息", "邮政编码", "家庭住址"};
					String[] addressKeyBase = {"RESCONDITION", "RESZIP", "RESIDENCE"};
					obj2Table = pubElements.obj2Table(obj2Table, obj.getJSONObject("address"), addressCnBase, addressKeyBase);
					continue;
				}
			}
		}
		if (null != obj2Table) {
			document.add(obj2Table);	
		}
		
		// 遍历查询项(其他信息)
		for (int i = 0; i < queryItems.length; i++) {
			String queryItem = queryItems[i];
			// 信用卡信息
			if (Constants.ITEM_CREDITCARD.equals(queryItem)) {
//				document.add(pubElements.PsubTitle("信用卡信息", pubElements.FontChinese16, imagePath2));
//				document.add(pubElements.emptyRow());
//				String[] creditCardCnName = { "信用卡张数", "授信总额度", "最大授信额度", "本币卡最大使用额", "外币卡最大使用额","逾期总期数", "最大逾期期数", "最早用卡时间", "已销卡数" };
//				String[] creditCardKey = { "cards", "sumCreditLimit", "maxCreditLimit", "maxDebt", "foreignMaxDebt", "sumTermsPastDue", "maxTermsPastDue", "minCardDate", "closeCards" };
//				obj2Table = pubElements.obj2Table(null, obj.getJSONObject("creditCard"), creditCardCnName, creditCardKey);
//				document.add(obj2Table);
//				continue;
				document.add(pubElements.PsubTitle("信用卡信息", pubElements.FontChinese16, imagePath2));
				document.add(pubElements.emptyRow());
				
				PdfPTable headerTbl = new PdfPTable(2);
				headerTbl.setWidths(new int[]{10, 90});
				
				// 卡数
				PdfPCell cell = pubElements.createHeaderCell("卡数");
				headerTbl.addCell(cell);
				
				String[] num_Name = {"正常卡数", "未激活卡数", "销户卡数", "非正常卡数"};
				String[] num_Key = {"NOR_NUM", "INACTIVE_NUM", "CLOSE_NUM", "ABNOR_NUM"};
				PdfPTable tbl = pubElements.obj2Grid(null, true, obj.getJSONObject("creditCard"), num_Name, num_Key);
				
				String[] num_Name2 = {"正常外币卡数", "销户外币卡数", "非正常外币卡数", "发卡机构数"};
				String[] num_Key2 = {"NOR_FOREIGN_NUM", "CLOSE_FOREIGN_NUM", "ABNOR_FOREIGN_NUM", "ORG_NUM"};
				tbl = pubElements.obj2Grid(tbl, true, obj.getJSONObject("creditCard"), num_Name2, num_Key2);
				
				cell = new PdfPCell(tbl);
				pubElements.disableBorderSides(cell, new int[]{1, 2, 4, 8});
				headerTbl.addCell(cell);
				
				// 授信额度
				cell = pubElements.createHeaderCell("授信额度");
				headerTbl.addCell(cell);
				
				String[] grantCreditLimitName = {"在用卡总授信额度", "已销户总授信额度", "非正常卡总授信额度", "未激活卡总授信额度"};
				String[] grantCreditLimitKey = {"GRAN_INUSE_TOTAL", "GRAN_CLOES_TOTAL", "GRAN_ABNOR_TOTAL", "GRAN_INACT_TOTAL"};
				tbl = pubElements.obj2Grid(null, true, obj.getJSONObject("creditCard"), grantCreditLimitName, grantCreditLimitKey);
				
				String[] grantCreditLimitName2 = {"正常卡单卡最大授信额度", "已销户卡单卡最大授信额度", "非正常卡单卡最大授信额度", "未激活卡单卡最大授信额度"};
				String[] grantCreditLimitKey2 = {"GRAN_NOR_MAX", "GRAN_CLOSE_MAX", "GRAN_ABNOR_MAX", "GRAN_INACT_MAX"};
				tbl = pubElements.obj2Grid(tbl, true, obj.getJSONObject("creditCard"), grantCreditLimitName2, grantCreditLimitKey2);
				
				String[] grantCreditLimitName3 = {"正常卡单卡最小授信额度", "已销户卡单卡最小授信额度", "非正常卡单卡最小授信额度", "未激活卡单卡最小授信额度"};
				String[] grantCreditLimitKey3 = {"GRAN_NOR_MIN", "GRAN_CLOSE_MIN", "GRAN_ABNOR_MIN", "GRAN_INACT_MIN"};
				tbl = pubElements.obj2Grid(tbl, true, obj.getJSONObject("creditCard"), grantCreditLimitName3, grantCreditLimitKey3);
				
				cell = new PdfPCell(tbl);
				pubElements.disableBorderSides(cell, new int[]{1, 2, 4, 8});
				headerTbl.addCell(cell);
				
				// 使用额度
				cell = pubElements.createHeaderCell("使用额度");
				headerTbl.addCell(cell);
				
				String[] useLimitName = {"正常卡单卡最大使用额度", "已销户卡单卡最大使用额度", "正常外币单卡最大使用额度", "已销卡外币单卡最大使用额度"};
				String[] useLimitKey = {"USE_NOR_MAX", "USE_CLOSE_MAX", "USE_NOR_FOR_MAX", "USE_CLOSE_FOR_MAX"};
				tbl = pubElements.obj2Grid(null, true, obj.getJSONObject("creditCard"), useLimitName, useLimitKey);
				
				cell = new PdfPCell(tbl);
				pubElements.disableBorderSides(cell, new int[]{1, 2, 4, 8});
				headerTbl.addCell(cell);
				
				// 逾期
				cell = pubElements.createHeaderCell("逾期");
				headerTbl.addCell(cell);
				
				String[] overdueName = {"总逾期期数", "最大逾期期数", "逾期180天最大金额", "逾期180天总金额"};
				String[] overdueKey = {"OVERDUE_TOTAL_TIMES", "OVERDUE_MAX_TIMES", "OVERDUE_180_MAX", "OVERDUE_180_TOTAL"};
				tbl = pubElements.obj2Grid(null, true, obj.getJSONObject("creditCard"), overdueName, overdueKey);
				
				cell = new PdfPCell(tbl);
				pubElements.disableBorderSides(cell, new int[]{1, 2, 4, 8});
				headerTbl.addCell(cell);
				
				// 时间
				cell = pubElements.createHeaderCell("时间");
				headerTbl.addCell(cell);
				
				String[] timeName = {"历史最早用卡时间", "在用卡最早用卡时间"};
				String[] timeKey = {"EAR_HIS_TIME", "EAR_INUSE_TIME"};
				tbl = pubElements.obj2Grid(null, true, obj.getJSONObject("creditCard"), timeName, timeKey);
				
				cell = new PdfPCell(tbl);
				pubElements.disableBorderSides(cell, new int[]{1, 2, 4, 8});
				headerTbl.addCell(cell);
				
				// 将表格加入到PDF
				document.add(headerTbl);
				// 加入空行
				if (i != queryItems.length - 1) {
					document.add(pubElements.emptyRow());
				}
				continue;
			}
			// 贷款信息
			if (Constants.ITEM_LOAN.equals(queryItem)) {
//				// 添加贷款信息块
//				document.add(pubElements.PsubTitle("贷款信息", pubElements.FontChinese16, imagePath2));
//				document.add(pubElements.emptyRow());
//				// 添加贷款信息Grid
//				String[] loanCnGrid = { "编号", "贷款类型", "业务发生地", "机构", "贷款额度", "借款日期", "贷款到期日", "贷款状态" };
//				String[] loanDataKey = { "编号", "loanType", "areaName", "finance", "loanAmount", "dateOpened", "dateClosed", "accountStat" };
//				document.add(pubElements.obj2Grid(null, true, obj.getJSONArray("loan"), loanCnGrid, loanDataKey));
//				// 加入空行
//				document.add(pubElements.emptyRow());
//				continue;
				// 添加贷款信息块
				document.add(pubElements.PsubTitle("贷款信息", pubElements.FontChinese16, imagePath2));
				document.add(pubElements.emptyRow());
				
				PdfPTable headerTbl = new PdfPTable(2);
				headerTbl.setWidths(new int[]{10, 90});
				
				// 贷款数
				PdfPCell cell = pubElements.createHeaderCell("贷款数");
				headerTbl.addCell(cell);
				
				String[] numName = {"结清账户数", "未结清账户数", "机构数", "地区数", "贷款类型数"};
				String[] numKey = {"SETTLED_ACCOUNT_NUM", "UNSETTLED_ACCOUNT_NUM", "ORG_NUM", "AREA_NUM", "LOAN_TYPE_NUM"};
				PdfPTable tbl = pubElements.obj2Grid(null, true, obj.getJSONObject("loan"), numName, numKey);
				
				String[] numName2 = {"住房贷款笔数", "汽车贷款笔数", "个人经营性贷款笔数", "正常笔数", "逾期笔数"};
				String[] numKey2 = {"HOUSING_LOAN_NUM", "CAR_LOAN_NUM", "PERSONAL_BIZ_LOAN_NUM", "NOR_NUM", "OVERDUE_NUM"};
				tbl = pubElements.obj2Grid(tbl, true, obj.getJSONObject("loan"), numName2, numKey2);
				
				cell = new PdfPCell(tbl);
				pubElements.disableBorderSides(cell, new int[]{1, 2, 4, 8});
				headerTbl.addCell(cell);
				
				// 额度
				cell = pubElements.createHeaderCell("额度");
				headerTbl.addCell(cell);
				
				String[] limitName = {"贷款最大授信额度", "最大贷款额度", "结清贷款额度", "未结清贷款总额度"};
				String[] limitKey = {"MAX_GRAN_LIMIT", "MAX_LOAN_LIMIT", "SETTLED_LIMIT", "UNSETTLED_LIMIT"};
				tbl = pubElements.obj2Grid(null, true, obj.getJSONObject("loan"), limitName, limitKey);
				
				cell = new PdfPCell(tbl);
				pubElements.disableBorderSides(cell, new int[]{1, 2, 4, 8});
				headerTbl.addCell(cell);
				
				// 逾期
				cell = pubElements.createHeaderCell("逾期");
				headerTbl.addCell(cell);
				
				String[] overdueName = {"逾期180天贷款本金", "逾期180天贷款未付余额", "逾期180天单笔贷款本金", "逾期180天单笔贷款未付余额"};
				String[] overdueKey = {"OVERDUE_180_PRINCIPAL", "OVERDUE_180_UNPAID", "OVERDUE_180_SINGLE_PRINCIPAL", "OVERDUE_180_SINGLE_UNPAID"};
				tbl = pubElements.obj2Grid(null, true, obj.getJSONObject("loan"), overdueName, overdueKey);

				String[] overdueName2 = {"总逾期期数", "单笔最大逾期期数", "五级分类最严重状态", ""};
				String[] overdueKey2 = {"TOTAL_OVERDUE_NUM", "SINGLE_OVERDUE_MAX", "FIVE_CATEGORY_MOST_TYPE", ""};
				tbl = pubElements.obj2Grid(tbl, true, obj.getJSONObject("loan"), overdueName2, overdueKey2);
				
				cell = new PdfPCell(tbl);
				pubElements.disableBorderSides(cell, new int[]{1, 2, 4, 8});
				headerTbl.addCell(cell);
				
				// 时间
				cell = pubElements.createHeaderCell("时间");
				headerTbl.addCell(cell);
				
				String[] timeName = {"最早贷款时间", "最晚还贷时间"};
				String[] timeKey = {"EAR_LOAN_TIME", "LATEST_REPAY_TIME"};
				tbl = pubElements.obj2Grid(null, true, obj.getJSONObject("loan"), timeName, timeKey);

				cell = new PdfPCell(tbl);
				pubElements.disableBorderSides(cell, new int[]{1, 2, 4, 8});
				headerTbl.addCell(cell);
				
				// 将表格加入到PDF
				document.add(headerTbl);
				// 加入空行
				if (i != queryItems.length - 1) {
					document.add(pubElements.emptyRow());
				}
				continue;
			}
			// 担保信息
			if (Constants.ITEM_GUARANTEE.equals(queryItem)) { 
				// 添加担保信息块
				document.add(pubElements.PsubTitle("担保信息", pubElements.FontChinese16, imagePath2));
				document.add(pubElements.emptyRow());
				// 添加担保信息Grid
				String[] guaranteeCnGrid = { "编号", "担保金额", "业务状态", "担保方式", "发生机构", "发生时间"};
				String[] guaranteeDataKey = { "编号", "GUARANTEESUM", "GUARANTEESTAT", "GUARANTEEWAY", "FINANCE", "OCCURPYTIME"};
				document.add(pubElements.obj2Grid(null, true, obj.getJSONArray("guarantee"), guaranteeCnGrid, guaranteeDataKey));
				// 加入空行
				if (i != queryItems.length - 1) {
					document.add(pubElements.emptyRow());
				}
				continue;
			}
			// 公积金信息
			if (Constants.ITEM_GGJ.equals(queryItem)) {
				// 添加公积金信息块
				document.add(pubElements.PsubTitle("公积金信息", pubElements.FontChinese16, imagePath2));
				document.add(pubElements.emptyRow());
				// 添加担保信息Grid
				String[] ggjCnGrid = {"编号", "单位名称", "个人缴存比例%", "初缴日期", "单位缴存比例%", "开户日期", "缴存状态", "最近一次缴存日期", "月缴存额", "缴至日期"};
				String[] ggjDataKey = {"编号", "COMPANY", "OWNPERCENT", "FIRSTMONTH", "COPERCENT", "OPENDATE", "STATUS", "LASTDATE", "PAY", "TOMONTH"};
				document.add(pubElements.obj2Grid(null, true, obj.getJSONArray("gjj"), ggjCnGrid, ggjDataKey));
				// 加入空行
				if (i != queryItems.length - 1) {
					document.add(pubElements.emptyRow());
				}
				continue;
			}
			// 失信被执行人信息
			if (Constants.ITEM_SXR.equals(queryItem)) {
				// 添加失信被执行人信息块
				document.add(pubElements.PsubTitle("失信被执行人信息", pubElements.FontChinese16, imagePath2));
				document.add(pubElements.emptyRow());
				// 添加担保信息Grid
				String[] sxrCnGrid = {"编号", "执行法院", "省份", "案号", "被执行人的履行情况", "失信被执行人行为具体情形", "发布时间"};
				String[] sxrDataKey = {"编号", "COURT_NAME", "AREA_NAME", "CASE_CODE", "PERFORMANCE", "DISREPUT_TYPE_NAME", "PUBLISH_DATE"};
				document.add(pubElements.obj2Grid(null, true, obj.getJSONObject("sxr").getJSONArray("retData"), sxrCnGrid, sxrDataKey));
				// 加入空行
				if (i != queryItems.length - 1) {
					document.add(pubElements.emptyRow());
				}
				continue;
			}
			// 涉法涉诉信息
			if (Constants.ITEM_SFSS.equals(queryItem)) {
				// 涉法涉诉信息模块
				document.add(pubElements.PsubTitle("涉法涉诉信息", pubElements.FontChinese16, imagePath2));
				document.add(pubElements.emptyRow());
				// 添加涉法涉诉信息Grid(从执行公告中查询某人)
				String[] sfssCnGrid = {"编号", "案号", "涉法涉诉类型", "涉法涉诉时间", "法院名称", "案件状态"};
				String[] zxggDataKey = {"编号", "caseNo", "sfssType", "concludeTime", "fycourt", "caseStatus"};
				PdfPTable sfsxGrid = pubElements.obj2Grid(null, true, obj.getJSONArray("zxgg"), sfssCnGrid, zxggDataKey);
				
				String[] sxggDataKey = {"编号", "caseNo", "sfssType", "concludeTime", "fycourt", ""};
				sfsxGrid = pubElements.obj2Grid(sfsxGrid, false, obj.getJSONArray("sxgg"), sfssCnGrid, sxggDataKey);
				
				String[] cpwsDataKey = {"编号", "caseNo", "sfssType", "concludeTime", "fycourt", ""};
				sfsxGrid = pubElements.obj2Grid(sfsxGrid, false, obj.getJSONArray("cpws"), sfssCnGrid, cpwsDataKey);
				document.add(sfsxGrid);
				// 加入空行
				if (i != queryItems.length - 1) {
					document.add(pubElements.emptyRow());
				}
				continue;
			}
		}

		document.close();
		out.close();
		return fileName;
	}
}
