package org.pbccrc.api.core.mapper;

import java.util.List;
import java.util.Map;

import org.pbccrc.api.base.bean.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMapper {

	/***
	 * 查询所有产品
	 * @return
	 */
	List<Map<String, Object>> queryAll();
	
	/**
	 * 根据产品类型获取产品集合
	 * @param productType
	 * @return
	 */
	List<Product> getProductByType(Product product);
	
	/**
	 * 更新产品
	 * @param product
	 */
	void updateProduct(Product product);
	
	/**
	 * 条件分页查询产品
	 * @param product
	 * @return
	 */
	List<Product> queryProductByPage(Product product);
	
	/**
	 * 新增产品
	 * @param product
	 */
	int addProduct(Product product);
	
}
