package org.ljx.controller;

import org.ljx.entity.Store;
import org.ljx.entity.WarehouseRecord;
import org.ljx.entity.web.PageSearch;
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

    @RequestMapping(value = "/listMain", method = RequestMethod.GET)
    public ResponseMessage listMain(){
        Store store = getCurrentStore();
        if(store.getType()==Store.TYPE_DISTRIBUTION_CENTRE){
            PageSearch pageSearch = new PageSearch(5,1);
            return success(warehouseRecordService.list(pageSearch,(byte)0,store.getId()));
        }else{
            PageSearch pageSearch = new PageSearch(2,1);
            return success(warehouseRecordService.list(pageSearch,WarehouseRecord.TYPE_SCRAP,store.getId()));
        }

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseMessage add(WarehouseRecord warehouseRecord){
        warehouseRecordService.insert(warehouseRecord);
        return success();
    }

    @RequestMapping(value = "/addBuy", method = RequestMethod.POST)
    public ResponseMessage addBuy(WarehouseRecord warehouseRecord){
        warehouseRecord.setStatus(WarehouseRecord.STATUS_ON);
        warehouseRecord.setType(WarehouseRecord.TYPE_BUY);
        warehouseRecord.setConfirmFlag(WarehouseRecord.CONFIRMFLAG_YES);
        warehouseRecord.setInOrOut(WarehouseRecord.INOROUT_IN);
        warehouseRecord.setStoreId(getCurrentStore().getId());
        warehouseRecord.setStoreName(getCurrentStore().getName());
        if(warehouseRecord.getRemark()==null){
            warehouseRecord.setRemark("");
        }
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
