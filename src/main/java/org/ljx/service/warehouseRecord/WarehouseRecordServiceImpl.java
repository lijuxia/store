package org.ljx.service.warehouseRecord;

import com.github.pagehelper.PageHelper;
import org.ljx.dao.WarehouseRecordDetailMapper;
import org.ljx.dao.WarehouseRecordMapper;
import org.ljx.entity.Product;
import org.ljx.entity.WarehouseRecord;
import org.ljx.entity.WarehouseRecordDetail;
import org.ljx.entity.web.PageSearch;
import org.ljx.service.product.ProductService;
import org.ljx.service.warehouse.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
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
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    ProductService productService;

    public List<WarehouseRecord> list(PageSearch pageSearch,byte type,int storeId){
        if(pageSearch!=null)
        PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return warehouseRecordMapper.list(storeId,type,null,null,null,null);
    }

    public List<WarehouseRecord> list(PageSearch pageSearch, byte type, int storeId, Timestamp beginTime, Timestamp endTime){
        if(pageSearch!=null)
        PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return warehouseRecordMapper.list(storeId,type,beginTime,endTime,null,null);
    }

    public List<WarehouseRecord> list(PageSearch pageSearch, byte type, int storeId, Date beginDate, Date endDate){
        if(pageSearch!=null)
        PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return warehouseRecordMapper.list(storeId,type,null,null,beginDate,endDate);
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
                //库存修改
                if(warehouseRecord.getType()==WarehouseRecord.TYPE_BUY){//进货
                    warehouseService.into(warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getCreatTime());
                }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_CONSUME
                        || warehouseRecord.getType()==WarehouseRecord.TYPE_SCRAP){//报废、消耗
                    warehouseService.out(warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getCreatTime());
                }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_SALE){//销售、实际库存改变转换为原料改变
                    Product product = productService.findById(detail.getProductId());
                    for(int p=0;p<product.getDetails().size();p++){
                        warehouseService.out(warehouseRecord.getStoreId(),product.getDetails().get(p).getDetailId(),detail.getNum()*product.getDetails().get(p).getNum(),warehouseRecord.getCreatTime());
                    }
                }
            }
        }
        warehouseRecordMapper.insert(warehouseRecord);
    }

    @Transactional
    public void confirmeRecord(String oddId){
        WarehouseRecord record = findById(oddId);
        if(record!=null){
            record.setConfirmFlag(WarehouseRecord.CONFIRMFLAG_YES);
            warehouseRecordMapper.update(record);
            //配送单确认后，库存改变
            List<WarehouseRecordDetail> details = record.getListDetails();
            for(int i = 0;i<details.size();i++){
                WarehouseRecordDetail detail = details.get(i);
                //配送单位库存减少
                warehouseService.out(record.getStoreId(),detail.getProductId(),detail.getNum(),record.getCreatTime());
                //接收单位库存增加
                warehouseService.into(record.getSendStoreId(),detail.getProductId(),detail.getNum(),record.getCreatTime());
            }

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
