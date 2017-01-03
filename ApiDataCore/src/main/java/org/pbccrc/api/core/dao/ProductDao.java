package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.Product;
import org.pbccrc.api.core.mapper.ProductMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductDao {

	@Resource
	private ProductMapper productMapper;
	
	public List<Map<String, Object>> queryAll() {
		return productMapper.queryAll();
	}
	
	public List<Product> getProductByType(String productType) {
		return productMapper.getProductByType(productType);
	}
}
