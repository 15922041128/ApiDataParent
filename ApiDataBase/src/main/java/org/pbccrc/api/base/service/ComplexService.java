package org.pbccrc.api.base.service;

import java.util.Map;

public interface ComplexService {
	
	/**
	 * PDF单项查询
	 * @param item
	 * @param name
	 * @param identifier
	 * @param localApi
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPdfItem(String uuid, String userID, String item, String name, String identifier, Map<String, Object> localApi) throws Exception;
	
	/**
	 * PDF自定义查询
	 * @param item
	 * @param name
	 * @param identifier
	 * @param localApi
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPdfCustom(String uuid, String userID, String name, String identifier, Map<String, Object> localApi) throws Exception;
	
	/**
	 * 失信人查询验证
	 * @param name
	 * @param identifier
	 * @return
	 */
	public String validateSxr(String name, String identifier) throws Exception;
	
	/**
	 * 失信人查询
	 * @param identifier
	 * @return
	 */
	public Map<String, Object> querySxr(String uuid, String userID, String name, String identifier, String[] queryItems) throws Exception;

}
