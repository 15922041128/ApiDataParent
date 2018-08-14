package org.pbccrc.api.base.bean;

import java.io.Serializable;

public class QueryType implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -4933508227358166530L;
	
	/** 主键 */
	private int id;
	
	/** 查询类型1两标查询2电话号码查询 */
	private int typeCode;
	
	/** 查询类型名称 */
	private String typeName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
