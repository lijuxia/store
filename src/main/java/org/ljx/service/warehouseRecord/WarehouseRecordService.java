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

    public List<WarehouseRecord> list(PageSearch pageSearch, byte[] types, int storeId,String order);

    public List<WarehouseRecord> list(PageSearch pageSearch, byte[] types, int storeId, Timestamp beginTime, Timestamp endTime,String order);

    public List<WarehouseRecord> list(PageSearch pageSearch, byte[] types, int storeId, Date beginDate, Date endDate,String order);

    public List<WarehouseRecord> list(PageSearch pageSearch, byte[] types, int storeId, byte confirmFlag,String order);

    public List<WarehouseRecord> list(PageSearch pageSearch, byte[] types, int storeId, Date beginDate, Date endDate, Timestamp beginTime, Timestamp endTime,String order);

    public void reflesh(int storeId);

    public void insert(WarehouseRecord warehouseRecord);

    public void update(WarehouseRecord warehouseRecord) throws Exception;

    public void delete(String oddId);

    public WarehouseRecord findById(String oddId);

    public void confirmeRecord(String oddId);

    public WarehouseRecord findLastCheck(int storeId,Timestamp time);

    /**
     * 获取指定日期的盘点单
     * @param storeId
     * @param time
     * @return
     */
    public WarehouseRecord findCheckByDate(int storeId,Timestamp time);
}
