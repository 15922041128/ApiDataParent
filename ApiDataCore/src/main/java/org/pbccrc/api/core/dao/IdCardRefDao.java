package org.pbccrc.api.core.dao;

import javax.annotation.Resource;

import org.pbccrc.api.core.mapper.IdCardRefMapper;
import org.springframework.stereotype.Service;

@Service
public class IdCardRefDao {
	
	@Resource
	private IdCardRefMapper idCardRefMapper;
	
	/**
	 * 根据md5身份证号码查询身份证号码明文
	 * @param idCardMd5
	 * @return
	 */
	public String getIdCard(String idCardMd5) {
		return idCardRefMapper.getIdCard(idCardMd5);
	}

}
