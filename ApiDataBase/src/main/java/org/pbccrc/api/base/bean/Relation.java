package org.pbccrc.api.base.bean;

import java.io.Serializable;

public class Relation implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 8706759007270151944L;

	/** 主键 */
	private Integer ID;
	
	/** 用户ID */
	private String userID;
	
	/** apiKey */
	private String apiKey;
	
	/** 产品ID */
	private String productID;
	
	/** 剩余查询次数,-1为不限制 */
	private String count;

	/** 计费类型 0 count 1 price */
	private String costType;
	
	/** 该用户查询该api的价格 */
	private String price;
	
	/** 申请时间 */
	private String createDate;
	
	/** 允许IP*/
	private String allowIps;

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getAllowIps() {
		return allowIps;
	}

	public void setAllowIps(String allowIps) {
		this.allowIps = allowIps;
	}
}
