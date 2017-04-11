package org.pbccrc.api.external.service.impl;

import java.util.Map;

import org.pbccrc.api.base.service.HttpService;
import org.pbccrc.api.base.util.HttpUtil;
import org.springframework.stereotype.Service;

@Service
public class HttpServiceImpl implements HttpService{

	/**
	 * 
	 * @param Url
	 * @param Parms
	 * @return
	 */
	public String RequestForm(String url, Map<String,String> Parms) {
		
		return HttpUtil.RequestForm(url, Parms);
	}
}
