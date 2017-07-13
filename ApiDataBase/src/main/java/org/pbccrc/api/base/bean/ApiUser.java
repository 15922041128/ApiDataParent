package org.pbccrc.api.base.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ApiUser implements Serializable{
	
	/** serialVersionUID */
	private static final long serialVersionUID = -6788132249424048096L;

	/** 主键 */
	private String id;
	
	/** 余额 */
	private BigDecimal blance;
	
	/** 信用额度 */
	private BigDecimal creditLimit;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getBlance() {
		return blance;
	}

	public void setBlance(BigDecimal blance) {
		this.blance = blance;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}
	
}
