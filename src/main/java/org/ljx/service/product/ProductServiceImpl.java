package org.ljx.service.product;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.ljx.dao.ProductDetailMapper;
import org.ljx.dao.ProductMapper;
import org.ljx.entity.Product;
import org.ljx.entity.ProductDetail;
import org.ljx.entity.web.PageSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper mapper;
    @Autowired
    ProductDetailMapper detailMapper;

    public List<Product> list(byte type){
        return mapper.list(type);
    }

    public void delete(int id){
        Product product = mapper.findById(id);
        if(product!=null){
            product.setStatus(Product.STATUS_OFF);
            mapper.update(product);
        }
    }

    public void update(Product product){
        Product productOld = mapper.findById(product.getId());
        if(productOld!=null){
            productOld.setCode(product.getCode());
            productOld.setName(product.getName());
            productOld.setUnit(product.getUnit());
            productOld.setType(product.getType());
            mapper.update(productOld);
        }
    }

    public Product findById(int id){
        return mapper.findById(id);
    }

    public PageInfo list(PageSearch pageSearch,byte type){
        PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return new PageInfo(mapper.list(type));
    }

    public void insertMaterial(Product product){
        product.setStatus(Product.STATUS_ON);
        product.setType(Product.TYPE_MATERIAL);
        mapper.insert(product);
    }

    @Transactional
    public void insertGoods(Product product){
        product.setType(Product.TYPE_GOODS);
        product.setStatus(Product.STATUS_ON);
        mapper.insert(product);
        for(ProductDetail detail : product.getDetails()){
            if(detail.getNum()>0){
                detail.setProductId(product.getId());
                detail.setStatus(ProductDetail.STATUS_ON);
                detailMapper.insert(detail);
            }
        }
    }

    public void insert(Product product){
        product.setStatus(Product.STATUS_ON);
        mapper.insert(product);
    }
}
