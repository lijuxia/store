package org.ljx.service.store;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.ljx.dao.StoreMapper;
import org.ljx.entity.web.PageSearch;
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

    public void insertMD(Store store){
        store.setType(Store.TYPE_STORE);
        store.setStatus(Store.STATUS_ON);
        mapper.insert(store);
    }

    public void insertZP(Store store){
        store.setType(Store.TYPE_DISTRIBUTION_CENTRE);
        store.setStatus(Store.STATUS_ON);
        mapper.insert(store);
    }

    public void delete(int id){
        Store store = mapper.findById(id);
        if(store!=null){
            store.setStatus(Store.STATUS_OFF);
            mapper.update(store);
        }
    }

    public void update(Store store){
        Store storeOld = mapper.findById(store.getId());
        if(storeOld!=null){
            storeOld.setName(store.getName());
            mapper.update(storeOld);
        }
    }

    public void updatePassword(int id,String oldPassword,String password) throws Exception{
        Store store = mapper.findById(id);
        if(store!=null){
            if(!oldPassword.equals(store.getPassword())){
                throw new Exception("旧密码错误");
            }
            store.setPassword(password);
            mapper.update(store);
        }
    }

    public Store findById(int id){
        return mapper.findById(id);
    }

    public PageInfo list(PageSearch pageSearch){
        PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return new PageInfo(mapper.list());
    }

    public PageInfo list(PageSearch pageSearch,byte type){
        PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return new PageInfo(mapper.listType(type));
    }

    public Store login(String username,String password) throws Exception{
        Store store = mapper.findByUsername(username);
        if(store==null){
            throw new Exception("账号不存在");
        }else if(!password.equals(store.getPassword())){
            throw new Exception("密码错误");
        }else{
            return store;
        }
    }
}
