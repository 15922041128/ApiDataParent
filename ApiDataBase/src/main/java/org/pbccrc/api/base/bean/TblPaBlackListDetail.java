package org.pbccrc.api.base.bean;

import java.io.Serializable;

/**
 * 凭安黑名单详细记录
 * @author Administrator
 *
 */
public class TblPaBlackListDetail implements Serializable {

	private static final long serialVersionUID = 2872058082486708472L;

	/** 电话(md5) */
	private String phone;
	
	/** 银行失联 */
	private String bankLostContact;
	
	/** 机构失联 */
	private String orgLostContact;
	
	/** 机构逾期期数 */
	private String orgOverduePeriod;
	
	/** 最后一次严重逾期时间 */
	private String seriousOverdueTime;
	
	/** 银行逾期期数 */
	private String bankOverduePeriod;
	
	/** 机构诉讼(预设值,目前无值) */
	private String orgLitigation;
	
	/** 银行诉讼(预设值,目前无值) */
	private String bankLitigation;
	
	/** 最后一次催收电话的呼叫时间 */
	private String duntelCallTime;
	
	/** 开户30天有逾期(预设值,目前无值) */
	private String orgOneMonthOvedue;
	
	/** orgBlackList.size */
	private String orgBlackNum;
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBankLostContact() {
		return bankLostContact;
	}

	public void setBankLostContact(String bankLostContact) {
		this.bankLostContact = bankLostContact;
	}

	public String getOrgLostContact() {
		return orgLostContact;
	}

	public void setOrgLostContact(String orgLostContact) {
		this.orgLostContact = orgLostContact;
	}

	public String getOrgOverduePeriod() {
		return orgOverduePeriod;
	}

	public void setOrgOverduePeriod(String orgOverduePeriod) {
		this.orgOverduePeriod = orgOverduePeriod;
	}

	public String getSeriousOverdueTime() {
		return seriousOverdueTime;
	}

	public void setSeriousOverdueTime(String seriousOverdueTime) {
		this.seriousOverdueTime = seriousOverdueTime;
	}

	public String getBankOverduePeriod() {
		return bankOverduePeriod;
	}

	public void setBankOverduePeriod(String bankOverduePeriod) {
		this.bankOverduePeriod = bankOverduePeriod;
	}

	public String getOrgLitigation() {
		return orgLitigation;
	}

	public void setOrgLitigation(String orgLitigation) {
		this.orgLitigation = orgLitigation;
	}

	public String getBankLitigation() {
		return bankLitigation;
	}

	public void setBankLitigation(String bankLitigation) {
		this.bankLitigation = bankLitigation;
	}

	public String getDuntelCallTime() {
		return duntelCallTime;
	}

	public void setDuntelCallTime(String duntelCallTime) {
		this.duntelCallTime = duntelCallTime;
	}

	public String getOrgOneMonthOvedue() {
		return orgOneMonthOvedue;
	}

	public void setOrgOneMonthOvedue(String orgOneMonthOvedue) {
		this.orgOneMonthOvedue = orgOneMonthOvedue;
	}

	public String getOrgBlackNum() {
		return orgBlackNum;
	}

	public void setOrgBlackNum(String orgBlackNum) {
		this.orgBlackNum = orgBlackNum;
	}
	
}
