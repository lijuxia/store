package org.ljx.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ljx on 2018/5/15.
 */
@RestController(value = "product")
public class ProductController {

    @RequestMapping(value = "listYL",method = RequestMethod.GET)
    public String listYL(String name){
        return "你好！"+name;
    }
}
