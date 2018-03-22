package cn.itcast;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.core.bean.TestTb;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.service.TestTbService;

/**	
 * 测试类  junit + Spring 
 * @author lx
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestProduct {

	@Autowired
	private ProductDao productDao;
	
	@Test
	public void testAdd() throws Exception {
		//通过ID查询商品  
		
	/*	Product product = productDao.selectByPrimaryKey(441L);
		System.out.println(product);*/
		
		/**
		 * 条件 、分页  指定字段 查询  排序  
		 */
		ProductQuery productQuery = new ProductQuery();
		//productQuery.createCriteria().andBrandIdEqualTo(4L).andNameLike("%好莱坞%");
		//分页
		productQuery.setPageNo(2);
		productQuery.setPageSize(10);
		//排序
		productQuery.setOrderByClause("id desc");
		//指定字段 查询
		productQuery.setFields("id,brand_id");
		List<Product> products = productDao.selectByExample(productQuery);
		for (Product product : products) {
			System.err.println(product);
		}
		int countByExample = productDao.countByExample(productQuery);
		
//		productDao.in
		
		
		
	}
}
