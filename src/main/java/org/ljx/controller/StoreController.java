package org.ljx.controller;

import org.ljx.entity.Store;
import org.ljx.entity.web.PageSearch;
import org.ljx.entity.web.ResponseMessage;
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
    public ResponseMessage find(@PathVariable int id){
        return success(storeService.findById(id));
    }

    @ResponseBody
    @RequestMapping(value = "addMD",method = RequestMethod.POST)
    public ResponseMessage addMD(Store store){
        storeService.insertMD(store);
        return success();
    }

    @ResponseBody
    @RequestMapping(value = "addZP",method = RequestMethod.POST)
    public ResponseMessage addZP(Store store){
        storeService.insertZP(store);
        return success();
    }

    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public ResponseMessage list(PageSearch pageSearch,byte type){
        return success(storeService.list(pageSearch,type));
    }

    @ResponseBody
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public ResponseMessage update(Store store){
        storeService.update(store);
        return success();
    }

    @ResponseBody
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    public ResponseMessage dalete(int id){
        storeService.delete(id);
        return success();
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("text","测试");
        return "/index";
    }

}
