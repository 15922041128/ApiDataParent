package org.pbccrc.api.base.bean;

import java.io.Serializable;

public class ProductType implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 808911728927307796L;

	/** 主键  */
	private int ID;
	
	/** 产品类型 */
	private int type;
	
	/** 产品类型名称 */
	private String typeName;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
}