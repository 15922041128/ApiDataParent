package org.pbccrc.api.base.service;

import java.util.List;

import org.pbccrc.api.base.bean.ProductType;

public interface ProductTypeService {

	/***
	 * 查询所有产品类型
	 * @return
	 */
	List<ProductType> queryAll();
	
}
