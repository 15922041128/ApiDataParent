package org.pbccrc.api.core.service.impl;

import java.util.List;

import org.pbccrc.api.base.bean.ProductType;
import org.pbccrc.api.base.service.ProductTypeService;
import org.pbccrc.api.core.dao.ProductTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeServiceImpl implements ProductTypeService{
	
	@Autowired
	private ProductTypeDao productTypeDao;

	/***
	 * 查询所有产品类型
	 * @return
	 */
	public List<ProductType> queryAll() {
		return productTypeDao.queryAll();
	}
	
}
