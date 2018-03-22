package cn.itcast.core.service.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.ColorQuery;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.ProductQuery.Criteria;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.dao.product.ColorDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;

/**
 * 商品
 * 
 * @author lx
 *
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	// 分页对象
	public Pagination selectPaginationByQuery(Integer pageNo, String name, Long brandId, Boolean isShow) {
		ProductQuery productQuery = new ProductQuery();
		productQuery.setPageNo(Pagination.cpn(pageNo));
		// 排序
		productQuery.setOrderByClause("id desc");
		// productQuery.s
		Criteria createCriteria = productQuery.createCriteria();
		StringBuilder params = new StringBuilder();
		if (null != name) {
			createCriteria.andNameLike("%" + name + "%");
			params.append("name=").append(name);
		}
		if (null != brandId) {
			createCriteria.andBrandIdEqualTo(brandId);
			params.append("&brandId=").append(brandId);
		}
		if (null != isShow) {
			createCriteria.andIsShowEqualTo(isShow);
			params.append("&isShow=").append(isShow);
		} else {
			createCriteria.andIsShowEqualTo(false);
			params.append("&isShow=").append(false);
		}

		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(),
				productDao.countByExample(productQuery), productDao.selectByExample(productQuery));
		String url = "/product/list.do";

		pagination.pageView(url, params.toString());

		return pagination;
	}

	// 加载颜色
	@Autowired
	private ColorDao colorDao;

	// 颜色结果集
	public List<Color> selectColorList() {
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.createCriteria().andParentIdNotEqualTo(0L);
		return colorDao.selectByExample(colorQuery);
	}

	@Autowired
	private SkuDao skuDao;

	@Override
	public void insertProduct(Product product) {
		product.setIsShow(false);
		product.setIsDel(true);
		productDao.insertSelective(product);
		String[] colors = product.getColors().split(",");
		String[] sizes = product.getSizes().split(",");
		for (String color : colors) {
			for (String size : sizes) {
				Sku sku = new Sku();
				sku.setProductId(product.getId());
				sku.setColorId(Long.parseLong(color));
				sku.setMarketPrice(999f);
				sku.setPrice(666f);
				sku.setDeliveFee(8f);
				sku.setStock(0);
				sku.setUpperLimit(200);
				sku.setCreateTime(new Date());
				skuDao.insertSelective(sku);
			}
		}

	}

}
