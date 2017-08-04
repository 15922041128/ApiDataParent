package org.pbccrc.api.base.bean;

import java.io.Serializable;

public class Borrow implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -816793730163873351L;

	/** ID */
	private int id;
	
	/** 借款类型 */
	private String borrowType;
	
	/** 借款状态 */
	private String borrowState;
	
	/** 合同金额 */
	private int borrowAmount;
	
	/** 合同日期 */
	private String contractDate;
	
	/** 批贷期数 */
	private String loanPeriod;
	
	/** 还款状态 */
	private String repayState;
	
	/** 欠款金额 */
	private String arrearsAmount;
	
	/** 公司代码 */
	private String companyCode;
	
	/** 身份证号 */
	private String idCard;
	
	/** 姓名 */
	private String realName;
	

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBorrowType() {
		return borrowType;
	}

	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}

	public String getBorrowState() {
		return borrowState;
	}

	public void setBorrowState(String borrowState) {
		this.borrowState = borrowState;
	}

	public int getBorrowAmount() {
		return borrowAmount;
	}

	public void setBorrowAmount(int borrowAmount) {
		this.borrowAmount = borrowAmount;
	}

	public String getContractDate() {
		return contractDate;
	}

	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}

	public String getLoanPeriod() {
		return loanPeriod;
	}

	public void setLoanPeriod(String loanPeriod) {
		this.loanPeriod = loanPeriod;
	}

	public String getRepayState() {
		return repayState;
	}

	public void setRepayState(String repayState) {
		this.repayState = repayState;
	}

	public String getArrearsAmount() {
		return arrearsAmount;
	}

	public void setArrearsAmount(String arrearsAmount) {
		this.arrearsAmount = arrearsAmount;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

}
