package org.ljx.service.product;

import com.github.pagehelper.PageInfo;
import org.ljx.entity.Product;
import org.ljx.entity.web.PageSearch;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
public interface ProductService {

    public List<Product> list(byte[] types);

    public void delete(int id);

    public void update(Product product) throws Exception;

    public void updateGoods(Product product) throws Exception;

    public void updateHalf(Product product) throws Exception;

    public Product findById(int id);

    public PageInfo list(PageSearch pageSearch, byte[] types);

    public void insertMaterial(Product product) throws Exception;

    public void insertGoods(Product product) throws Exception;

    public void insertHalf(Product product) throws Exception;

    public void insert(Product product) throws Exception;
}
