package org.pbccrc.api.base.service;

/** 短信服务接口 */
public interface SMSService {

	/**
	 * 向指定电话号码发送指定短信内容
	 * @param phoneNo		手机号
	 * @param vCode			验证码
	 * @return
	 * @throws Exception
	 */
	public void send(String phoneNo, String vCode) throws Exception;
}
