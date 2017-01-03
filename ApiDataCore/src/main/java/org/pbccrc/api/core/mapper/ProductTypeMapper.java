package org.pbccrc.api.core.mapper;

import java.util.List;

import org.pbccrc.api.base.bean.ProductType;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeMapper {

	/***
	 * 查询所有产品类型
	 * @return
	 */
	List<ProductType> queryAll();
	
}
