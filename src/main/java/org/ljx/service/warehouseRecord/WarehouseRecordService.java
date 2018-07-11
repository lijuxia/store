package org.ljx.service.warehouseRecord;

import org.ljx.entity.WarehouseRecord;
import org.ljx.entity.web.PageSearch;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
public interface WarehouseRecordService {

    public List<WarehouseRecord> list(PageSearch pageSearch, byte type, int storeId,String order);

    public List<WarehouseRecord> list(PageSearch pageSearch, byte type, int storeId, Timestamp beginTime, Timestamp endTime,String order);

    public List<WarehouseRecord> list(PageSearch pageSearch, byte type, int storeId, Date beginDate, Date endDate,String order);

    public void insert(WarehouseRecord warehouseRecord);

    public void update(WarehouseRecord warehouseRecord);

    public void delete(String oddId);

    public WarehouseRecord findById(String oddId);

    public void confirmeRecord(String oddId);
}
