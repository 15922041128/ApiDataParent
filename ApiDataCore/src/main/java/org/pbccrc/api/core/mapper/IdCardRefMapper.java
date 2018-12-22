package org.pbccrc.api.core.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface IdCardRefMapper {

	/**
	 * 根据md5身份证号码查询身份证号码明文
	 * @param idCardMd5
	 * @return
	 */
	String getIdCard(String idCardMd5);
	
}
