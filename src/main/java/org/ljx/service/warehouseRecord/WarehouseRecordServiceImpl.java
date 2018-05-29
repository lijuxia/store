package org.ljx.service.warehouseRecord;

import org.ljx.dao.WarehouseMapper;
import org.ljx.dao.WarehouseRecordDetailMapper;
import org.ljx.dao.WarehouseRecordMapper;
import org.ljx.entity.WarehouseRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ljx on 2018/5/29.
 */
@Service
public class WarehouseRecordServiceImpl implements WarehouseRecordService {

    @Autowired
    WarehouseRecordMapper warehouseRecordMapper;
    @Autowired
    WarehouseRecordDetailMapper warehouseRecordDetailMapper;

    public List<WarehouseRecord> list(){
        return warehouseRecordMapper.list();
    }

    public void insert(WarehouseRecord warehouseRecord){
        warehouseRecord.setCreatTime(new Timestamp(System.currentTimeMillis()));
        warehouseRecordMapper.insert(warehouseRecord);
    }

    public void update(WarehouseRecord warehouseRecord){
        WarehouseRecord warehouseRecordOld = warehouseRecordMapper.findById(warehouseRecord.getOddId());
        if(warehouseRecordOld!=null){
            warehouseRecordMapper.update(warehouseRecord);
        }
    }

    public void delete(String oddId){
        WarehouseRecord warehouseRecordOld = warehouseRecordMapper.findById(oddId);
        if(warehouseRecordOld!=null){
            warehouseRecordOld.setStatus(WarehouseRecord.STATUS_OFF);
            warehouseRecordMapper.update(warehouseRecordOld);
        }
    }

    public WarehouseRecord findById(String oddId){
        return warehouseRecordMapper.findById(oddId);
    }
}
