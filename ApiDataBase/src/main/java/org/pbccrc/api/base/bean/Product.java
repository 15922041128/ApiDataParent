package org.pbccrc.api.base.bean;

import java.io.Serializable;

public class Product implements Serializable{
	
	private static final long serialVersionUID = -6228787629678154122L;

	/** 主键  */
	private int ID;
	
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
	
	/** 产品状态(待发布,发布,下架) */
	private String status;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
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
}
