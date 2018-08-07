package org.pbccrc.api.base.bean;

import java.io.Serializable;

/**
 * 凭安逾期详细记录
 * 
 * @author Administrator
 *
 */
public class TblPaOverdueDetail implements Serializable {

	private static final long serialVersionUID = 1992388466976446255L;

	/** 电话号码(md5) */
	private String phone;

	/** M3_BankCredit_涉及机构数 */
	private String m3BankCreditOrgNums;

	/** M3_BankCredit_最长逾期时间 */
	private String m3BankCreditLongestDaysTime;

	/** M3_BankCredit_最长逾期天数 */
	private String m3BankCreditLongestDays;

	/** M3_BankCredit_逾期记录条数 */
	private String m3BankCreditRecordNums;

	/** M3_BankCredit_最大逾期金额 */
	private String m3BankCreditMaxAmount;

	/** M6_BankCredit_涉及机构数 */
	private String m6BankCreditOrgNums;

	/** M6_BankCredit_最长逾期时间 */
	private String m6BankCreditLongestDaysTime;

	/** M6_BankCredit_最长逾期天数 */
	private String m6BankCreditLongestDays;

	/** M6_BankCredit_逾期记录条数 */
	private String m6BankCreditRecordNums;

	/** M6_BankCredit_最大逾期金额 */
	private String m6BankCreditMaxAmount;

	/** M9_BankCredit_涉及机构数 */
	private String m9BankCreditOrgNums;

	/** M9_BankCredit_最长逾期时间 */
	private String m9BankCreditLongestDaysTime;

	/** M9_BankCredit_最长逾期天数 */
	private String m9BankCreditLongestDays;

	/** M9_BankCredit_逾期记录条数 */
	private String m9BankCreditRecordNums;

	/** M9_BankCredit_最大逾期金额 */
	private String m9BankCreditMaxAmount;

	/** M12_BankCredit_涉及机构数 */
	private String m12BankCreditOrgNums;

	/** M12_BankCredit_最长逾期时间 */
	private String m12BankCreditLongestDaysTime;

	/** M12_BankCredit_最长逾期天数 */
	private String m12BankCreditLongestDays;

	/** M12_BankCredit_逾期记录条数 */
	private String m12BankCreditRecordNums;

	/** M12_BankCredit_最大逾期金额 */
	private String m12BankCreditMaxAmount;

	/** M24_BankCredit_涉及机构数 */
	private String m24BankCreditOrgNums;

	/** M24_BankCredit_最长逾期时间 */
	private String m24BankCreditLongestDaysTime;

	/** M24_BankCredit_最长逾期天数 */
	private String m24BankCreditLongestDays;

	/** M24_BankCredit_逾期记录条数 */
	private String m24BankCreditRecordNums;

	/** M24_BankCredit_最大逾期金额 */
	private String m24BankCreditMaxAmount;

	/** M3_OtherLoan_涉及机构数 */
	private String m3OtherLoanOrgNums;

	/** M3_OtherLoan_最长逾期时间 */
	private String m3OtherLoanLongestDaysTime;

	/** M3_OtherLoan_逾期记录条数 */
	private String m3OtherLoanLongestDays;

	/** M3_OtherLoan_逾期记录条数 */
	private String m3OtherLoanRecordNums;

	/** M3_OtherLoan_最大逾期金额 */
	private String m3OtherLoanMaxAmount;

	/** M6_OtherLoan_涉及机构数 */
	private String m6OtherLoanOrgNums;

	/** M6_OtherLoan_最长逾期时间 */
	private String m6OtherLoanLongestDaysTime;

	/** M6_OtherLoan_逾期记录条数 */
	private String m6OtherLoanLongestDays;

	/** M6_OtherLoan_逾期记录条数 */
	private String m6OtherLoanRecordNums;

	/** M6_OtherLoan_最大逾期金额 */
	private String m6OtherLoanMaxAmount;

	/** M9_OtherLoan_涉及机构数 */
	private String m9OtherLoanOrgNums;

	/** M9_OtherLoan_最长逾期时间 */
	private String m9OtherLoanLongestDaysTime;

	/** M9_OtherLoan_逾期记录条数 */
	private String m9OtherLoanLongestDays;

	/** M9_OtherLoan_逾期记录条数 */
	private String m9OtherLoanRecordNums;

	/** M9_OtherLoan_最大逾期金额 */
	private String m9OtherLoanMaxAmount;

	/** M12_OtherLoan_涉及机构数 */
	private String m12OtherLoanOrgNums;

	/** M12_OtherLoan_最长逾期时间 */
	private String m12OtherLoanLongestDaysTime;

	/** M12_OtherLoan_逾期记录条数 */
	private String m12OtherLoanLongestDays;

	/** M12_OtherLoan_逾期记录条数 */
	private String m12OtherLoanRecordNums;

	/** M12_OtherLoan_最大逾期金额 */
	private String m12OtherLoanMaxAmount;

	/** M24_OtherLoan_涉及机构数 */
	private String m24OtherLoanOrgNums;

	/** M24_OtherLoan_最长逾期时间 */
	private String m24OtherLoanLongestDaysTime;

	/** M24_OtherLoan_逾期记录条数 */
	private String m24OtherLoanLongestDays;

	/** M24_OtherLoan_逾期记录条数 */
	private String m24OtherLoanRecordNums;

	/** M24_OtherLoan_最大逾期金额 */
	private String m24OtherLoanMaxAmount;

	/** M3_BankLoan_涉及机构数 */
	private String m3BankLoanOrgNums;

	/** M3_BankLoan_最长逾期时间 */
	private String m3BankLoanLongestDaysTime;

	/** M3_BankLoan_逾期记录条数 */
	private String m3BankLoanLongestDays;

	/** M3_BankLoan_逾期记录条数 */
	private String m3BankLoanRecordNums;

	/** M3_BankLoan_最大逾期金额 */
	private String m3BankLoanMaxAmount;

	/** M6_BankLoan_涉及机构数 */
	private String m6BankLoanOrgNums;

	/** M6_BankLoan_最长逾期时间 */
	private String m6BankLoanLongestDaysTime;

	/** M6_BankLoan_逾期记录条数 */
	private String m6BankLoanLongestDays;

	/** M6_BankLoan_逾期记录条数 */
	private String m6BankLoanRecordNums;

	/** M6_BankLoan_最大逾期金额 */
	private String m6BankLoanMaxAmount;

	/** M9_BankLoan_涉及机构数 */
	private String m9BankLoanOrgNums;

	/** M9_BankLoan_最长逾期时间 */
	private String m9BankLoanLongestDaysTime;

	/** M9_BankLoan_逾期记录条数 */
	private String m9BankLoanLongestDays;

	/** M9_BankLoan_逾期记录条数 */
	private String m9BankLoanRecordNums;

	/** M9_BankLoan_最大逾期金额 */
	private String m9BankLoanMaxAmount;

	/** M12_BankLoan_涉及机构数 */
	private String m12BankLoanOrgNums;

	/** M12_BankLoan_最长逾期时间 */
	private String m12BankLoanLongestDaysTime;

	/** M12_BankLoan_逾期记录条数 */
	private String m12BankLoanLongestDays;

	/** M12_BankLoan_逾期记录条数 */
	private String m12BankLoanRecordNums;

	/** M12_BankLoan_最大逾期金额 */
	private String m12BankLoanMaxAmount;

	/** M24_BankLoan_涉及机构数 */
	private String m24BankLoanOrgNums;

	/** M24_BankLoan_最长逾期时间 */
	private String m24BankLoanLongestDaysTime;

	/** M24_BankLoan_逾期记录条数 */
	private String m24BankLoanLongestDays;

	/** M24_BankLoan_逾期记录条数 */
	private String m24BankLoanRecordNums;

	/** M24_BankLoan_最大逾期金额 */
	private String m24BankLoanMaxAmount;

	/** M3_OtherCredit_涉及机构数 */
	private String m3OtherCreditOrgNums;

	/** M3_OtherCredit_最长逾期时间 */
	private String m3OtherCreditLongestDaysTime;

	/** M3_OtherCredit_逾期记录条数 */
	private String m3OtherCreditLongestDays;

	/** M3_OtherCredit_逾期记录条数 */
	private String m3OtherCreditRecordNums;

	/** M3_OtherCredit_最大逾期金额 */
	private String m3OtherCreditMaxAmount;

	/** M6_OtherCredit_涉及机构数 */
	private String m6OtherCreditOrgNums;

	/** M6_OtherCredit_最长逾期时间 */
	private String m6OtherCreditLongestDaysTime;

	/** M6_OtherCredit_逾期记录条数 */
	private String m6OtherCreditLongestDays;

	/** M6_OtherCredit_逾期记录条数 */
	private String m6OtherCreditRecordNums;

	/** M6_OtherCredit_最大逾期金额 */
	private String m6OtherCreditMaxAmount;

	/** M9_OtherCredit_涉及机构数 */
	private String m9OtherCreditOrgNums;

	/** M9_OtherCredit_最长逾期时间 */
	private String m9OtherCreditLongestDaysTime;

	/** M9_OtherCredit_逾期记录条数 */
	private String m9OtherCreditLongestDays;

	/** M9_OtherCredit_逾期记录条数 */
	private String m9OtherCreditRecordNums;

	/** M9_OtherCredit_最大逾期金额 */
	private String m9OtherCreditMaxAmount;

	/** M12_OtherCredit_涉及机构数 */
	private String m12OtherCreditOrgNums;

	/** M12_OtherCredit_最长逾期时间 */
	private String m12OtherCreditLongestDaysTime;

	/** M12_OtherCredit_逾期记录条数 */
	private String m12OtherCreditLongestDays;

	/** M12_OtherCredit_逾期记录条数 */
	private String m12OtherCreditRecordNums;

	/** M12_OtherCredit_最大逾期金额 */
	private String m12OtherCreditMaxAmount;

	/** M24_OtherCredit_涉及机构数 */
	private String m24OtherCreditOrgNums;

	/** M24_OtherCredit_最长逾期时间 */
	private String m24OtherCreditLongestDaysTime;

	/** M24_OtherCredit_逾期记录条数 */
	private String m24OtherCreditLongestDays;

	/** M24_OtherCredit_逾期记录条数 */
	private String m24OtherCreditRecordNums;

	/** M24_OtherCredit_最大逾期金额 */
	private String m24OtherCreditMaxAmount;
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getM3BankCreditOrgNums() {
		return m3BankCreditOrgNums;
	}

	public void setM3BankCreditOrgNums(String m3BankCreditOrgNums) {
		this.m3BankCreditOrgNums = m3BankCreditOrgNums;
	}

	public String getM3BankCreditLongestDaysTime() {
		return m3BankCreditLongestDaysTime;
	}

	public void setM3BankCreditLongestDaysTime(String m3BankCreditLongestDaysTime) {
		this.m3BankCreditLongestDaysTime = m3BankCreditLongestDaysTime;
	}

	public String getM3BankCreditLongestDays() {
		return m3BankCreditLongestDays;
	}

	public void setM3BankCreditLongestDays(String m3BankCreditLongestDays) {
		this.m3BankCreditLongestDays = m3BankCreditLongestDays;
	}

	public String getM3BankCreditRecordNums() {
		return m3BankCreditRecordNums;
	}

	public void setM3BankCreditRecordNums(String m3BankCreditRecordNums) {
		this.m3BankCreditRecordNums = m3BankCreditRecordNums;
	}

	public String getM3BankCreditMaxAmount() {
		return m3BankCreditMaxAmount;
	}

	public void setM3BankCreditMaxAmount(String m3BankCreditMaxAmount) {
		this.m3BankCreditMaxAmount = m3BankCreditMaxAmount;
	}

	public String getM6BankCreditOrgNums() {
		return m6BankCreditOrgNums;
	}

	public void setM6BankCreditOrgNums(String m6BankCreditOrgNums) {
		this.m6BankCreditOrgNums = m6BankCreditOrgNums;
	}

	public String getM6BankCreditLongestDaysTime() {
		return m6BankCreditLongestDaysTime;
	}

	public void setM6BankCreditLongestDaysTime(String m6BankCreditLongestDaysTime) {
		this.m6BankCreditLongestDaysTime = m6BankCreditLongestDaysTime;
	}

	public String getM6BankCreditLongestDays() {
		return m6BankCreditLongestDays;
	}

	public void setM6BankCreditLongestDays(String m6BankCreditLongestDays) {
		this.m6BankCreditLongestDays = m6BankCreditLongestDays;
	}

	public String getM6BankCreditRecordNums() {
		return m6BankCreditRecordNums;
	}

	public void setM6BankCreditRecordNums(String m6BankCreditRecordNums) {
		this.m6BankCreditRecordNums = m6BankCreditRecordNums;
	}

	public String getM6BankCreditMaxAmount() {
		return m6BankCreditMaxAmount;
	}

	public void setM6BankCreditMaxAmount(String m6BankCreditMaxAmount) {
		this.m6BankCreditMaxAmount = m6BankCreditMaxAmount;
	}

	public String getM9BankCreditOrgNums() {
		return m9BankCreditOrgNums;
	}

	public void setM9BankCreditOrgNums(String m9BankCreditOrgNums) {
		this.m9BankCreditOrgNums = m9BankCreditOrgNums;
	}

	public String getM9BankCreditLongestDaysTime() {
		return m9BankCreditLongestDaysTime;
	}

	public void setM9BankCreditLongestDaysTime(String m9BankCreditLongestDaysTime) {
		this.m9BankCreditLongestDaysTime = m9BankCreditLongestDaysTime;
	}

	public String getM9BankCreditLongestDays() {
		return m9BankCreditLongestDays;
	}

	public void setM9BankCreditLongestDays(String m9BankCreditLongestDays) {
		this.m9BankCreditLongestDays = m9BankCreditLongestDays;
	}

	public String getM9BankCreditRecordNums() {
		return m9BankCreditRecordNums;
	}

	public void setM9BankCreditRecordNums(String m9BankCreditRecordNums) {
		this.m9BankCreditRecordNums = m9BankCreditRecordNums;
	}

	public String getM9BankCreditMaxAmount() {
		return m9BankCreditMaxAmount;
	}

	public void setM9BankCreditMaxAmount(String m9BankCreditMaxAmount) {
		this.m9BankCreditMaxAmount = m9BankCreditMaxAmount;
	}

	public String getM12BankCreditOrgNums() {
		return m12BankCreditOrgNums;
	}

	public void setM12BankCreditOrgNums(String m12BankCreditOrgNums) {
		this.m12BankCreditOrgNums = m12BankCreditOrgNums;
	}

	public String getM12BankCreditLongestDaysTime() {
		return m12BankCreditLongestDaysTime;
	}

	public void setM12BankCreditLongestDaysTime(String m12BankCreditLongestDaysTime) {
		this.m12BankCreditLongestDaysTime = m12BankCreditLongestDaysTime;
	}

	public String getM12BankCreditLongestDays() {
		return m12BankCreditLongestDays;
	}

	public void setM12BankCreditLongestDays(String m12BankCreditLongestDays) {
		this.m12BankCreditLongestDays = m12BankCreditLongestDays;
	}

	public String getM12BankCreditRecordNums() {
		return m12BankCreditRecordNums;
	}

	public void setM12BankCreditRecordNums(String m12BankCreditRecordNums) {
		this.m12BankCreditRecordNums = m12BankCreditRecordNums;
	}

	public String getM12BankCreditMaxAmount() {
		return m12BankCreditMaxAmount;
	}

	public void setM12BankCreditMaxAmount(String m12BankCreditMaxAmount) {
		this.m12BankCreditMaxAmount = m12BankCreditMaxAmount;
	}

	public String getM24BankCreditOrgNums() {
		return m24BankCreditOrgNums;
	}

	public void setM24BankCreditOrgNums(String m24BankCreditOrgNums) {
		this.m24BankCreditOrgNums = m24BankCreditOrgNums;
	}

	public String getM24BankCreditLongestDaysTime() {
		return m24BankCreditLongestDaysTime;
	}

	public void setM24BankCreditLongestDaysTime(String m24BankCreditLongestDaysTime) {
		this.m24BankCreditLongestDaysTime = m24BankCreditLongestDaysTime;
	}

	public String getM24BankCreditLongestDays() {
		return m24BankCreditLongestDays;
	}

	public void setM24BankCreditLongestDays(String m24BankCreditLongestDays) {
		this.m24BankCreditLongestDays = m24BankCreditLongestDays;
	}

	public String getM24BankCreditRecordNums() {
		return m24BankCreditRecordNums;
	}

	public void setM24BankCreditRecordNums(String m24BankCreditRecordNums) {
		this.m24BankCreditRecordNums = m24BankCreditRecordNums;
	}

	public String getM24BankCreditMaxAmount() {
		return m24BankCreditMaxAmount;
	}

	public void setM24BankCreditMaxAmount(String m24BankCreditMaxAmount) {
		this.m24BankCreditMaxAmount = m24BankCreditMaxAmount;
	}

	public String getM3OtherLoanOrgNums() {
		return m3OtherLoanOrgNums;
	}

	public void setM3OtherLoanOrgNums(String m3OtherLoanOrgNums) {
		this.m3OtherLoanOrgNums = m3OtherLoanOrgNums;
	}

	public String getM3OtherLoanLongestDaysTime() {
		return m3OtherLoanLongestDaysTime;
	}

	public void setM3OtherLoanLongestDaysTime(String m3OtherLoanLongestDaysTime) {
		this.m3OtherLoanLongestDaysTime = m3OtherLoanLongestDaysTime;
	}

	public String getM3OtherLoanLongestDays() {
		return m3OtherLoanLongestDays;
	}

	public void setM3OtherLoanLongestDays(String m3OtherLoanLongestDays) {
		this.m3OtherLoanLongestDays = m3OtherLoanLongestDays;
	}

	public String getM3OtherLoanRecordNums() {
		return m3OtherLoanRecordNums;
	}

	public void setM3OtherLoanRecordNums(String m3OtherLoanRecordNums) {
		this.m3OtherLoanRecordNums = m3OtherLoanRecordNums;
	}

	public String getM3OtherLoanMaxAmount() {
		return m3OtherLoanMaxAmount;
	}

	public void setM3OtherLoanMaxAmount(String m3OtherLoanMaxAmount) {
		this.m3OtherLoanMaxAmount = m3OtherLoanMaxAmount;
	}

	public String getM6OtherLoanOrgNums() {
		return m6OtherLoanOrgNums;
	}

	public void setM6OtherLoanOrgNums(String m6OtherLoanOrgNums) {
		this.m6OtherLoanOrgNums = m6OtherLoanOrgNums;
	}

	public String getM6OtherLoanLongestDaysTime() {
		return m6OtherLoanLongestDaysTime;
	}

	public void setM6OtherLoanLongestDaysTime(String m6OtherLoanLongestDaysTime) {
		this.m6OtherLoanLongestDaysTime = m6OtherLoanLongestDaysTime;
	}

	public String getM6OtherLoanLongestDays() {
		return m6OtherLoanLongestDays;
	}

	public void setM6OtherLoanLongestDays(String m6OtherLoanLongestDays) {
		this.m6OtherLoanLongestDays = m6OtherLoanLongestDays;
	}

	public String getM6OtherLoanRecordNums() {
		return m6OtherLoanRecordNums;
	}

	public void setM6OtherLoanRecordNums(String m6OtherLoanRecordNums) {
		this.m6OtherLoanRecordNums = m6OtherLoanRecordNums;
	}

	public String getM6OtherLoanMaxAmount() {
		return m6OtherLoanMaxAmount;
	}

	public void setM6OtherLoanMaxAmount(String m6OtherLoanMaxAmount) {
		this.m6OtherLoanMaxAmount = m6OtherLoanMaxAmount;
	}

	public String getM9OtherLoanOrgNums() {
		return m9OtherLoanOrgNums;
	}

	public void setM9OtherLoanOrgNums(String m9OtherLoanOrgNums) {
		this.m9OtherLoanOrgNums = m9OtherLoanOrgNums;
	}

	public String getM9OtherLoanLongestDaysTime() {
		return m9OtherLoanLongestDaysTime;
	}

	public void setM9OtherLoanLongestDaysTime(String m9OtherLoanLongestDaysTime) {
		this.m9OtherLoanLongestDaysTime = m9OtherLoanLongestDaysTime;
	}

	public String getM9OtherLoanLongestDays() {
		return m9OtherLoanLongestDays;
	}

	public void setM9OtherLoanLongestDays(String m9OtherLoanLongestDays) {
		this.m9OtherLoanLongestDays = m9OtherLoanLongestDays;
	}

	public String getM9OtherLoanRecordNums() {
		return m9OtherLoanRecordNums;
	}

	public void setM9OtherLoanRecordNums(String m9OtherLoanRecordNums) {
		this.m9OtherLoanRecordNums = m9OtherLoanRecordNums;
	}

	public String getM9OtherLoanMaxAmount() {
		return m9OtherLoanMaxAmount;
	}

	public void setM9OtherLoanMaxAmount(String m9OtherLoanMaxAmount) {
		this.m9OtherLoanMaxAmount = m9OtherLoanMaxAmount;
	}

	public String getM12OtherLoanOrgNums() {
		return m12OtherLoanOrgNums;
	}

	public void setM12OtherLoanOrgNums(String m12OtherLoanOrgNums) {
		this.m12OtherLoanOrgNums = m12OtherLoanOrgNums;
	}

	public String getM12OtherLoanLongestDaysTime() {
		return m12OtherLoanLongestDaysTime;
	}

	public void setM12OtherLoanLongestDaysTime(String m12OtherLoanLongestDaysTime) {
		this.m12OtherLoanLongestDaysTime = m12OtherLoanLongestDaysTime;
	}

	public String getM12OtherLoanLongestDays() {
		return m12OtherLoanLongestDays;
	}

	public void setM12OtherLoanLongestDays(String m12OtherLoanLongestDays) {
		this.m12OtherLoanLongestDays = m12OtherLoanLongestDays;
	}

	public String getM12OtherLoanRecordNums() {
		return m12OtherLoanRecordNums;
	}

	public void setM12OtherLoanRecordNums(String m12OtherLoanRecordNums) {
		this.m12OtherLoanRecordNums = m12OtherLoanRecordNums;
	}

	public String getM12OtherLoanMaxAmount() {
		return m12OtherLoanMaxAmount;
	}

	public void setM12OtherLoanMaxAmount(String m12OtherLoanMaxAmount) {
		this.m12OtherLoanMaxAmount = m12OtherLoanMaxAmount;
	}

	public String getM24OtherLoanOrgNums() {
		return m24OtherLoanOrgNums;
	}

	public void setM24OtherLoanOrgNums(String m24OtherLoanOrgNums) {
		this.m24OtherLoanOrgNums = m24OtherLoanOrgNums;
	}

	public String getM24OtherLoanLongestDaysTime() {
		return m24OtherLoanLongestDaysTime;
	}

	public void setM24OtherLoanLongestDaysTime(String m24OtherLoanLongestDaysTime) {
		this.m24OtherLoanLongestDaysTime = m24OtherLoanLongestDaysTime;
	}

	public String getM24OtherLoanLongestDays() {
		return m24OtherLoanLongestDays;
	}

	public void setM24OtherLoanLongestDays(String m24OtherLoanLongestDays) {
		this.m24OtherLoanLongestDays = m24OtherLoanLongestDays;
	}

	public String getM24OtherLoanRecordNums() {
		return m24OtherLoanRecordNums;
	}

	public void setM24OtherLoanRecordNums(String m24OtherLoanRecordNums) {
		this.m24OtherLoanRecordNums = m24OtherLoanRecordNums;
	}

	public String getM24OtherLoanMaxAmount() {
		return m24OtherLoanMaxAmount;
	}

	public void setM24OtherLoanMaxAmount(String m24OtherLoanMaxAmount) {
		this.m24OtherLoanMaxAmount = m24OtherLoanMaxAmount;
	}

	public String getM3BankLoanOrgNums() {
		return m3BankLoanOrgNums;
	}

	public void setM3BankLoanOrgNums(String m3BankLoanOrgNums) {
		this.m3BankLoanOrgNums = m3BankLoanOrgNums;
	}

	public String getM3BankLoanLongestDaysTime() {
		return m3BankLoanLongestDaysTime;
	}

	public void setM3BankLoanLongestDaysTime(String m3BankLoanLongestDaysTime) {
		this.m3BankLoanLongestDaysTime = m3BankLoanLongestDaysTime;
	}

	public String getM3BankLoanLongestDays() {
		return m3BankLoanLongestDays;
	}

	public void setM3BankLoanLongestDays(String m3BankLoanLongestDays) {
		this.m3BankLoanLongestDays = m3BankLoanLongestDays;
	}

	public String getM3BankLoanRecordNums() {
		return m3BankLoanRecordNums;
	}

	public void setM3BankLoanRecordNums(String m3BankLoanRecordNums) {
		this.m3BankLoanRecordNums = m3BankLoanRecordNums;
	}

	public String getM3BankLoanMaxAmount() {
		return m3BankLoanMaxAmount;
	}

	public void setM3BankLoanMaxAmount(String m3BankLoanMaxAmount) {
		this.m3BankLoanMaxAmount = m3BankLoanMaxAmount;
	}

	public String getM6BankLoanOrgNums() {
		return m6BankLoanOrgNums;
	}

	public void setM6BankLoanOrgNums(String m6BankLoanOrgNums) {
		this.m6BankLoanOrgNums = m6BankLoanOrgNums;
	}

	public String getM6BankLoanLongestDaysTime() {
		return m6BankLoanLongestDaysTime;
	}

	public void setM6BankLoanLongestDaysTime(String m6BankLoanLongestDaysTime) {
		this.m6BankLoanLongestDaysTime = m6BankLoanLongestDaysTime;
	}

	public String getM6BankLoanLongestDays() {
		return m6BankLoanLongestDays;
	}

	public void setM6BankLoanLongestDays(String m6BankLoanLongestDays) {
		this.m6BankLoanLongestDays = m6BankLoanLongestDays;
	}

	public String getM6BankLoanRecordNums() {
		return m6BankLoanRecordNums;
	}

	public void setM6BankLoanRecordNums(String m6BankLoanRecordNums) {
		this.m6BankLoanRecordNums = m6BankLoanRecordNums;
	}

	public String getM6BankLoanMaxAmount() {
		return m6BankLoanMaxAmount;
	}

	public void setM6BankLoanMaxAmount(String m6BankLoanMaxAmount) {
		this.m6BankLoanMaxAmount = m6BankLoanMaxAmount;
	}

	public String getM9BankLoanOrgNums() {
		return m9BankLoanOrgNums;
	}

	public void setM9BankLoanOrgNums(String m9BankLoanOrgNums) {
		this.m9BankLoanOrgNums = m9BankLoanOrgNums;
	}

	public String getM9BankLoanLongestDaysTime() {
		return m9BankLoanLongestDaysTime;
	}

	public void setM9BankLoanLongestDaysTime(String m9BankLoanLongestDaysTime) {
		this.m9BankLoanLongestDaysTime = m9BankLoanLongestDaysTime;
	}

	public String getM9BankLoanLongestDays() {
		return m9BankLoanLongestDays;
	}

	public void setM9BankLoanLongestDays(String m9BankLoanLongestDays) {
		this.m9BankLoanLongestDays = m9BankLoanLongestDays;
	}

	public String getM9BankLoanRecordNums() {
		return m9BankLoanRecordNums;
	}

	public void setM9BankLoanRecordNums(String m9BankLoanRecordNums) {
		this.m9BankLoanRecordNums = m9BankLoanRecordNums;
	}

	public String getM9BankLoanMaxAmount() {
		return m9BankLoanMaxAmount;
	}

	public void setM9BankLoanMaxAmount(String m9BankLoanMaxAmount) {
		this.m9BankLoanMaxAmount = m9BankLoanMaxAmount;
	}

	public String getM12BankLoanOrgNums() {
		return m12BankLoanOrgNums;
	}

	public void setM12BankLoanOrgNums(String m12BankLoanOrgNums) {
		this.m12BankLoanOrgNums = m12BankLoanOrgNums;
	}

	public String getM12BankLoanLongestDaysTime() {
		return m12BankLoanLongestDaysTime;
	}

	public void setM12BankLoanLongestDaysTime(String m12BankLoanLongestDaysTime) {
		this.m12BankLoanLongestDaysTime = m12BankLoanLongestDaysTime;
	}

	public String getM12BankLoanLongestDays() {
		return m12BankLoanLongestDays;
	}

	public void setM12BankLoanLongestDays(String m12BankLoanLongestDays) {
		this.m12BankLoanLongestDays = m12BankLoanLongestDays;
	}

	public String getM12BankLoanRecordNums() {
		return m12BankLoanRecordNums;
	}

	public void setM12BankLoanRecordNums(String m12BankLoanRecordNums) {
		this.m12BankLoanRecordNums = m12BankLoanRecordNums;
	}

	public String getM12BankLoanMaxAmount() {
		return m12BankLoanMaxAmount;
	}

	public void setM12BankLoanMaxAmount(String m12BankLoanMaxAmount) {
		this.m12BankLoanMaxAmount = m12BankLoanMaxAmount;
	}

	public String getM24BankLoanOrgNums() {
		return m24BankLoanOrgNums;
	}

	public void setM24BankLoanOrgNums(String m24BankLoanOrgNums) {
		this.m24BankLoanOrgNums = m24BankLoanOrgNums;
	}

	public String getM24BankLoanLongestDaysTime() {
		return m24BankLoanLongestDaysTime;
	}

	public void setM24BankLoanLongestDaysTime(String m24BankLoanLongestDaysTime) {
		this.m24BankLoanLongestDaysTime = m24BankLoanLongestDaysTime;
	}

	public String getM24BankLoanLongestDays() {
		return m24BankLoanLongestDays;
	}

	public void setM24BankLoanLongestDays(String m24BankLoanLongestDays) {
		this.m24BankLoanLongestDays = m24BankLoanLongestDays;
	}

	public String getM24BankLoanRecordNums() {
		return m24BankLoanRecordNums;
	}

	public void setM24BankLoanRecordNums(String m24BankLoanRecordNums) {
		this.m24BankLoanRecordNums = m24BankLoanRecordNums;
	}

	public String getM24BankLoanMaxAmount() {
		return m24BankLoanMaxAmount;
	}

	public void setM24BankLoanMaxAmount(String m24BankLoanMaxAmount) {
		this.m24BankLoanMaxAmount = m24BankLoanMaxAmount;
	}

	public String getM3OtherCreditOrgNums() {
		return m3OtherCreditOrgNums;
	}

	public void setM3OtherCreditOrgNums(String m3OtherCreditOrgNums) {
		this.m3OtherCreditOrgNums = m3OtherCreditOrgNums;
	}

	public String getM3OtherCreditLongestDaysTime() {
		return m3OtherCreditLongestDaysTime;
	}

	public void setM3OtherCreditLongestDaysTime(String m3OtherCreditLongestDaysTime) {
		this.m3OtherCreditLongestDaysTime = m3OtherCreditLongestDaysTime;
	}

	public String getM3OtherCreditLongestDays() {
		return m3OtherCreditLongestDays;
	}

	public void setM3OtherCreditLongestDays(String m3OtherCreditLongestDays) {
		this.m3OtherCreditLongestDays = m3OtherCreditLongestDays;
	}

	public String getM3OtherCreditRecordNums() {
		return m3OtherCreditRecordNums;
	}

	public void setM3OtherCreditRecordNums(String m3OtherCreditRecordNums) {
		this.m3OtherCreditRecordNums = m3OtherCreditRecordNums;
	}

	public String getM3OtherCreditMaxAmount() {
		return m3OtherCreditMaxAmount;
	}

	public void setM3OtherCreditMaxAmount(String m3OtherCreditMaxAmount) {
		this.m3OtherCreditMaxAmount = m3OtherCreditMaxAmount;
	}

	public String getM6OtherCreditOrgNums() {
		return m6OtherCreditOrgNums;
	}

	public void setM6OtherCreditOrgNums(String m6OtherCreditOrgNums) {
		this.m6OtherCreditOrgNums = m6OtherCreditOrgNums;
	}

	public String getM6OtherCreditLongestDaysTime() {
		return m6OtherCreditLongestDaysTime;
	}

	public void setM6OtherCreditLongestDaysTime(String m6OtherCreditLongestDaysTime) {
		this.m6OtherCreditLongestDaysTime = m6OtherCreditLongestDaysTime;
	}

	public String getM6OtherCreditLongestDays() {
		return m6OtherCreditLongestDays;
	}

	public void setM6OtherCreditLongestDays(String m6OtherCreditLongestDays) {
		this.m6OtherCreditLongestDays = m6OtherCreditLongestDays;
	}

	public String getM6OtherCreditRecordNums() {
		return m6OtherCreditRecordNums;
	}

	public void setM6OtherCreditRecordNums(String m6OtherCreditRecordNums) {
		this.m6OtherCreditRecordNums = m6OtherCreditRecordNums;
	}

	public String getM6OtherCreditMaxAmount() {
		return m6OtherCreditMaxAmount;
	}

	public void setM6OtherCreditMaxAmount(String m6OtherCreditMaxAmount) {
		this.m6OtherCreditMaxAmount = m6OtherCreditMaxAmount;
	}

	public String getM9OtherCreditOrgNums() {
		return m9OtherCreditOrgNums;
	}

	public void setM9OtherCreditOrgNums(String m9OtherCreditOrgNums) {
		this.m9OtherCreditOrgNums = m9OtherCreditOrgNums;
	}

	public String getM9OtherCreditLongestDaysTime() {
		return m9OtherCreditLongestDaysTime;
	}

	public void setM9OtherCreditLongestDaysTime(String m9OtherCreditLongestDaysTime) {
		this.m9OtherCreditLongestDaysTime = m9OtherCreditLongestDaysTime;
	}

	public String getM9OtherCreditLongestDays() {
		return m9OtherCreditLongestDays;
	}

	public void setM9OtherCreditLongestDays(String m9OtherCreditLongestDays) {
		this.m9OtherCreditLongestDays = m9OtherCreditLongestDays;
	}

	public String getM9OtherCreditRecordNums() {
		return m9OtherCreditRecordNums;
	}

	public void setM9OtherCreditRecordNums(String m9OtherCreditRecordNums) {
		this.m9OtherCreditRecordNums = m9OtherCreditRecordNums;
	}

	public String getM9OtherCreditMaxAmount() {
		return m9OtherCreditMaxAmount;
	}

	public void setM9OtherCreditMaxAmount(String m9OtherCreditMaxAmount) {
		this.m9OtherCreditMaxAmount = m9OtherCreditMaxAmount;
	}

	public String getM12OtherCreditOrgNums() {
		return m12OtherCreditOrgNums;
	}

	public void setM12OtherCreditOrgNums(String m12OtherCreditOrgNums) {
		this.m12OtherCreditOrgNums = m12OtherCreditOrgNums;
	}

	public String getM12OtherCreditLongestDaysTime() {
		return m12OtherCreditLongestDaysTime;
	}

	public void setM12OtherCreditLongestDaysTime(String m12OtherCreditLongestDaysTime) {
		this.m12OtherCreditLongestDaysTime = m12OtherCreditLongestDaysTime;
	}

	public String getM12OtherCreditLongestDays() {
		return m12OtherCreditLongestDays;
	}

	public void setM12OtherCreditLongestDays(String m12OtherCreditLongestDays) {
		this.m12OtherCreditLongestDays = m12OtherCreditLongestDays;
	}

	public String getM12OtherCreditRecordNums() {
		return m12OtherCreditRecordNums;
	}

	public void setM12OtherCreditRecordNums(String m12OtherCreditRecordNums) {
		this.m12OtherCreditRecordNums = m12OtherCreditRecordNums;
	}

	public String getM12OtherCreditMaxAmount() {
		return m12OtherCreditMaxAmount;
	}

	public void setM12OtherCreditMaxAmount(String m12OtherCreditMaxAmount) {
		this.m12OtherCreditMaxAmount = m12OtherCreditMaxAmount;
	}

	public String getM24OtherCreditOrgNums() {
		return m24OtherCreditOrgNums;
	}

	public void setM24OtherCreditOrgNums(String m24OtherCreditOrgNums) {
		this.m24OtherCreditOrgNums = m24OtherCreditOrgNums;
	}

	public String getM24OtherCreditLongestDaysTime() {
		return m24OtherCreditLongestDaysTime;
	}

	public void setM24OtherCreditLongestDaysTime(String m24OtherCreditLongestDaysTime) {
		this.m24OtherCreditLongestDaysTime = m24OtherCreditLongestDaysTime;
	}

	public String getM24OtherCreditLongestDays() {
		return m24OtherCreditLongestDays;
	}

	public void setM24OtherCreditLongestDays(String m24OtherCreditLongestDays) {
		this.m24OtherCreditLongestDays = m24OtherCreditLongestDays;
	}

	public String getM24OtherCreditRecordNums() {
		return m24OtherCreditRecordNums;
	}

	public void setM24OtherCreditRecordNums(String m24OtherCreditRecordNums) {
		this.m24OtherCreditRecordNums = m24OtherCreditRecordNums;
	}

	public String getM24OtherCreditMaxAmount() {
		return m24OtherCreditMaxAmount;
	}

	public void setM24OtherCreditMaxAmount(String m24OtherCreditMaxAmount) {
		this.m24OtherCreditMaxAmount = m24OtherCreditMaxAmount;
	}

}
