package org.ljx.controller;

import org.ljx.entity.Store;
import org.ljx.entity.web.ResponseMessage;
import org.ljx.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ljx on 2018/5/29.
 */
@RestController
@RequestMapping
public class LoginController extends BaseController {

    @Autowired
    StoreService storeService;

    @RequestMapping(value = "login",method = RequestMethod.GET)
    public ResponseMessage login(String username,String password) throws Exception{
        Store store = storeService.login(username,password);
        getSession().setAttribute("user",store);
        return success();
    }

    @RequestMapping(value = "loginUser",method = RequestMethod.GET)
    public ResponseMessage loginUser(){
        return success(getCurrentStore());
    }
}
