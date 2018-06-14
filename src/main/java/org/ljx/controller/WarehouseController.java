package org.ljx.controller;

import org.ljx.entity.Warehouse;
import org.ljx.entity.web.PageSearch;
import org.ljx.entity.web.ResponseMessage;
import org.ljx.service.warehouse.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
@RestController
@RequestMapping("/warehouse")
public class WarehouseController extends BaseController{

    @Autowired
    WarehouseService warehouseService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseMessage list(PageSearch pageSearch, Integer storeId,Byte status){
        return success(warehouseService.list(pageSearch,storeId,status));
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public ResponseMessage listAll(){

        List<Warehouse> list =  warehouseService.list(getCurrentStore().getId(),Warehouse.STATUS_ON);
        return success(list);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseMessage dalete(Integer id){
        warehouseService.delete(id);
        return success();
    }

    @RequestMapping(value = "/find/{id}",method = RequestMethod.GET)
    public ResponseMessage find(@PathVariable Integer id){
        return success(warehouseService.findById(id));
    }
}
