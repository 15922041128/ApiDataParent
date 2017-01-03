package org.pbccrc.api.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.ProductType;
import org.pbccrc.api.core.mapper.ProductTypeMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeDao {

	@Resource
	private ProductTypeMapper productTypeMapper;
	
	public List<ProductType> queryAll() {
		return productTypeMapper.queryAll();
	}
}
