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
	
	/** 公司地址-省 */
	private String province;
	
	/** 公司地址-市 */
	private String city;
	
	/** 公司地址-区 */
	private String area;
	
	/** 公司地址-详细地址 */
	private String address;
	
	/** 营业执照编号 */
	private String license;
	
	/** 税务登记证编号 */
	private String taxRegCertify;
	
	/** 组织机构编号 */
	private String org;
	
	/** 营业执照副本(url) */
	private String licenseImg;
	
	/** 审核信息 */
	private String message;
	
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
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getTaxRegCertify() {
		return taxRegCertify;
	}

	public void setTaxRegCertify(String taxRegCertify) {
		this.taxRegCertify = taxRegCertify;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getLicenseImg() {
		return licenseImg;
	}

	public void setLicenseImg(String licenseImg) {
		this.licenseImg = licenseImg;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
