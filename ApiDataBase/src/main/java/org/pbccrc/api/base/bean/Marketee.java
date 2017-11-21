package org.pbccrc.api.base.bean;

import java.io.Serializable;
/**
 * 营销对象
 * @author charles
 *
 */
public class Marketee implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -816793730163873351L;

	/** ID */
	private int id;
	
	/** innerId */
	private String innerId;
	
	/** 电话号码 */
	private String telNum;
	
	/** 营销产品分类 1-现金贷  */
	private int xjd;
	
	/** 营销产品分类 2-小贷 */
	private int xd;
	
	/** 营销产品分类3-信用卡  */
	private int xyk;
	
	/** 营销产品分类 4-理财*/
	private int lc;
	
	/** 营销产品分类 5-保险 */
	private int bx;
	
	/** 年龄 */
	private String age;
	
	/** 地区 */
	private String area;
	
	/** 手机运营商*/
	private String operator;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInnerId() {
		return innerId;
	}

	public void setInnerId(String innerId) {
		this.innerId = innerId;
	}

	public String getTelNum() {
		return telNum;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getXjd() {
		return xjd;
	}

	public void setXjd(int xjd) {
		this.xjd = xjd;
	}

	public int getXd() {
		return xd;
	}

	public void setXd(int xd) {
		this.xd = xd;
	}

	public int getXyk() {
		return xyk;
	}

	public void setXyk(int xyk) {
		this.xyk = xyk;
	}

	public int getLc() {
		return lc;
	}

	public void setLc(int lc) {
		this.lc = lc;
	}

	public int getBx() {
		return bx;
	}

	public void setBx(int bx) {
		this.bx = bx;
	}


}
