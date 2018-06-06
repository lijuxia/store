package org.ljx.service.warehouse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.ljx.dao.WarehouseMapper;
import org.ljx.entity.Warehouse;
import org.ljx.entity.web.PageSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
@Service
public class WarehouseServiceImpl implements WarehouseService{

    @Autowired
    WarehouseMapper mapper;

    public List<Warehouse> list(int storeId,byte status){
        return mapper.list(storeId,status);
    }

    public void insert(Warehouse warehouse){
        mapper.insert(warehouse);
    }

    public void update(Warehouse warehouse){
        Warehouse warehouseOld = mapper.findById(warehouse.getId());
        if(warehouseOld!=null){
            mapper.update(warehouse);
        }
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
        return new PageInfo(mapper.list(storeId,status));
    }

}
