package org.ljx.service.product;

import com.github.pagehelper.PageInfo;
import org.ljx.entity.Product;
import org.ljx.entity.web.PageSearch;

import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
public interface ProductService {

    public List<Product> list(byte type);

    public void delete(int id);

    public void update(Product product);

    public Product findById(int id);

    public PageInfo list(PageSearch pageSearch, byte type);

    public void insert(Product product);
}
