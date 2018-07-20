package org.pbccrc.api.base.bean;

import java.io.Serializable;

/**
 * 凭安申请人属性详细记录
 * @author Administrator
 *
 */
public class TblPaScoreDetail implements Serializable {

	private static final long serialVersionUID = 5391543110014667451L;

	/** 电话号码(md5) */
	private String phone;
	
	/** 最近两年内乘坐火车出行次数 */
	private String cr_tr_tr_lm24;
	
	/** 最近信用卡三个月消费次数 */
	private String cr_cc_cs_lm03;
	
	/** 最近两年关机后的来电提醒次数 */
	private String cr_ps_mc_lm24;
	
	/** 最近一年持有的信用卡张数 */
	private String cd_cc_al_lm12;
	
	/** 最近一年持有的全部借记卡单笔支出超过2000元的次数 */
	private String cr_dc_ogo2_lm12;
	
	/** 最近两年内申请人号码对应的IMSI数 */
	private String cd_al_is_lm24;
	
	/** 最近半年收取的快递件数 */
	private String cr_ex_ep_lm06;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCr_tr_tr_lm24() {
		return cr_tr_tr_lm24;
	}

	public void setCr_tr_tr_lm24(String cr_tr_tr_lm24) {
		this.cr_tr_tr_lm24 = cr_tr_tr_lm24;
	}

	public String getCr_cc_cs_lm03() {
		return cr_cc_cs_lm03;
	}

	public void setCr_cc_cs_lm03(String cr_cc_cs_lm03) {
		this.cr_cc_cs_lm03 = cr_cc_cs_lm03;
	}

	public String getCr_ps_mc_lm24() {
		return cr_ps_mc_lm24;
	}

	public void setCr_ps_mc_lm24(String cr_ps_mc_lm24) {
		this.cr_ps_mc_lm24 = cr_ps_mc_lm24;
	}

	public String getCd_cc_al_lm12() {
		return cd_cc_al_lm12;
	}

	public void setCd_cc_al_lm12(String cd_cc_al_lm12) {
		this.cd_cc_al_lm12 = cd_cc_al_lm12;
	}

	public String getCr_dc_ogo2_lm12() {
		return cr_dc_ogo2_lm12;
	}

	public void setCr_dc_ogo2_lm12(String cr_dc_ogo2_lm12) {
		this.cr_dc_ogo2_lm12 = cr_dc_ogo2_lm12;
	}

	public String getCd_al_is_lm24() {
		return cd_al_is_lm24;
	}

	public void setCd_al_is_lm24(String cd_al_is_lm24) {
		this.cd_al_is_lm24 = cd_al_is_lm24;
	}

	public String getCr_ex_ep_lm06() {
		return cr_ex_ep_lm06;
	}

	public void setCr_ex_ep_lm06(String cr_ex_ep_lm06) {
		this.cr_ex_ep_lm06 = cr_ex_ep_lm06;
	}
	
}
