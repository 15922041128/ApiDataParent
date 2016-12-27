package org.pbccrc.api.base.bean;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class Code implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 6659470294076484221L;

	/** 主键 */
	private String id;
	
	/** code名称 */
	private String codeName;
	
	/** code值 */
	private String codeValue;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
	
}
