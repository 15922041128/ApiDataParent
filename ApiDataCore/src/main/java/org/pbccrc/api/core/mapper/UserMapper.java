package org.pbccrc.api.core.mapper;

import org.pbccrc.api.base.bean.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

	Integer isExist(String userName);
	
	void addUser(User user);
	
	User login(User user);
	
	void updateUser(User user);
}
