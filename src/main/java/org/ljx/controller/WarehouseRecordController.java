package org.ljx.controller;

import org.ljx.entity.WarehouseRecord;
import org.ljx.entity.web.ResponseMessage;
import org.ljx.service.warehouseRecord.WarehouseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ljx on 2018/5/29.
 */
@RestController
@RequestMapping("warehouserecord")
public class WarehouseRecordController extends BaseController {

    @Autowired
    WarehouseRecordService warehouseRecordService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseMessage list(){
        return success(warehouseRecordService.list());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseMessage add(WarehouseRecord warehouseRecord){
        warehouseRecordService.insert(warehouseRecord);
        return success();
    }

    @RequestMapping(value = "delete",method = RequestMethod.POST)
    public ResponseMessage delete(String oddId){
        warehouseRecordService.delete(oddId);
        return success();
    }

    @RequestMapping(value = "update",method = RequestMethod.POST)
    public ResponseMessage update(WarehouseRecord warehouseRecord){
        warehouseRecordService.update(warehouseRecord);
        return success();
    }

    @RequestMapping(value = "/find/{oddId}",method = RequestMethod.GET)
    public ResponseMessage find(@PathVariable String oddId){
        return success(warehouseRecordService.findById(oddId));
    }

}
