package org.pbccrc.api.base.bean;

import java.io.Serializable;

public class PersonLog implements Serializable{
	
	/** serialVersionUID */
	private static final long serialVersionUID = -6983648655112309745L;

	/** 主键 */
	private int ID;
	
	/** ip地址 */
	private String ipAddress;
	
	/** 用户ID */
	private String userID;
	
	/** 操作类型 */
	private String operatorType;
	
	/** 总操作数 */
	private String totalCount;
	
	/** 成功操作数 */
	private String successCount;
	
	/** 失败操作数 */
	private String failCount;
	
	/** 查询时间 */
	private String queryDate;
	
	/** 查询开始时间 */
	private String startDate;
	
	/** 查询结束时间 */
	private String endDate;
	
	/** 备注 */
	private String note;
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(String successCount) {
		this.successCount = successCount;
	}

	public String getFailCount() {
		return failCount;
	}

	public void setFailCount(String failCount) {
		this.failCount = failCount;
	}

	public String getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
