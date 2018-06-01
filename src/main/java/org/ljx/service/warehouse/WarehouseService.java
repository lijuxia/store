package org.ljx.service.warehouse;

import com.github.pagehelper.PageInfo;
import org.ljx.entity.Warehouse;
import org.ljx.entity.web.PageSearch;

import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
public interface WarehouseService {

    public List<Warehouse> list(byte storeId, byte status);

    public void insert(Warehouse warehouse);

    public void update(Warehouse warehouse);

    public void delete(int id);

    public Warehouse findById(int id);

    public PageInfo list(PageSearch pageSearch, int storeId, byte status);
}
