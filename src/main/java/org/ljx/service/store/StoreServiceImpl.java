package org.ljx.service.store;

import org.ljx.dao.StoreMapper;
import org.ljx.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 店铺业务实现层
 * Created by ljx on 2018/5/14.
 */
@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    StoreMapper mapper;

    public List<Store> list(){
        return mapper.list();
    }

    public void insert(Store store){
        mapper.insert(store);
    }

    public void delete(int id){
        mapper.delete(id);
    }

    public void update(Store store){
        mapper.update(store);
    }

    public Store findById(int id){
        return mapper.findById(id);
    }
}
