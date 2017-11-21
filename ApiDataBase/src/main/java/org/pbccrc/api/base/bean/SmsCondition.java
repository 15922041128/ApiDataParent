package org.pbccrc.api.base.bean;

import java.io.Serializable;

public class SmsCondition implements Serializable{
	
	private static final long serialVersionUID = 1507049231307966555L;
	
	private String province;	//省
	private String city;	    //市
	private String operator;	//手机运营商
	private Integer age_max;    //年龄上限（不包含）
	private Integer age_min;	//年龄下限（包含）
	private String content;		//短信内容
	private String productType;	//产品类型
	private String productName;	//产品名称
	private Integer sendNum;	//发送条数
	private String smsTunnel;	//短信通道：壹信息/鼎汉/云信 sms_1xinxi、sms_dinghan、sms_yunxin
	private Integer pageNum;
	private Integer pageSize = 500;
	private String  seq ;		//发送接口调用ID
	
	
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
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Integer getAge_max() {
		return age_max;
	}
	public void setAge_max(Integer age_max) {
		this.age_max = age_max;
	}
	public Integer getAge_min() {
		return age_min;
	}
	public void setAge_min(Integer age_min) {
		this.age_min = age_min;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getSendNum() {
		return sendNum;
	}
	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}
	public String getSmsTunnel() {
		return smsTunnel;
	}
	public void setSmsTunnel(String smsTunnel) {
		this.smsTunnel = smsTunnel;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}

	

}
