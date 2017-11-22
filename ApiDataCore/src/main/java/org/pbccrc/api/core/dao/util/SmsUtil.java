package org.pbccrc.api.core.dao.util;

public class SmsUtil {
	public static SmsWorkQueue wq;
	static {
		wq = new SmsWorkQueue(2);// 2个工作线程  
	}

}
