package org.ljx.service.product;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.ljx.dao.ProductMapper;
import org.ljx.entity.Product;
import org.ljx.entity.web.PageSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper mapper;

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

    public void insert(Product product){
        product.setStatus(Product.STATUS_ON);
        mapper.insert(product);
    }
}
