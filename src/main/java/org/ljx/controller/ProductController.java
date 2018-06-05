package org.ljx.controller;

import org.ljx.entity.Product;
import org.ljx.entity.web.PageSearch;
import org.ljx.entity.web.ResponseMessage;
import org.ljx.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ljx on 2018/5/15.
 */
@RestController
@RequestMapping("product")
public class ProductController extends BaseController {

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseMessage list(PageSearch pageSearch, Byte type){
        return success(productService.list(pageSearch,type));
    }


    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public ResponseMessage listAll(byte type){
        return success(productService.list(type));
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseMessage add(Product product){
        productService.insert(product);
        return success();
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseMessage update(Product product){
        productService.update(product);
        return success();
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseMessage dalete(int id){
        productService.delete(id);
        return success();
    }

    @RequestMapping(value = "/find/{id}",method = RequestMethod.GET)
    public ResponseMessage find(@PathVariable Integer id){
        return success(productService.findById(id));
    }
}
