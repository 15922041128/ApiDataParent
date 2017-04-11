package org.pbccrc.api.base.service;

import java.util.Map;

public interface HttpService {

	/**
	 * 
	 * @param Url
	 * @param Parms
	 * @return
	 */
	String RequestForm(String Url, Map<String,String> Parms);
}
