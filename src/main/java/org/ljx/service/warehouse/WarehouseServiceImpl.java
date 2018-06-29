package org.ljx.service.warehouse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.ljx.dao.WarehouseMapper;
import org.ljx.entity.Warehouse;
import org.ljx.entity.web.PageSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
@Service
public class WarehouseServiceImpl implements WarehouseService{

    @Autowired
    WarehouseMapper mapper;

    public List<Warehouse> list(int storeId,byte status){
        return mapper.list(storeId,status,null,null);
    }

    public List<Warehouse> list(int storeId,byte status,Date beginDate,Date endDate){
        return mapper.list(storeId,status,beginDate,endDate);
    }

    @Transactional
    public synchronized BigDecimal into(int storeId,int productId,BigDecimal num,Timestamp time){
        BigDecimal beforeSaveNum = new BigDecimal(0);
        Warehouse warehouse = mapper.findByStoreIdAndProductId(storeId,productId);
        if(warehouse!=null){
            beforeSaveNum = warehouse.getBalance();
            num = warehouse.getBalance().add(num);
            warehouse.setStatus(Warehouse.STATUS_OFF);
            mapper.update(warehouse);
        }
        warehouse = new Warehouse();
        warehouse.setProductId(productId);
        warehouse.setStoreId(storeId);
        warehouse.setBalance(num);
        warehouse.setTime(time);
        warehouse.setStatus(Warehouse.STATUS_ON);
        mapper.insert(warehouse);
        return beforeSaveNum;
    }

    @Transactional
    public synchronized BigDecimal out(int storeId,int productId,BigDecimal num,Timestamp time){
        BigDecimal beforeSaveNum = new BigDecimal(0);
        Warehouse warehouse = mapper.findByStoreIdAndProductId(storeId,productId);
        if(warehouse!=null){
            beforeSaveNum = warehouse.getBalance();
            num = warehouse.getBalance().subtract(num);
            warehouse.setStatus(Warehouse.STATUS_OFF);
            mapper.update(warehouse);
        }else{
            num = new BigDecimal(0).subtract(num);
        }
        warehouse = new Warehouse();
        warehouse.setProductId(productId);
        warehouse.setStoreId(storeId);
        warehouse.setBalance(num);
        warehouse.setTime(time);
        warehouse.setStatus(Warehouse.STATUS_ON);
        mapper.insert(warehouse);
        return beforeSaveNum;
    }

    @Transactional
    public synchronized BigDecimal change(int storeId,int productId,BigDecimal num,Timestamp time){
        BigDecimal beforeSaveNum = new BigDecimal(0);
        Warehouse warehouse = mapper.findByStoreIdAndProductId(storeId,productId);
        if(warehouse!=null){
            beforeSaveNum = warehouse.getBalance();
            warehouse.setStatus(Warehouse.STATUS_OFF);
            mapper.update(warehouse);
        }
        warehouse = new Warehouse();
        warehouse.setProductId(productId);
        warehouse.setStoreId(storeId);
        warehouse.setBalance(num);
        warehouse.setTime(time);
        warehouse.setStatus(Warehouse.STATUS_ON);
        mapper.insert(warehouse);
        return beforeSaveNum;
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
        return new PageInfo(mapper.list(storeId,status,null,null));
    }

}
