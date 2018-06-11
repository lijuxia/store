package org.ljx.controller;

import io.swagger.models.auth.In;
import org.ljx.entity.Store;
import org.ljx.entity.web.PageSearch;
import org.ljx.entity.web.ResponseMessage;
import org.ljx.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺
 * Created by ljx on 2018/5/14.
 */
@RestController
@RequestMapping("store")
public class StoreController extends BaseController{
    @Autowired
    StoreService storeService;

    @RequestMapping(value = "/find/{id}",method = RequestMethod.GET)
    public ResponseMessage find(@PathVariable Integer id){
        Store store = storeService.findById(id);
        store.setPassword("");
        return success(store);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseMessage add(Store store) throws Exception{
        if(store.getType()==Store.TYPE_DISTRIBUTION_CENTRE){
            storeService.insertZP(store);
        }else if(store.getType()==Store.TYPE_STORE){
            storeService.insertMD(store);
        }
        return success();
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ResponseMessage list(PageSearch pageSearch,Byte type){
        return success(storeService.list(pageSearch,type));
    }

    @RequestMapping(value = "/listAll",method = RequestMethod.GET)
    public ResponseMessage listAll(){
        return success(storeService.list());
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseMessage update(Store store) throws Exception{
        storeService.update(store);
        return success();
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseMessage dalete(Integer id){
        storeService.delete(id);
        return success();
    }

}
