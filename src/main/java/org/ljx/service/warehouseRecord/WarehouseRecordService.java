package org.ljx.service.warehouseRecord;

import org.ljx.entity.WarehouseRecord;
import org.ljx.entity.web.PageSearch;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
public interface WarehouseRecordService {

    public List<WarehouseRecord> list(PageSearch pageSearch, byte type, int storeId);

    public List<WarehouseRecord> list(PageSearch pageSearch, byte type, int storeId, Timestamp beginTime, Timestamp endTime);

    public void insert(WarehouseRecord warehouseRecord);

    public void update(WarehouseRecord warehouseRecord);

    public void delete(String oddId);

    public WarehouseRecord findById(String oddId);

    public void confirmeRecord(String oddId);
}
