package org.pbccrc.api.base.bean;

import java.io.Serializable;

/**
 * 凭安黑名单查询
 * @author Administrator
 *
 */
public class TblPaBlackList implements Serializable {

	private static final long serialVersionUID = 239105393747742387L;

	/** 主键 */
	private int id;
	
	/** 姓名 */
	private String name;
	
	/** 身份证号 */
	private String idCard;
	
	/** 电话 */
	private String phone;
	
	/** imsi */
	private String imsi;
	
	/** imei */
	private String imei;
	
	/** 金融机构名 */
	private String orgName;
	
	/** 查询时间 */
	private String apiQueryDate;
	
	/** 返回数据 */
	private String returnValue;

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

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getApiQueryDate() {
		return apiQueryDate;
	}

	public void setApiQueryDate(String apiQueryDate) {
		this.apiQueryDate = apiQueryDate;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}
	
	
}
