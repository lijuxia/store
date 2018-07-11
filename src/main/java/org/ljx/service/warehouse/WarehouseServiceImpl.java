package org.ljx.service.warehouse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.ljx.dao.WarehouseMapper;
import org.ljx.dao.WarehouseRecordDetailMapper;
import org.ljx.entity.Warehouse;
import org.ljx.entity.WarehouseRecord;
import org.ljx.entity.WarehouseRecordDetail;
import org.ljx.entity.web.PageSearch;
import org.ljx.service.warehouseRecord.WarehouseRecordService;
import org.ljx.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
@Service
public class WarehouseServiceImpl implements WarehouseService{

    public final static byte OP_INTO = 1;
    public final static byte OP_OUT = 2;
    public final static byte OP_CHANGE = 3;

    @Autowired
    WarehouseMapper mapper;
    @Autowired
    WarehouseRecordService warehouseRecordService;
    @Autowired
    WarehouseRecordDetailMapper warehouseRecordDetailMapper;

    public List<Warehouse> list(int storeId,byte status){
        return mapper.list(storeId,0,status,null,null);
    }

    public List<Warehouse> list(int storeId,byte status,Timestamp beginDate,Timestamp endDate){
        return mapper.list(storeId,0,status,beginDate,endDate);
    }

    private BigDecimal getAfterSave(byte op,BigDecimal beforeSaveNum,BigDecimal num){
        BigDecimal afterSaveNum = new BigDecimal(0);
        switch (op){
            case OP_INTO:{
                afterSaveNum = beforeSaveNum.add(num);
            }
            case OP_OUT:{
                afterSaveNum = beforeSaveNum.subtract(num);
            }
            case OP_CHANGE:{
                afterSaveNum = num;
            }
            default:break;
        }
        return afterSaveNum;
    }

    /**
     * 更新库存
     * 库存记录，每天一条记录，出库入库在记录上进行修改
     * 正常入库，时间在上次库存时间之后，直接进行入库；
     * 异常入库（修改之前入库单，补填入库单），修改当日及之后直到下次盘点单当日为止的所有每日库存记录，并修改盘点单的问题数量，纠正错误
     * @param storeId
     * @param productId
     * @param num
     * @param time
     * @return
     */
    @Transactional
    public synchronized BigDecimal updateSave(byte op,int storeId,int productId,BigDecimal num,Timestamp time){
        //获取当日库存记录所余库存
        BigDecimal beforeSaveNum = new BigDecimal(0);
        BigDecimal afterSaveNum = num;
        Warehouse warehouse = mapper.findByStoreIdAndProductId(storeId,productId);
        if(warehouse!=null && warehouse.isToday(time)){//当前库存记录存在，并且是当日的，直接修改库存
            beforeSaveNum = warehouse.getBalance();
            afterSaveNum = getAfterSave(op,warehouse.getBalance(),num);
            warehouse.setBalance(afterSaveNum);
            mapper.update(warehouse);
        }else{
            if(warehouse!=null){//存库记录存在，但不是当日的，对现在库存记录到当日时间内所有未结算的日期进行日结算
                //判断库存时间，如果库存时间不是当日的话，判断为异常入库，需要对该异常入库时间之后，到盘点之前的所有当日库存结算记录进行修改
                if(!warehouse.isToday(time)){
                    if(warehouse.getTime().before(time)){//入库时间为之后时间，现在库存记录到当日时间内所有未结算的日期进行日结算
                        beforeSaveNum = warehouse.getBalance();
                        warehouse.setStatus(Warehouse.STATUS_OFF);
                        mapper.update(warehouse);
                        afterSaveNum = getAfterSave(op,warehouse.getBalance(),num);

                        List<Timestamp> list = warehouse.duringTimes(warehouse.getTime(),time);
                        for(Timestamp t : list){
                            warehouse = new Warehouse();
                            warehouse.setProductId(productId);
                            warehouse.setStoreId(storeId);
                            warehouse.setBalance(beforeSaveNum);
                            warehouse.setTime(t);
                            warehouse.setStatus(Warehouse.STATUS_OFF);
                            mapper.insert(warehouse);
                        }
                        //新增当日的库存记录
                        warehouse = new Warehouse();
                        warehouse.setProductId(productId);
                        warehouse.setStoreId(storeId);
                        warehouse.setBalance(afterSaveNum);
                        warehouse.setTime(time);
                        warehouse.setStatus(Warehouse.STATUS_ON);
                        mapper.insert(warehouse);
                    }else{//入库时间为之前时间，对之后到盘点之前的所有日库存记录进行修改
                        //获取该时间之后的第一次盘点单
                        WarehouseRecord check = findLastCheck(storeId,time);
                        Timestamp endTime = new Timestamp(System.currentTimeMillis());
                        if(check!=null){
                            //对盘点单进行修改
                            List<WarehouseRecordDetail> details = check.getListDetails();
                            for(int i=0;i<details.size();i++){
                                if(details.get(i).getProductId()==productId){
                                    WarehouseRecordDetail detail = details.get(i);
                                    BigDecimal save = detail.getBeforeSaveNum();
                                    save = save.add(num);
                                    detail.setBeforeSaveNum(save);
                                    warehouseRecordDetailMapper.update(detail);
                                }
                            }
                            Calendar c = Calendar.getInstance();
                            c.setTimeInMillis(check.getDateTime().getTime());
                            c.add(Calendar.DAY_OF_MONTH,-1);
                            endTime = TimeUtil.getEndTime(new Timestamp(c.getTimeInMillis()));
                        }
                        //获取该时间到盘点日期之前的所有库存日结算单,对结算单进行修改
                        if(op==OP_INTO){
                            mapper.updateHistory(storeId,productId,num,time,endTime);
                        }else if(op==OP_OUT){
                            mapper.updateHistory(storeId,productId,new BigDecimal(0).subtract(num),time,endTime);
                        }
                    }
                }
            }else{
                //新增当日的库存记录
                warehouse = new Warehouse();
                warehouse.setProductId(productId);
                warehouse.setStoreId(storeId);
                warehouse.setBalance(afterSaveNum);
                warehouse.setTime(time);
                warehouse.setStatus(Warehouse.STATUS_ON);
                mapper.insert(warehouse);
            }
            //获取当日的库存余量返回
            List<Warehouse> list = mapper.list(storeId,productId,(byte) 0,time, TimeUtil.getEndTime(time));
            for(Warehouse wh : list){
                beforeSaveNum = wh.getBalance();
            }
        }
        return beforeSaveNum;
    }

    private WarehouseRecord findLastCheck(int storeId,Timestamp time){
        PageSearch pageSearch = new PageSearch();
        pageSearch.setPageSize(1);
        List<WarehouseRecord> list = warehouseRecordService.list(pageSearch,WarehouseRecord.TYPE_CHECK,storeId,new Date(time.getTime()),null,"date asc");
        if(list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    public synchronized void delete(int id){
        Warehouse warehouseOld = mapper.findById(id);
        if(warehouseOld!=null){
            warehouseOld.setStatus(Warehouse.STATUS_OFF);
            mapper.update(warehouseOld);
        }
    }

    public Warehouse findById(int id){
        return mapper.findById(id);
    }

    public PageInfo list(PageSearch pageSearch,int storeId,byte status){
        PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return new PageInfo(mapper.list(storeId,0,status,null,null));
    }

}
