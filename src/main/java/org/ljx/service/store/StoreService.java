package org.ljx.service.store;

import com.github.pagehelper.PageInfo;
import org.ljx.entity.web.PageSearch;
import org.ljx.entity.Store;

import java.util.List;

/**
 * Created by ljx on 2018/5/14.
 */
public interface StoreService {

    public List<Store> list();

    public void insertMD(Store store);

    public void insertZP(Store store);

    public void delete(int id);

    public void update(Store store);

    public void updatePassword(int id,String oldPassword,String password) throws Exception;

    public Store findById(int id);

    public PageInfo list(PageSearch pageSearch);

    public PageInfo list(PageSearch pageSearch,byte type);

    public Store login(String username,String password) throws Exception;
}
