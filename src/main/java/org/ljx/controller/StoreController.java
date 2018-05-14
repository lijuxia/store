package org.ljx.controller;

import org.ljx.entity.Store;
import org.ljx.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺
 * Created by ljx on 2018/5/14.
 */
@Controller
@RequestMapping("store")
public class StoreController extends BaseController{
    @Autowired
    StoreService storeService;

    @ResponseBody
    @RequestMapping(value = "find/{id}",method = RequestMethod.GET)
    public Store find(@PathVariable int id){
        return storeService.findById(id);
    }

    @ResponseBody
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public void add(Store store){
        storeService.insert(store);
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("text","测试");
        return "/index";
    }

}
