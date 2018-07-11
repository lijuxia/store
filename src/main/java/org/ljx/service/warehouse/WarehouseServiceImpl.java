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
        return mapper.list(storeId,0,status,null,null,"storeId,productId,time");
    }

    public List<Warehouse> list(int storeId,byte status,Timestamp beginDate,Timestamp endDate){
        return mapper.list(storeId,0,status,beginDate,endDate,"storeId,productId,time");
    }

    private BigDecimal getAfterSave(byte op,BigDecimal beforeSaveNum,BigDecimal num){
        BigDecimal afterSaveNum = new BigDecimal(0);
        switch (op){
            case OP_INTO:{
                afterSaveNum = beforeSaveNum.add(num);
                break;
            }
            case OP_OUT:{
                afterSaveNum = beforeSaveNum.subtract(num);
                break;
            }
            case OP_CHANGE:{
                afterSaveNum = num;
                break;
            }
            default:break;
        }
        return afterSaveNum;
    }

    /**
     * 初始化时间段间的所有库存日期记录
     * @param storeId
     * @param productId
     * @param time
     * @param warehouse
     */
    private void initWarehouse(int storeId,int productId,Timestamp time,Warehouse warehouse){
        //前一天的库存
        BigDecimal beforeSaveNum = new BigDecimal(0);
        Warehouse earlyWarehouse = getWarehouseEarliest(storeId,productId);
        Warehouse lastWarehouse = getWarehouseLast(storeId,productId);
        Timestamp earlyTime = new Timestamp(System.currentTimeMillis());
        Timestamp lastTime = new Timestamp(System.currentTimeMillis());
        if(earlyWarehouse!=null){
            earlyTime = earlyWarehouse.getTime();
        }
        if(lastWarehouse!=null){
            lastTime = lastWarehouse.getTime();
        }
        if(time.before(earlyTime)){
            //如果记录时间是当前库存时间之前，初始化记录时间到当前库存时间
            List<Timestamp> list = TimeUtil.duringTimes(time,earlyTime);
            //如果没有当前库存，则生成当日的库存记录
            if(warehouse==null){
                list.add(time);
            }
            for(Timestamp t : list){
                warehouse = new Warehouse(beforeSaveNum,storeId,productId,Warehouse.STATUS_OFF,t);
                if(TimeUtil.isSameday(t,time)){
                    warehouse.setStatus(Warehouse.STATUS_ON);
                }
                mapper.insert(warehouse);
            }
        }else if(time.after(lastTime)){
            if(lastWarehouse!=null){
                beforeSaveNum = lastWarehouse.getBalance();
            }
            //如果记录时间是当前库存时间之后，初始化当前库存时间到记录时间
            List<Timestamp> list = TimeUtil.duringTimes(lastTime,time);
            //如果没有当前库存，则生成当日的库存记录
            if(warehouse==null){
                list.add(time);
            }
            for(Timestamp t : list){
                warehouse = new Warehouse(beforeSaveNum,storeId,productId,Warehouse.STATUS_OFF,t);
                mapper.insert(warehouse);
            }
        }
    }

    /**
     * 获取当日的库存记录
     * @param storeId
     * @param productId
     * @param time
     * @return
     */
    private Warehouse getWarehouseByDate(int storeId,int productId,Timestamp time){
        PageHelper.startPage(1,1);
        List<Warehouse> list = mapper.list(storeId,productId,(byte) 0,time, TimeUtil.getEndTime(time),"time desc");
        for(Warehouse w : list){
            return w;
        }
        return null;
    }

    /**
     * 获取当日的库存记录
     * @param storeId
     * @param productId
     * @return
     */
    private Warehouse getWarehouseLast(int storeId,int productId){
        PageHelper.startPage(1,1);
        List<Warehouse> list = mapper.list(storeId,productId,(byte) 0,null, null,"time desc");
        for(Warehouse w : list){
            return w;
        }
        return null;
    }

    /**
     * 获取当日的库存记录
     * @param storeId
     * @param productId
     * @return
     */
    private Warehouse getWarehouseEarliest(int storeId,int productId){
        PageHelper.startPage(1,1);
        List<Warehouse> list = mapper.list(storeId,productId,(byte) 0,null, null,"time");
        for(Warehouse w : list){
            return w;
        }
        return null;
    }

    /**
     * 更新库存（一个月之前的记录无法修改）
     * 库存记录，每天一条记录，出库入库在记录上进行修改
     * 正常入库，时间在上次库存时间之后，直接进行入库；
     * 异常入库（修改之前入库单，补填入库单），修改当日及之后直到下次盘点单当日为止的所有每日库存记录，并修改盘点单的问题数量，纠正错误
     * 步骤：1、将修改日期与当前日期之间所有没有库存记录的日期，添加库存记录
     *       2、查询理修改日期最近的盘点记录（日期之后的盘点单），如果盘点单是同一天，就不需要3、4步骤的处理
     *       3、根据盘点日期，对修改日期与盘点日期之间的库存记录进行连带修改
     *       4、对盘点单进行修改
     *       5、修改当日的库存记录
     *       6、返回修改日期的原库存记录
     * @param storeId
     * @param productId
     * @param num
     * @param time
     * @return
     */
    @Transactional
    public synchronized BigDecimal updateSave(byte op,int storeId,int productId,BigDecimal num,Timestamp time,Timestamp creatTime){
        BigDecimal resultSave = new BigDecimal(0);
        //获取当前库存记录
//        Warehouse warehouse = mapper.findByStoreIdAndProductId(storeId,productId);
        Warehouse warehouse = getWarehouseByDate(storeId,productId,time);
        //1、将修改日期与当前日期之间所有没有库存记录的日期，添加库存记录
        initWarehouse(storeId,productId,time,warehouse);
        //2、查询理修改日期最近的盘点记录（日期之后的盘点单），如果盘点单是同一天，就不需要3、4步骤的处理
        WarehouseRecord check = warehouseRecordService.findLastCheck(storeId,time);
        //获取当日的库存记录
        Warehouse timeWarehouse = getWarehouseByDate(storeId,productId,time);
        resultSave = timeWarehouse.getBalance();
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        Warehouse lastWarehouse = getWarehouseLast(storeId,productId);
        if(lastWarehouse!=null){
            endTime = lastWarehouse.getTime();
        }
        if(check!=null){
            endTime = check.getDateTime();
        }
        //处理当日有盘点单的情况
        //1、判断盘点单的添加时间和单据日期以及处理的单据添加日期是否是同一天
        //2、同一天：处理盘点单和修改单据之间的时间关系；不同天：不处理
        if(check!=null&&TimeUtil.isSameday(time,creatTime)&& TimeUtil.isSameday(check.getCreatTime(),time)&&TimeUtil.isSameday(check.getDateTime(),time)){
            //3、处理单据时间在盘点单之前：不修改库存，修改盘点单内容
            if(creatTime.before(check.getCreatTime())){
                //对盘点单进行修改
                List<WarehouseRecordDetail> details = check.getListDetails();
                for(int i=0;i<details.size();i++){
                    if(details.get(i).getProductId()==productId){
                        WarehouseRecordDetail detail = details.get(i);
                        BigDecimal save = detail.getBeforeSaveNum();
                        save = getAfterSave(op,save,num);
                        detail.setBeforeSaveNum(save);
                        warehouseRecordDetailMapper.update(detail);
                    }
                }
            }
            //4、处理单据时间在盘点单之后：修改库存及之后的库存，不修改盘点单内容
            if(creatTime.after(check.getCreatTime())) {
                timeWarehouse.setBalance(getAfterSave(op, resultSave, num));
                mapper.update(timeWarehouse);
                if(!TimeUtil.isSameday(time,new Timestamp(System.currentTimeMillis()))){
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(time.getTime());
                    c.add(Calendar.DAY_OF_MONTH,1);
                    updateSave(op,storeId,productId,num,new Timestamp(c.getTimeInMillis()),creatTime);
                }
            }
        //有盘点单，盘点单的日期和修改日期是同一天：不修改库存，修改盘点单
        }else if(check!=null&&TimeUtil.isSameday(time,check.getDateTime())){
            //对盘点单进行修改
            List<WarehouseRecordDetail> details = check.getListDetails();
            for(int i=0;i<details.size();i++){
                if(details.get(i).getProductId()==productId){
                    WarehouseRecordDetail detail = details.get(i);
                    BigDecimal save = detail.getBeforeSaveNum();
                    save = getAfterSave(op,save,num);
                    detail.setBeforeSaveNum(save);
                    warehouseRecordDetailMapper.update(detail);
                }
            }
        }else{
            //3、根据盘点日期，对修改日期与盘点日期之间的库存记录进行连带修改
            if(check!=null){
                //盘点日期的前一天
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(endTime.getTime());
                c.add(Calendar.DAY_OF_MONTH,-1);
                endTime = TimeUtil.getEndTime(new Timestamp(c.getTimeInMillis()));
            }
            if(op==OP_INTO){
                mapper.updateHistory(storeId,productId,num,time,endTime);
            }else if(op==OP_OUT){
                mapper.updateHistory(storeId,productId,new BigDecimal(0).subtract(num),time,endTime);
            }else if(op==OP_CHANGE){//改变、更新当天库存
                BigDecimal diff = num.subtract(timeWarehouse.getBalance());
                mapper.updateHistory(storeId,productId,diff,time,endTime);
            }
            //4、对盘点单进行修改
            if(check!=null){
                //对盘点单进行修改
                List<WarehouseRecordDetail> details = check.getListDetails();
                for(int i=0;i<details.size();i++){
                    if(details.get(i).getProductId()==productId){
                        WarehouseRecordDetail detail = details.get(i);
                        BigDecimal save = detail.getBeforeSaveNum();
                        save = getAfterSave(op,save,num);
                        detail.setBeforeSaveNum(save);
                        warehouseRecordDetailMapper.update(detail);
                    }
                }
            }
            //5、修改当日的库存记录
            timeWarehouse.setBalance(getAfterSave(op,resultSave,num));
            mapper.update(timeWarehouse);
        }
        //6、返回修改日期的原库存记录
        return resultSave;
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
        return new PageInfo(mapper.list(storeId,0,status,null,null,"storeId,productId,time"));
    }

}
