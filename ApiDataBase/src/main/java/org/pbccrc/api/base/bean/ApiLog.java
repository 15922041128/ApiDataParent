package org.pbccrc.api.base.bean;

import java.io.Serializable;

public class ApiLog implements Serializable{

	/** serialVersionUID */
	private static final long serialVersionUID = -608342296352201188L;

	/** 主键 */
	private int ID;
	
	/** 与systemLog关联用uuid */
	private String uuid;
	
	/** 用户ID */
	private String userID;
	
	/** 本地apiID */
	private String localApiID;
	
	/** 查询参数 */
	private String params;
	
	/** 查询来源(本地/远程) */
	private String dataFrom;
	
	/** 查询是否成功 */
	private String isSuccess;
	
	/** 查询日期 */
	private String queryDate;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getUserID() {
		return userID;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}
}
