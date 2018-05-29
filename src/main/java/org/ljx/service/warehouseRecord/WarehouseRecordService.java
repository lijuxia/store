package org.ljx.service.warehouseRecord;

import org.ljx.entity.WarehouseRecord;

import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
public interface WarehouseRecordService {

    public List<WarehouseRecord> list();

    public void insert(WarehouseRecord warehouseRecord);

    public void update(WarehouseRecord warehouseRecord);

    public void delete(String oddId);

    public WarehouseRecord findById(String oddId);
}
