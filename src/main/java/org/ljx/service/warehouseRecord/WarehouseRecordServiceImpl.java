package org.ljx.service.warehouseRecord;

import com.github.pagehelper.PageHelper;
import org.ljx.dao.WarehouseMapper;
import org.ljx.dao.WarehouseRecordDetailMapper;
import org.ljx.dao.WarehouseRecordMapper;
import org.ljx.entity.WarehouseRecord;
import org.ljx.entity.WarehouseRecordDetail;
import org.ljx.entity.web.PageSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Created by ljx on 2018/5/29.
 */
@Service
public class WarehouseRecordServiceImpl implements WarehouseRecordService {

    @Autowired
    WarehouseRecordMapper warehouseRecordMapper;
    @Autowired
    WarehouseRecordDetailMapper warehouseRecordDetailMapper;

    public List<WarehouseRecord> list(PageSearch pageSearch,byte type,int storeId){
        PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return warehouseRecordMapper.listType(storeId,type);
    }

    @Transactional
    public void insert(WarehouseRecord warehouseRecord){
        warehouseRecord.setCreatTime(new Timestamp(System.currentTimeMillis()));
        warehouseRecord.setOddId(UUID.randomUUID().toString().replaceAll("-",""));
        //保存详细列表
        List<WarehouseRecordDetail> details = warehouseRecord.getListDetails();
        for(int i = 0;i<details.size();i++){
            WarehouseRecordDetail detail = details.get(i);
            if(detail.getNum()!=0){
                detail.setOddId(warehouseRecord.getOddId());
                detail.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
                warehouseRecordDetailMapper.insert(detail);
            }
        }
        warehouseRecordMapper.insert(warehouseRecord);
    }

    public void confirmeRecord(String oddId){
        WarehouseRecord record = findById(oddId);
        if(record!=null){
            record.setConfirmFlag(WarehouseRecord.CONFIRMFLAG_YES);
            warehouseRecordMapper.update(record);
        }
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
