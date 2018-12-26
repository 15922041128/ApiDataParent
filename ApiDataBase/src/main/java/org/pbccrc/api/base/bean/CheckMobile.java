package org.pbccrc.api.base.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

public class CheckMobile implements Serializable{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 6055342713485030570L;

	/** 主键 */
	private String id;
	
	/** 电话号码 */
	private String mobile;
	
	/** 时间戳 */
	private String lastTime;
	
	/** 地区 */
	private String area;
	
	/** 手机运营商类型 */
	private String numberType;
	
	/** 检测结果0空号1实号2停机3库无4沉默号5风险号 */
	private String status;
	
	/** 是否计费0否1是 */
	private String chargesStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getNumberType() {
		return numberType;
	}

	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChargesStatus() {
		return chargesStatus;
	}

	public void setChargesStatus(String chargesStatus) {
		this.chargesStatus = chargesStatus;
	}
	
	public JSONObject toJson(){
		JSONObject object = new JSONObject();
		object.put("mobile", mobile);
		object.put("lastTime", lastTime);
		object.put("area", area);
		object.put("numberType", numberType);
		object.put("status", status);
//		object.put("chargesStatus", chargesStatus);
		return object;
	}
}
