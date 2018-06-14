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

import java.math.BigDecimal;
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

    public List<Product> list(byte[] types){
        return mapper.list(types);
    }

    public void delete(int id){
        Product product = mapper.findById(id);
        if(product!=null){
            product.setStatus(Product.STATUS_OFF);
            mapper.update(product);
        }
    }

    public void update(Product product) throws Exception{
        Product product1 = mapper.findByCode(product.getCode());
        if(product1!=null && product1.getId()!=product.getId()){
            throw new Exception("产品编号：已存在！");
        }
        Product productOld = mapper.findById(product.getId());
        if(productOld!=null){
            productOld.setCode(product.getCode());
            productOld.setName(product.getName());
            productOld.setUnit(product.getUnit());
            productOld.setType(product.getType());
            mapper.update(productOld);
        }
    }

    @Transactional
    public void updateGoods(Product product) throws Exception{
        Product product1 = mapper.findByCode(product.getCode());
        if(product1!=null && product1.getId()!=product.getId()){
            throw new Exception("产品编号：已存在！");
        }
        Product productOld = mapper.findById(product.getId());
        if(productOld!=null){
            productOld.setCode(product.getCode());
            productOld.setName(product.getName());
            productOld.setUnit(product.getUnit());
            productOld.setType(product.getType());
            mapper.update(productOld);
        }
        for(int i=0;i<product.getDetails().size();i++){
            ProductDetail detail = product.getDetails().get(i);
            if(detail.getId()>0){//修改
                if(detail.getNum().compareTo(BigDecimal.ZERO)==1){
                    detail.setProductId(productOld.getId());
                    detail.setStatus(ProductDetail.STATUS_ON);
                    detailMapper.update(detail);
                }else{
                    detailMapper.delete(detail.getId());
                }
            }else{//新增
                if(detail.getNum().compareTo(BigDecimal.ZERO)==1){
                    detail.setProductId(productOld.getId());
                    detail.setStatus(ProductDetail.STATUS_ON);
                    detailMapper.insert(detail);
                }
            }
        }
    }

    @Transactional
    public void updateHalf(Product product) throws Exception{
        Product product1 = mapper.findByCode(product.getCode());
        if(product1!=null && product1.getId()!=product.getId()){
            throw new Exception("产品编号：已存在！");
        }
        Product productOld = mapper.findById(product.getId());
        if(productOld!=null){
            productOld.setCode(product.getCode());
            productOld.setName(product.getName());
            productOld.setUnit(product.getUnit());
            productOld.setType(product.getType());
            mapper.update(productOld);
        }
        for(int i=0;i<product.getDetails().size();i++){
            ProductDetail detail = product.getDetails().get(i);
            if(detail.getId()>0){//修改
                if(detail.getNum().compareTo(BigDecimal.ZERO)==1){
                    detail.setProductId(productOld.getId());
                    detail.setStatus(ProductDetail.STATUS_ON);
                    detailMapper.update(detail);
                }else{
                    detailMapper.delete(detail.getId());
                }
            }else{//新增
                if(detail.getNum().compareTo(BigDecimal.ZERO)==1){
                    detail.setProductId(productOld.getId());
                    detail.setStatus(ProductDetail.STATUS_ON);
                    detailMapper.insert(detail);
                }
            }
        }
    }

    public Product findById(int id){
        return mapper.findById(id);
    }

    public PageInfo list(PageSearch pageSearch,byte[] types){
        PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return new PageInfo(mapper.list(types));
    }

    public void insertMaterial(Product product) throws Exception{
        Product product1 = mapper.findByCode(product.getCode());
        if(product1!=null){
            throw new Exception("产品编号：已存在！");
        }
        product.setStatus(Product.STATUS_ON);
        product.setType(Product.TYPE_MATERIAL);
        mapper.insert(product);
    }

    @Transactional
    public void insertGoods(Product product) throws Exception{
        Product product1 = mapper.findByCode(product.getCode());
        if(product1!=null){
            throw new Exception("产品编号：已存在！");
        }
        product.setType(Product.TYPE_GOODS);
        product.setStatus(Product.STATUS_ON);
        mapper.insert(product);
        for(ProductDetail detail : product.getDetails()){
            if(detail.getNum().compareTo(BigDecimal.ZERO)==1){
                detail.setProductId(product.getId());
                detail.setStatus(ProductDetail.STATUS_ON);
                detailMapper.insert(detail);
            }
        }
    }

    @Transactional
    public void insertHalf(Product product) throws Exception{
        Product product1 = mapper.findByCode(product.getCode());
        if(product1!=null){
            throw new Exception("产品编号：已存在！");
        }
        product.setType(Product.TYPE_HALF);
        product.setStatus(Product.STATUS_ON);
        mapper.insert(product);
        for(ProductDetail detail : product.getDetails()){
            if(detail.getNum().compareTo(BigDecimal.ZERO)==1){
                detail.setProductId(product.getId());
                detail.setStatus(ProductDetail.STATUS_ON);
                detailMapper.insert(detail);
            }
        }
    }

    public void insert(Product product) throws Exception{
        Product product1 = mapper.findByCode(product.getCode());
        if(product1!=null){
            throw new Exception("产品编号：已存在！");
        }
        product.setStatus(Product.STATUS_ON);
        mapper.insert(product);
    }
}
