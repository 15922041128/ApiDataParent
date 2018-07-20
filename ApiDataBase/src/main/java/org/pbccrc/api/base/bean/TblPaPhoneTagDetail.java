package org.pbccrc.api.base.bean;

import java.io.Serializable;

/**
 * 凭安电话标记详细记录
 * @author Administrator
 *
 */
public class TblPaPhoneTagDetail implements Serializable {

	private static final long serialVersionUID = 6482478201923282484L;

	/** 电话号码(md5) */
	private String phone;
	
	/** 号码标记信息  */
	private String tag;
	
	/** 标记次数 */
	private String tagTimes;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTagTimes() {
		return tagTimes;
	}

	public void setTagTimes(String tagTimes) {
		this.tagTimes = tagTimes;
	}

	
	
}
