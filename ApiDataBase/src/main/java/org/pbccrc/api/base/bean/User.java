package org.pbccrc.api.base.bean;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class User implements Serializable{

	private static final long serialVersionUID = -7313847131251988603L;

	/** 主键 */
	private Integer ID;
	
	/** 帐号 */
	private String userName;
	
	/** 密码 */
	private String password;
	
	/** 注册机构名称 */
	private String compName;
	
	/** 注册机构电话 */
	private String compTel;
	
	/** 联系人姓名 */
	private String contactName;
	
	/** 联系人电话 */
	private String contactTel;
	
	/** 认证标识 0否1是 */
	private String auth;
	
	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

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

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getCompTel() {
		return compTel;
	}

	public void setCompTel(String compTel) {
		this.compTel = compTel;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	
	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}
}
