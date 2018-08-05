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

    public List<Warehouse> listGroupBy(){
        return mapper.listGroupBy();
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
        Timestamp earlyTime = TimeUtil.getNow();
        Timestamp lastTime = TimeUtil.getNow();
        if(earlyWarehouse!=null){
            earlyTime = earlyWarehouse.getTime();
        }
        if(lastWarehouse!=null){
            lastTime = lastWarehouse.getTime();
        }
        //添加当前日期的库存
        if(earlyWarehouse==null && lastWarehouse==null){
            Warehouse warehouse1 = new Warehouse(beforeSaveNum,storeId,productId,Warehouse.STATUS_OFF,TimeUtil.getBeginTime(TimeUtil.getNow()));
            mapper.insert(warehouse1);
            initWarehouse(storeId,productId,time,warehouse);
            return ;
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
     *       2、查询当天是否有盘点单：
     *          有盘点单，并盘点该商品，执行3
     *          有盘点单，未盘点该商品，执行4
     *          无盘点单，执行4
     *       3、修改盘点单中对应的原始库存数量（结束）
     *       4、修改库存后判断：
     *          库存时间是今天（结束）
     *          库存时间为今天之前，修改下一天库存（递归）
     *              (如果是盘点，修改下一天的时候，改为处理差值）
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
        Warehouse warehouse = getWarehouseByDate(storeId,productId,time);
        //1、将修改日期与当前日期之间所有没有库存记录的日期，添加库存记录
        initWarehouse(storeId,productId,time,warehouse);

        //2、查询当天是否有盘点单
        //获取当天盘点单
        WarehouseRecord check = warehouseRecordService.findCheckByDate(storeId,time);

        //是否修改下一天库存记录标志
        boolean changeNextDay = true;

        if(check!=null){
            //有盘点单
            List<WarehouseRecordDetail> details = check.getListDetails();
            for(int i=0;i<details.size();i++){
                if(details.get(i).getProductId()==productId){
                    //并盘点改商品，修改盘点单中对应的原始库存数量（结束）
                    WarehouseRecordDetail detail = details.get(i);
                    BigDecimal save = detail.getBeforeSaveNum();
                    save = getAfterSave(op,save,num);
                    resultSave = save;
                    detail.setBeforeSaveNum(save);
                    warehouseRecordDetailMapper.update(detail);
                    changeNextDay = false;
                }
            }
        }else{
            //盘点单不存在，修改当日库存
            changeNextDay = true;
        }
        //判断是否修改下一天库存（递归）
        if(changeNextDay){
            //获取当日的库存记录
            Warehouse timeWarehouse = getWarehouseByDate(storeId,productId,time);
            resultSave = timeWarehouse.getBalance();
            timeWarehouse.setBalance(getAfterSave(op, resultSave, num));
            mapper.update(timeWarehouse);
            //库存日期不是今天，并且比今天早，则修改下一天库存
            if(!TimeUtil.isSameday(time,TimeUtil.getNow())
                    && time.before(TimeUtil.getNow())){
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(time.getTime());
                c.add(Calendar.DAY_OF_MONTH,1);
                //如果是盘点，修改下一天的时候，改为处理差值
                if(op == OP_CHANGE){
                    num = num.subtract(resultSave);
                    op = OP_INTO;
                }
                updateSave(op,storeId,productId,num,new Timestamp(c.getTimeInMillis()),creatTime);
            }
        }
        //3、返回修改日期的原库存记录
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
