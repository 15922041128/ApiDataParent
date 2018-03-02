package org.pbccrc.api.base.bean;

import java.io.Serializable;

/**
 * 短信发送记录
 * @author charles
 *
 */
public class SmsLog implements Serializable{

	/** serialVersionUID */
	private static final long serialVersionUID = -608342296352201188L;

	/** 与systemLog关联用uuid */
	private String uuid;
	
	/** 短信内容 */
	private String content;
	
	/** 号码*/
	private String numbers;
	
	/** 号码个数*/
	private Integer numCount;
	
	/** 查询参数 */
	private String params;
	
	/** 反馈 */
	private String feedBack;
	
	/** 短信平台ID */
	private String SmsTunnel;
	
	/** 发送状态 */
	private Integer sendState;
	
	public Integer getSendState() {
		return sendState;
	}

	public void setSendState(Integer sendState) {
		this.sendState = sendState;
	}

	/** 发送时间 */
	private String sendDate;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getSmsTunnel() {
		return SmsTunnel;
	}

	public void setSmsTunnel(String smsTunnel) {
		SmsTunnel = smsTunnel;
	}

	public Integer getNumCount() {
		return numCount;
	}

	public void setNumCount(Integer numCount) {
		this.numCount = numCount;
	}

}
