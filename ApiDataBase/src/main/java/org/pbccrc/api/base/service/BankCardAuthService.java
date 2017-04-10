package org.pbccrc.api.base.service;

import java.util.Map;

public interface BankCardAuthService {

	/**
	 * 认证
	 * @param verify_element     验证类型(三要素/四要素)
	 * @param id_holder			  姓名	
	 * @param id_card			  身份证号
	 * @param acc_no			  银行卡号
	 * @param mobile			  预留电话
	 * @param card_type			  卡类型(信用卡/储蓄卡)
	 * @param valid_date_year    卡有效期年
	 * @param valid_date_month   卡有效期月
	 * @param valid_no			   信用卡安全码
	 * @return
	 * @throws Exception
	 */
	Map<String, String> auth(String verify_element, String id_holder, String id_card, String acc_no, String mobile,
			String card_type, String valid_date_year, String valid_date_month, String valid_no) throws Exception;
}
