package org.ljx.service.warehouse;

import com.github.pagehelper.PageInfo;
import org.ljx.entity.Warehouse;
import org.ljx.entity.web.PageSearch;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
public interface WarehouseService {

    public List<Warehouse> list(int storeId, byte status);

    public List<Warehouse> list(int storeId, byte status, Timestamp beginDate, Timestamp endDate);

    public List<Warehouse> listGroupBy();

    public BigDecimal updateSave(byte op,int storeId,int productId,BigDecimal num,Timestamp time,Timestamp creatTime);

    public void delete(int id);

    public Warehouse findById(int id);

    public PageInfo list(PageSearch pageSearch, int storeId, byte status);
}
