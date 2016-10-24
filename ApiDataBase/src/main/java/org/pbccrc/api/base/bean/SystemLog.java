package org.pbccrc.api.base.bean;

import java.io.Serializable;

public class SystemLog implements Serializable{
	
	private static final long serialVersionUID = 6798031777223836704L;

	/** 主键 */
	private int ID;
	
	/** ip地址 */
	private String ipAddress;
	
	/** 用户ID */
	private String userID;
	
	/** 本地apiID */
	private String localApiID;
	
	/** 查询参数 */
	private String params;
	
	/** 查询apiKey */
	private String apiKey;
	
	/** 是否成功 */
	private String isSuccess;
	
	/** 是否计费 */
	private String isCount;
	
	/** 查询时间 */
	private String queryDate;
	
	/** 查询开始时间 */
	private String startDate;
	
	/** 查询结束时间 */
	private String endDate;
	
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

	public String getLocalApiID() {
		return localApiID;
	}

	public void setLocalApiID(String localApiID) {
		this.localApiID = localApiID;
	}
	
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getIsCount() {
		return isCount;
	}

	public void setIsCount(String isCount) {
		this.isCount = isCount;
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
