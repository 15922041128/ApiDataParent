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
	
	private String userName;
	
	private String password;
	
	private String channel;
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

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
