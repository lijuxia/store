package org.ljx.service.store;

import org.ljx.entity.Store;

import java.util.List;

/**
 * Created by ljx on 2018/5/14.
 */
public interface StoreService {

    public List<Store> list();

    public void insert(Store store);

    public void delete(int id);

    public void update(Store store);

    public Store findById(int id);
}
