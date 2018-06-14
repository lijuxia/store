package org.ljx.service.warehouse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.ljx.dao.WarehouseMapper;
import org.ljx.entity.Warehouse;
import org.ljx.entity.web.PageSearch;
import org.ljx.util.DoubleUtil;
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
    public void into(int storeId,int productId,BigDecimal num,Timestamp time){
        Warehouse warehouse = mapper.findByStoreIdAndProductId(storeId,productId);
        if(warehouse!=null){
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
    }

    @Transactional
    public void out(int storeId,int productId,BigDecimal num,Timestamp time){
        Warehouse warehouse = mapper.findByStoreIdAndProductId(storeId,productId);
        if(warehouse!=null){
            num = warehouse.getBalance().subtract(num);
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
    }

    public void delete(int id){
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
