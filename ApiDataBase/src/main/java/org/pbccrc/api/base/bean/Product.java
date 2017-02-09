package org.pbccrc.api.base.bean;

import java.io.Serializable;

public class Product implements Serializable{
	
	private static final long serialVersionUID = -6228787629678154122L;

	/** 主键  */
	private int id;
	
	/** 产品名称 */
	private String name;
	
	/** 产品图标url */
	private String image;
	
	/** 产品分类ID */
	private int type;
	
	/** 产品计费([次/元],次/元) */
	private String price;
	
	/** 产品包含apiID */
	private String apis;
	
	/** 产品状态(0待发布,1发布,2下架,3删除) */
	private String status;
	
	/** 产品是否可见(0不可见,1可见) */
	private int isShow;
	
	/** 初始赠送次数 -1为免费使用 */
	private int initCount;
	
	/** 初始赠送描述 */
	private String initNote;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getApis() {
		return apis;
	}

	public void setApis(String apis) {
		this.apis = apis;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	
	public int getInitCount() {
		return initCount;
	}

	public void setInitCount(int initCount) {
		this.initCount = initCount;
	}

	public String getInitNote() {
		return initNote;
	}

	public void setInitNote(String initNote) {
		this.initNote = initNote;
	}
}
