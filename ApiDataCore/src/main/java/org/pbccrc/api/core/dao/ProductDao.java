package org.pbccrc.api.core.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pbccrc.api.base.bean.LocalApi;
import org.pbccrc.api.base.bean.Pagination;
import org.pbccrc.api.base.bean.Product;
import org.pbccrc.api.core.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

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
	
	/**
	 * 更新产品
	 * @param product
	 */
	public void updateProduct(Product product){
		productMapper.updateProduct(product);
	}
	
	/**
	 * 条件分页查询产品
	 * @param product
	 * @return
	 */
	public Pagination queryProductByPage(Product product, Pagination pagination){
		PageHelper.startPage(pagination.getCurrentPage(), pagination.getPageSize());
		Page<Product> products = (Page<Product>) productMapper.queryProductByPage(product);
		Pagination productsPagination = new Pagination();
		productsPagination.setResult(products.getResult());
		productsPagination.setTotalCount(products.getTotal());
		return productsPagination;
	}
	
	/**
	 * 新增产品
	 * @param product
	 */
	public int addProduct(Product product){
		return productMapper.addProduct(product);
	}
}
