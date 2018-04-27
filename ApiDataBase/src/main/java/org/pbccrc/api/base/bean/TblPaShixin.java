package org.pbccrc.api.base.bean;

import java.io.Serializable;

/**
 * 凭安失信被执行人
 * @author Administrator
 *
 */
public class TblPaShixin implements Serializable {


	private static final long serialVersionUID = 8207280652150678487L;

	/** 主键 */
	private int id;
	
	/** 姓名 */
	private String name;
	
	/** 身份证号 */
	private String idCard;
	
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
