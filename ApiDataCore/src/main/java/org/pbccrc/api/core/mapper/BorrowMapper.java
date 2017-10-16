package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.Borrow;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowMapper {

	/**
	 * 查询下一个序列值
	 * @return
	 */
	String getNexSeq();
	
	/**
	 * @param borrow
	 */
	void addBorrows(List<Borrow> list);
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	void getCreditModel(Map<String, Object> map);
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	void getCreditModelTri(Map<String, Object> map);
}
