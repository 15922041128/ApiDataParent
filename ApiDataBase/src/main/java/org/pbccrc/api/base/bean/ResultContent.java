package org.pbccrc.api.base.bean;

import java.io.Serializable;

public class ResultContent implements Serializable{
	
	private static final long serialVersionUID = -5386256195485266333L;

	/** 错误代码 */
	private String code;
	
	/** 返回信息 */
	private String retMsg;

	/** 返回数据 */
	private Object retData;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public Object getRetData() {
		return retData;
	}

	public void setRetData(Object retData) {
		this.retData = retData;
	}
}