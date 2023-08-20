package org.ljx.controller;

import org.ljx.entity.Product;
import org.ljx.entity.web.PageSearch;
import org.ljx.entity.web.ResponseMessage;
import org.ljx.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ljx on 2018/5/15.
 */
@RestController
@RequestMapping("product")
public class ProductController extends BaseController {

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseMessage list(PageSearch pageSearch, @RequestParam(value = "types[]",required = false,defaultValue = "0")byte[] types){
        return success(productService.list(pageSearch,types));
    }


    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public ResponseMessage listAll(@RequestParam(value = "types[]",required = false,defaultValue = "0")byte[] types){
        return success(productService.list(types));
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseMessage add(Product product) throws Exception{
        productService.insertMaterial(product);
        return success();
    }

    @RequestMapping(value = "/addGoods",method = RequestMethod.POST)
    public ResponseMessage addGoods(Product product) throws Exception{
        productService.insertGoods(product);
        return success();
    }

    @RequestMapping(value = "/addHalf",method = RequestMethod.POST)
    public ResponseMessage addHalf(Product product) throws Exception{
        productService.insertHalf(product);
        return success();
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseMessage update(Product product) throws Exception{
        productService.update(product);
        return success();
    }

    @RequestMapping(value = "/updateGoods",method = RequestMethod.POST)
    public ResponseMessage updateGoods(Product product) throws Exception{
        productService.updateGoods(product);
        return success();
    }

    @RequestMapping(value = "/updateHalf",method = RequestMethod.POST)
    public ResponseMessage updateHalfs(Product product) throws Exception{
        productService.updateGoods(product);
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
