package org.pbccrc.api.base.bean;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class SendMsgRef implements Serializable {
	
	
	private static final long serialVersionUID = 7798963026419714121L;

	private String type;
	
	private String className;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
