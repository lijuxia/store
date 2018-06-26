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

import java.math.BigDecimal;
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
        if(warehouseRecord.getType()==WarehouseRecord.TYPE_MAKE){//生产，半成品入库
            warehouseService.into(warehouseRecord.getStoreId(),warehouseRecord.getMakeProductId(),warehouseRecord.getMakeNum(),warehouseRecord.getCreatTime());//半成品入库
        }
        for(int i = 0;i<details.size();i++){
            WarehouseRecordDetail detail = details.get(i);
            if(detail.getNum().compareTo(BigDecimal.ZERO)!=0){
                detail.setOddId(warehouseRecord.getOddId());
                detail.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
                //库存修改
                if(warehouseRecord.getType()==WarehouseRecord.TYPE_BUY){//进货
                    BigDecimal beforeSaveNum = warehouseService.into(warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getCreatTime());
                    detail.setBeforeSaveNum(beforeSaveNum);
                }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_CONSUME
                        || warehouseRecord.getType()==WarehouseRecord.TYPE_SCRAP){//报废、消耗
                    BigDecimal beforeSaveNum = warehouseService.out(warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getCreatTime());
                    detail.setBeforeSaveNum(beforeSaveNum);
                }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_SALE){//销售、实际库存改变转换为原料改变
                    Product product = productService.findById(detail.getProductId());
                    for(int p=0;p<product.getDetails().size();p++){
                        warehouseService.out(warehouseRecord.getStoreId(),product.getDetails().get(p).getDetailId(),detail.getNum().multiply(product.getDetails().get(p).getNum()),warehouseRecord.getCreatTime());
                    }
                }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_MAKE){//生产
                    BigDecimal beforeSaveNum = warehouseService.out(warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getCreatTime());//原料出库
                    detail.setBeforeSaveNum(beforeSaveNum);
                }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_CHECK){//盘点,库存直接修改为盘点量,并生成之前的库存量,用作后面统计数据,分析盘点的缺失问题
                    if(detail.getNum().compareTo(BigDecimal.ZERO)!=-1){//大于等于0,可以修改库存,小于0不能修改库存,认为是无效数据
                        BigDecimal beforeSaveNum = warehouseService.change(warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getCreatTime());//原料出库
                        detail.setBeforeSaveNum(beforeSaveNum);
                    }
                }
                if(warehouseRecord.getType()==WarehouseRecord.TYPE_CHECK
                        &&detail.getNum().compareTo(BigDecimal.ZERO)==-1){
                }else{
                    warehouseRecordDetailMapper.insert(detail);
                }
            }else{
                if(warehouseRecord.getType()==WarehouseRecord.TYPE_CHECK){//盘点,库存直接修改为盘点量,并生成之前的库存量,用作后面统计数据,分析盘点的缺失问题
                    detail.setOddId(warehouseRecord.getOddId());
                    detail.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
                    BigDecimal beforeSaveNum = warehouseService.change(warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getCreatTime());//原料出库
                    detail.setBeforeSaveNum(beforeSaveNum);
                    warehouseRecordDetailMapper.insert(detail);
                }
            }
        }
        warehouseRecordMapper.insert(warehouseRecord);
    }

    @Transactional
    public void confirmeRecord(String oddId){
        WarehouseRecord record = findById(oddId);
        if(record!=null){
            Timestamp time = new Timestamp(System.currentTimeMillis());
            record.setConfirmFlag(WarehouseRecord.CONFIRMFLAG_YES);
            warehouseRecordMapper.update(record);
            //配送单确认后，库存改变
            List<WarehouseRecordDetail> details = record.getListDetails();
            for(int i = 0;i<details.size();i++){
                WarehouseRecordDetail detail = details.get(i);
                //配送单位库存减少
                warehouseService.out(record.getStoreId(),detail.getProductId(),detail.getNum(),time);
                //接收单位库存增加
                warehouseService.into(record.getSendStoreId(),detail.getProductId(),detail.getNum(),time);
            }

        }
    }

    public void update(WarehouseRecord warehouseRecord){
        WarehouseRecord warehouseRecordOld = warehouseRecordMapper.findById(warehouseRecord.getOddId());
        if(warehouseRecordOld!=null){
            warehouseRecordMapper.update(warehouseRecord);
        }
    }

    @Transactional
    public void delete(String oddId){
        WarehouseRecord warehouseRecordOld = warehouseRecordMapper.findById(oddId);
        if(warehouseRecordOld!=null){
            warehouseRecordOld.setStatus(WarehouseRecord.STATUS_OFF);
            warehouseRecordMapper.update(warehouseRecordOld);
        }
        //单据删除无效后,库存回滚
        Timestamp updateTime = new Timestamp(System.currentTimeMillis());
        //保存详细列表
        List<WarehouseRecordDetail> details = warehouseRecordOld.getListDetails();
        if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_MAKE){//生产，半成品入库回退
            warehouseService.out(warehouseRecordOld.getStoreId(),warehouseRecordOld.getMakeProductId(),warehouseRecordOld.getMakeNum(),updateTime);//半成品入库
        }
        for(int i = 0;i<details.size();i++){
            WarehouseRecordDetail detail = details.get(i);
            if(detail.getNum().compareTo(BigDecimal.ZERO)!=0){
                //库存修改
                if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_BUY){//进货回退
                    warehouseService.out(warehouseRecordOld.getStoreId(),detail.getProductId(),detail.getNum(),updateTime);
                }else if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_CONSUME
                        || warehouseRecordOld.getType()==WarehouseRecord.TYPE_SCRAP){//报废、消耗回退
                    warehouseService.into(warehouseRecordOld.getStoreId(),detail.getProductId(),detail.getNum(),updateTime);
                }else if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_SALE){//销售、实际库存改变转换为原料改变,回退
                    Product product = productService.findById(detail.getProductId());
                    for(int p=0;p<product.getDetails().size();p++){
                        warehouseService.into(warehouseRecordOld.getStoreId(),product.getDetails().get(p).getDetailId(),detail.getNum().multiply(product.getDetails().get(p).getNum()),updateTime);
                    }
                }else if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_MAKE){//生产 回退
                    warehouseService.out(warehouseRecordOld.getStoreId(),detail.getProductId(),detail.getNum(),updateTime);//原料出库
                }else if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_SEND){//配送回退
                    //配送单位库存回退增加
                    warehouseService.into(warehouseRecordOld.getStoreId(),detail.getProductId(),detail.getNum(),updateTime);
                    //接收单位库存回退减少
                    warehouseService.out(warehouseRecordOld.getSendStoreId(),detail.getProductId(),detail.getNum(),updateTime);
                }else if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_CHECK){//盘点,根据盘点后的库存和盘点之前的库存,差值进行回退
                    BigDecimal num = detail.getBeforeSaveNum().subtract(detail.getNum());
                    warehouseService.into(warehouseRecordOld.getStoreId(),detail.getProductId(),num,updateTime);
                }
            }else{
                if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_CHECK){//盘点,根据盘点后的库存和盘点之前的库存,差值进行回退
                    BigDecimal num = detail.getBeforeSaveNum().subtract(detail.getNum());
                    warehouseService.into(warehouseRecordOld.getStoreId(),detail.getProductId(),num,updateTime);
                }
            }
        }
    }

    public WarehouseRecord findById(String oddId){
        return warehouseRecordMapper.findById(oddId);
    }
}
