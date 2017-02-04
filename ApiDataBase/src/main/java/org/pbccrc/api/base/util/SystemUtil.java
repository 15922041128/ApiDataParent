package org.pbccrc.api.base.util;

import javax.servlet.http.HttpServletRequest;

public class SystemUtil {

	/** 获取IP地址 */
	public static String getIpAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-real-ip");
		if (StringUtil.isNull(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}
}
