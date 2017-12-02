package org.pbccrc.api.core.dao.util;

public class SmsUtil {
	public static SmsWorkQueue wq;
	static {
		wq = new SmsWorkQueue(1);// 工作线程  
	}

}
