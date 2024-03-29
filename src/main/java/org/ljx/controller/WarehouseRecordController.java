package org.ljx.controller;

import org.ljx.entity.Store;
import org.ljx.entity.WarehouseRecord;
import org.ljx.entity.web.PageSearch;
import org.ljx.entity.web.ResponseMessage;
import org.ljx.service.store.StoreService;
import org.ljx.service.warehouseRecord.WarehouseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
@RestController
@RequestMapping("warehouserecord")
public class WarehouseRecordController extends BaseController {

    @Autowired
    WarehouseRecordService warehouseRecordService;
    @Autowired
    StoreService storeService;

    @RequestMapping(value = "/listMain", method = RequestMethod.GET)
    public ResponseMessage listMain(){
        Store store = getCurrentStore();
        PageSearch pageSearch = new PageSearch(2,1);
        List<WarehouseRecord> listSend = warehouseRecordService.list(pageSearch,
                new byte[]{WarehouseRecord.TYPE_SEND},
                store.getId(),"creatTime desc");
        pageSearch = new PageSearch(4,1);
        List<WarehouseRecord> listOther = warehouseRecordService.list(pageSearch,
                new byte[]{WarehouseRecord.TYPE_CHECK,WarehouseRecord.TYPE_MAKE,WarehouseRecord.TYPE_BUY,WarehouseRecord.TYPE_SCRAP,WarehouseRecord.TYPE_SALE,WarehouseRecord.TYPE_CONSUME},
                store.getId(),"creatTime desc");
        listSend.addAll(listOther);
        return success(listSend);
    }

    @RequestMapping(value = "/listPull", method = RequestMethod.GET)
    public ResponseMessage listPull(String oddId, byte type, Date beginDate, Date endDate,Integer sendStoreId){
        Timestamp time = null;
        int storeId = 0;
        WarehouseRecord record = warehouseRecordService.findById(oddId);
        if(record!=null){
            time = record.getCreatTime();
        }
        PageSearch pageSearch = new PageSearch(100,1);
//        if(getCurrentStore().getType()==Store.TYPE_STORE){
            storeId = getCurrentStore().getId();
//        }
        byte[] types = new byte[]{};
        if(type > 0){
            types = new byte[]{type};
        }
        return success(warehouseRecordService.list(pageSearch,types,storeId,beginDate,endDate,time,null,sendStoreId,"creatTime desc"));
    }

    @RequestMapping(value = "/listPullUp", method = RequestMethod.GET)
    public ResponseMessage listPullUp(String oddId, byte type, Date beginDate, Date endDate,Integer sendStoreId){
        Timestamp time = null;
        int storeId = 0;
        WarehouseRecord record = warehouseRecordService.findById(oddId);
        if(record!=null){
            time = record.getCreatTime();
        }
        PageSearch pageSearch = new PageSearch(100,1);
//        if(getCurrentStore().getType()==Store.TYPE_STORE){
            storeId = getCurrentStore().getId();
//        }
        byte[] types = new byte[]{};
        if(type > 0){
            types = new byte[]{type};
        }
        return success(warehouseRecordService.list(pageSearch,types,storeId,beginDate,endDate,null,time,sendStoreId,"creatTime desc"));
    }


    @RequestMapping(value = "/reflesh", method = RequestMethod.GET)
    public ResponseMessage reflesh(String oddId){
        warehouseRecordService.reflesh(getCurrentStore().getId());
        return success();
    }

    @RequestMapping(value = "/addConsume", method = RequestMethod.POST)
    public ResponseMessage addConsume(WarehouseRecord warehouseRecord) throws Exception{
        warehouseRecord.setStatus(WarehouseRecord.STATUS_ON);
        warehouseRecord.setType(WarehouseRecord.TYPE_CONSUME);
        warehouseRecord.setConfirmFlag(WarehouseRecord.CONFIRMFLAG_YES);
        warehouseRecord.setInOrOut(WarehouseRecord.INOROUT_OUT);
        warehouseRecord.setStoreId(getCurrentStore().getId());
        warehouseRecord.setStoreName(getCurrentStore().getName());
        if (warehouseRecord.getRemark() == null) {
            warehouseRecord.setRemark("");
        }
        if(warehouseRecord.getOddId()!=null&&!warehouseRecord.getOddId().equals("")){
            warehouseRecordService.update(warehouseRecord);
        }else {
            warehouseRecordService.insert(warehouseRecord);
        }
        return success();
    }

    @RequestMapping(value = "/addBuy", method = RequestMethod.POST)
    public ResponseMessage addBuy(WarehouseRecord warehouseRecord) throws Exception{
        warehouseRecord.setStatus(WarehouseRecord.STATUS_ON);
        warehouseRecord.setType(WarehouseRecord.TYPE_BUY);
        warehouseRecord.setConfirmFlag(WarehouseRecord.CONFIRMFLAG_YES);
        warehouseRecord.setInOrOut(WarehouseRecord.INOROUT_IN);
        warehouseRecord.setStoreId(getCurrentStore().getId());
        warehouseRecord.setStoreName(getCurrentStore().getName());
        if(warehouseRecord.getRemark()==null){
            warehouseRecord.setRemark("");
        }
        if(warehouseRecord.getOddId()!=null&&!warehouseRecord.getOddId().equals("")){
            warehouseRecordService.update(warehouseRecord);
        }else{
            warehouseRecordService.insert(warehouseRecord);
        }
        return success();
    }

    @RequestMapping(value = "/addSend", method = RequestMethod.POST)
    public ResponseMessage addSend(WarehouseRecord warehouseRecord) throws Exception{
        if(warehouseRecord.getSendStoreId()==0){
            throw new Exception("请选择：门店");
        }
        Store sendStore = storeService.findById(warehouseRecord.getSendStoreId());
        if(sendStore==null){
            throw new Exception("门店不存在，请重新选择");
        }
        warehouseRecord.setStatus(WarehouseRecord.STATUS_ON);
        warehouseRecord.setType(WarehouseRecord.TYPE_SEND);
        warehouseRecord.setConfirmFlag(WarehouseRecord.CONFIRMFLAG_NO);
        warehouseRecord.setInOrOut(WarehouseRecord.INOROUT_OUT);
        warehouseRecord.setStoreId(getCurrentStore().getId());
        warehouseRecord.setStoreName(getCurrentStore().getName());
        if(warehouseRecord.getRemark()==null){
            warehouseRecord.setRemark("");
        }
        if(warehouseRecord.getOddId()!=null&&!warehouseRecord.getOddId().equals("")){
            warehouseRecordService.update(warehouseRecord);
        }else{
            warehouseRecordService.insert(warehouseRecord);
        }
        return success();
    }

    @RequestMapping(value = "/addSale", method = RequestMethod.POST)
    public ResponseMessage addSale(WarehouseRecord warehouseRecord) throws Exception{
        warehouseRecord.setStatus(WarehouseRecord.STATUS_ON);
        warehouseRecord.setType(WarehouseRecord.TYPE_SALE);
        warehouseRecord.setConfirmFlag(WarehouseRecord.CONFIRMFLAG_YES);
        warehouseRecord.setInOrOut(WarehouseRecord.INOROUT_OUT);
        warehouseRecord.setStoreId(getCurrentStore().getId());
        warehouseRecord.setStoreName(getCurrentStore().getName());
        if (warehouseRecord.getRemark() == null) {
            warehouseRecord.setRemark("");
        }
        if(warehouseRecord.getOddId()!=null&&!warehouseRecord.getOddId().equals("")){
            warehouseRecordService.update(warehouseRecord);
        }else {
            warehouseRecordService.insert(warehouseRecord);
        }
        return success();
    }

    @RequestMapping(value = "/addScrap", method = RequestMethod.POST)
    public ResponseMessage addScrap(WarehouseRecord warehouseRecord) throws Exception{
        warehouseRecord.setStatus(WarehouseRecord.STATUS_ON);
        warehouseRecord.setType(WarehouseRecord.TYPE_SCRAP);
        warehouseRecord.setConfirmFlag(WarehouseRecord.CONFIRMFLAG_YES);
        warehouseRecord.setInOrOut(WarehouseRecord.INOROUT_OUT);
        warehouseRecord.setStoreId(getCurrentStore().getId());
        warehouseRecord.setStoreName(getCurrentStore().getName());
        if (warehouseRecord.getRemark() == null) {
            warehouseRecord.setRemark("");
        }
        if(warehouseRecord.getOddId()!=null&&!warehouseRecord.getOddId().equals("")){
            warehouseRecordService.update(warehouseRecord);
        }else {
            warehouseRecordService.insert(warehouseRecord);
        }
        return success();
    }

    @RequestMapping(value = "/addCheck", method = RequestMethod.POST)
    public ResponseMessage addCheck(WarehouseRecord warehouseRecord) throws Exception{
        warehouseRecord.setStatus(WarehouseRecord.STATUS_ON);
        warehouseRecord.setType(WarehouseRecord.TYPE_CHECK);
        warehouseRecord.setConfirmFlag(WarehouseRecord.CONFIRMFLAG_YES);
        warehouseRecord.setInOrOut(WarehouseRecord.INOROUT_OUT);
        warehouseRecord.setStoreId(getCurrentStore().getId());
        warehouseRecord.setStoreName(getCurrentStore().getName());
        if (warehouseRecord.getRemark() == null) {
            warehouseRecord.setRemark("");
        }
        WarehouseRecord timeCheck = warehouseRecordService.findCheckByDate(warehouseRecord.getStoreId(),warehouseRecord.getDateTime());
        if(timeCheck!=null && !timeCheck.getOddId().equals(warehouseRecord.getOddId())){
            throw new Exception("当天已盘点,不能重复盘点");
        }
        if(warehouseRecord.getOddId()!=null&&!warehouseRecord.getOddId().equals("")){
            warehouseRecordService.update(warehouseRecord);
        }else {
            warehouseRecordService.insert(warehouseRecord);
        }
        return success();
    }

    @RequestMapping(value = "/addMake", method = RequestMethod.POST)
    public ResponseMessage addMake(WarehouseRecord warehouseRecord) throws Exception{
        warehouseRecord.setStatus(WarehouseRecord.STATUS_ON);
        warehouseRecord.setType(WarehouseRecord.TYPE_MAKE);
        warehouseRecord.setConfirmFlag(WarehouseRecord.CONFIRMFLAG_YES);
        warehouseRecord.setInOrOut(WarehouseRecord.INOROUT_OUT);
        warehouseRecord.setStoreId(getCurrentStore().getId());
        warehouseRecord.setStoreName(getCurrentStore().getName());
        if (warehouseRecord.getRemark() == null) {
            warehouseRecord.setRemark("");
        }
        if(warehouseRecord.getOddId()!=null&&!warehouseRecord.getOddId().equals("")){
            warehouseRecordService.update(warehouseRecord);
        }else {
            warehouseRecordService.insert(warehouseRecord);
        }
        return success();
    }

    @RequestMapping(value = "/confirmeRecord",method = RequestMethod.POST)
    public ResponseMessage confirmeRecord(String oddId){
        warehouseRecordService.confirmeRecord(oddId);
        return success();
    }

    @RequestMapping(value = "delete",method = RequestMethod.POST)
    public ResponseMessage delete(String oddId) throws Exception{
        WarehouseRecord warehouseRecord = warehouseRecordService.findById(oddId);
        if(warehouseRecord.getStoreId()!=getCurrentStore().getId()){
            throw new Exception("您无权限删除该记录");
        }
        warehouseRecordService.delete(oddId);
        return success();
    }

    @RequestMapping(value = "/find/{oddId}",method = RequestMethod.GET)
    public ResponseMessage find(@PathVariable String oddId){
        return success(warehouseRecordService.findById(oddId));
    }

}
