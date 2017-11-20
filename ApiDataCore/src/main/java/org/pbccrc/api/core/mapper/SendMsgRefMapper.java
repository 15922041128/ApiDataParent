package org.pbccrc.api.core.mapper;

import java.util.List;

import org.pbccrc.api.base.bean.SendMsgRef;
import org.springframework.stereotype.Repository;

@Repository
public interface SendMsgRefMapper {

	List<SendMsgRef> queryAll();
}
