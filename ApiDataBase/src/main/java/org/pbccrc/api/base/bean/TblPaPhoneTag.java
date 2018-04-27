package org.pbccrc.api.base.bean;

import java.io.Serializable;

/**
 * 凭安电话标记
 * @author Administrator
 *
 */
public class TblPaPhoneTag implements Serializable {

	private static final long serialVersionUID = -8802827128008271807L;

	/** 主键 */
	private int id;
	
	/** 电话号码 */
	private String phone;
	
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
