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
import org.ljx.service.warehouse.WarehouseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

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

    public List<WarehouseRecord> list(PageSearch pageSearch,byte type,int storeId,String order){
        if(pageSearch!=null)
        PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return warehouseRecordMapper.list(storeId,type,null,null,null,null,order);
    }

    public List<WarehouseRecord> list(PageSearch pageSearch, byte type, int storeId, Timestamp beginTime, Timestamp endTime,String order){
        if(pageSearch!=null)
        PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return warehouseRecordMapper.list(storeId,type,beginTime,endTime,null,null,order);
    }

    public List<WarehouseRecord> list(PageSearch pageSearch, byte type, int storeId, Date beginDate, Date endDate,String order){
        if(pageSearch!=null)
        PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return warehouseRecordMapper.list(storeId,type,null,null,beginDate,endDate,order);
    }

    public List<WarehouseRecord> list(PageSearch pageSearch, byte type, int storeId, Date beginDate, Date endDate, Timestamp beginTime, Timestamp endTime,String order){
        if(pageSearch!=null)
            PageHelper.startPage(pageSearch.getPageNum(),pageSearch.getPageSize());
        return warehouseRecordMapper.list(storeId,type,beginTime,endTime,beginDate,endDate,order);
    }

    @Transactional
    public void reflesh(int storeId){
        List<WarehouseRecord> list = warehouseRecordMapper.list(0,(byte)0,null,null,null,null,"creatTime asc");
        for(int x=0;x<list.size();x++){
            WarehouseRecord warehouseRecord = list.get(x);
            if(warehouseRecord.getType()==WarehouseRecord.TYPE_SEND){
                List<WarehouseRecordDetail> details = warehouseRecord.getListDetails();
                for(int i = 0;i<details.size();i++){
                    WarehouseRecordDetail detail = details.get(i);
                    //配送单位库存减少
                    warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());
                    //接收单位库存增加
                    warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecord.getSendStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());
                }
            }
            //保存详细列表
            List<WarehouseRecordDetail> details = warehouseRecord.getListDetails();
            if(warehouseRecord.getType()==WarehouseRecord.TYPE_MAKE){//生产，半成品入库
                warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecord.getStoreId(),warehouseRecord.getMakeProductId(),warehouseRecord.getMakeNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());//半成品入库
            }
            for(int i = 0;i<details.size();i++){
                WarehouseRecordDetail detail = details.get(i);
                if(detail.getNum().compareTo(BigDecimal.ZERO)!=0){
                    //库存修改
                    if(warehouseRecord.getType()==WarehouseRecord.TYPE_BUY){//进货
                        BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());
                    }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_CONSUME
                            || warehouseRecord.getType()==WarehouseRecord.TYPE_SCRAP){//报废、消耗
                        BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());
                    }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_SALE){//销售、实际库存改变转换为原料改变
                        Product product = productService.findById(detail.getProductId());
                        for(int p=0;p<product.getDetails().size();p++){
                            warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecord.getStoreId(),product.getDetails().get(p).getDetailId(),detail.getNum().multiply(product.getDetails().get(p).getNum()),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());
                        }
                    }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_MAKE){//生产
                        BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());//原料出库
                    }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_CHECK){//盘点,库存直接修改为盘点量,并生成之前的库存量,用作后面统计数据,分析盘点的缺失问题
                        if(detail.getNum().compareTo(BigDecimal.ZERO)!=-1){//大于等于0,可以修改库存,小于0不能修改库存,认为是无效数据
                            BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_CHANGE,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());//原料出库
                        }
                    }
                    if(warehouseRecord.getType()==WarehouseRecord.TYPE_CHECK
                            &&detail.getNum().compareTo(BigDecimal.ZERO)==-1){
                    }else{
                    }
                }else{
                    if(warehouseRecord.getType()==WarehouseRecord.TYPE_CHECK){//盘点,库存直接修改为盘点量,并生成之前的库存量,用作后面统计数据,分析盘点的缺失问题
                        BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_CHANGE,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());//原料出库
                    }
                }
            }
        }
    }

    @Transactional
    public void insert(WarehouseRecord warehouseRecord){
        warehouseRecord.setCreatTime(new Timestamp(System.currentTimeMillis()));
        warehouseRecord.setOddId(UUID.randomUUID().toString().replaceAll("-",""));
        //保存详细列表
        List<WarehouseRecordDetail> details = warehouseRecord.getListDetails();
        if(warehouseRecord.getType()==WarehouseRecord.TYPE_MAKE){//生产，半成品入库
            warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecord.getStoreId(),warehouseRecord.getMakeProductId(),warehouseRecord.getMakeNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());//半成品入库
        }
        for(int i = 0;i<details.size();i++){
            WarehouseRecordDetail detail = details.get(i);
            if(detail.getNum().compareTo(BigDecimal.ZERO)!=0){
                detail.setOddId(warehouseRecord.getOddId());
                detail.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
                //库存修改
                if(warehouseRecord.getType()==WarehouseRecord.TYPE_BUY){//进货
                    BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());
                    detail.setBeforeSaveNum(beforeSaveNum);
                }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_CONSUME
                        || warehouseRecord.getType()==WarehouseRecord.TYPE_SCRAP){//报废、消耗
                    BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());
                    detail.setBeforeSaveNum(beforeSaveNum);
                }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_SALE){//销售、实际库存改变转换为原料改变
                    Product product = productService.findById(detail.getProductId());
                    for(int p=0;p<product.getDetails().size();p++){
                        warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecord.getStoreId(),product.getDetails().get(p).getDetailId(),detail.getNum().multiply(product.getDetails().get(p).getNum()),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());
                    }
                }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_MAKE){//生产
                    BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());//原料出库
                    detail.setBeforeSaveNum(beforeSaveNum);
                }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_CHECK){//盘点,库存直接修改为盘点量,并生成之前的库存量,用作后面统计数据,分析盘点的缺失问题
                    if(detail.getNum().compareTo(BigDecimal.ZERO)!=-1){//大于等于0,可以修改库存,小于0不能修改库存,认为是无效数据
                        BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_CHANGE,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());//原料出库
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
                    BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_CHANGE,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());//原料出库
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
        if(record!=null && record.getConfirmFlag()==WarehouseRecord.CONFIRMFLAG_NO){
            //只有未确认的单子才能进行库存操作，已确认的单子不执行
            record.setConfirmFlag(WarehouseRecord.CONFIRMFLAG_YES);
            warehouseRecordMapper.update(record);
            //配送单确认后，库存改变
            List<WarehouseRecordDetail> details = record.getListDetails();
            for(int i = 0;i<details.size();i++){
                WarehouseRecordDetail detail = details.get(i);
                //配送单位库存减少
                warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,record.getStoreId(),detail.getProductId(),detail.getNum(),record.getDateTime(),record.getCreatTime());
                //接收单位库存增加
                warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,record.getSendStoreId(),detail.getProductId(),detail.getNum(),record.getDateTime(),record.getCreatTime());
            }
        }
    }

    @Transactional
    public void update(WarehouseRecord warehouseRecord) throws Exception{
        WarehouseRecord warehouseRecordOld = warehouseRecordMapper.findById(warehouseRecord.getOddId());
        if(warehouseRecordOld!=null){
            //判断权限
            if(warehouseRecordOld.getStoreId()!=warehouseRecord.getStoreId()){
                throw new Exception("您无权限修改该记录");
            }
            //修改详细列表
            //假删记录，使库存回退
            delete(warehouseRecord.getOddId());
            warehouseRecordOld.setStatus(WarehouseRecord.STATUS_ON);
            warehouseRecord.setCreatTime(new Timestamp(System.currentTimeMillis()));
            //1、删除所有详细记录
            warehouseRecordDetailMapper.deleteByOddId(warehouseRecord.getOddId());
            //2、保存新的详细记录
            //保存详细列表
            List<WarehouseRecordDetail> details = warehouseRecord.getListDetails();
            if(warehouseRecord.getType()==WarehouseRecord.TYPE_MAKE){//生产，半成品入库
                warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecord.getStoreId(),warehouseRecord.getMakeProductId(),warehouseRecord.getMakeNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());//半成品入库
            }
            for(int i = 0;i<details.size();i++){
                WarehouseRecordDetail detail = details.get(i);
                if(detail.getNum().compareTo(BigDecimal.ZERO)!=0){
                    detail.setOddId(warehouseRecord.getOddId());
                    detail.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
                    //库存修改
                    if(warehouseRecord.getType()==WarehouseRecord.TYPE_BUY){//进货
                        BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());
                        detail.setBeforeSaveNum(beforeSaveNum);
                    }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_CONSUME
                            || warehouseRecord.getType()==WarehouseRecord.TYPE_SCRAP){//报废、消耗
                        BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());
                        detail.setBeforeSaveNum(beforeSaveNum);
                    }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_SALE){//销售、实际库存改变转换为原料改变
                        Product product = productService.findById(detail.getProductId());
                        for(int p=0;p<product.getDetails().size();p++){
                            warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecord.getStoreId(),product.getDetails().get(p).getDetailId(),detail.getNum().multiply(product.getDetails().get(p).getNum()),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());
                        }
                    }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_MAKE){//生产
                        BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());//原料出库
                        detail.setBeforeSaveNum(beforeSaveNum);
                    }else if(warehouseRecord.getType()==WarehouseRecord.TYPE_CHECK){//盘点,库存直接修改为盘点量,并生成之前的库存量,用作后面统计数据,分析盘点的缺失问题
                        if(detail.getNum().compareTo(BigDecimal.ZERO)!=-1){//大于等于0,可以修改库存,小于0不能修改库存,认为是无效数据
                            BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_CHANGE,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());//原料出库
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
                        BigDecimal beforeSaveNum = warehouseService.updateSave(WarehouseServiceImpl.OP_CHANGE,warehouseRecord.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecord.getDateTime(),warehouseRecord.getCreatTime());//原料出库
                        detail.setBeforeSaveNum(beforeSaveNum);
                        warehouseRecordDetailMapper.insert(detail);
                    }
                }
            }
            warehouseRecordMapper.update(warehouseRecord);
        }
    }

    public Map<String,BigDecimal> getWarehouseChangeList(List<WarehouseRecordDetail> beforeList,List<WarehouseRecordDetail> afterList){
        Map<String,BigDecimal> resultMap = new HashMap<>();
        for(int i=0;i<beforeList.size();i++){
            WarehouseRecordDetail before = beforeList.get(i);
            boolean is = false;
            for(int x=0;x<afterList.size();x++){
                WarehouseRecordDetail after = afterList.get(x);
                if(before.getProductId()==after.getProductId()){
                    is = true;
                    break;
                }
            }
            if(!is){
                BigDecimal num = new BigDecimal("0").subtract(before.getNum());
                if(resultMap.get(before.getProductId()+"")!=null){
                    num = resultMap.get(before.getProductId()+"").add(num);
                }
                resultMap.put(before.getProductId()+"",num);
            }
        }

        for(int i=0;i<afterList.size();i++){
            WarehouseRecordDetail after = afterList.get(i);
            boolean is = false;
            for(int x=0;x<beforeList.size();x++){
                WarehouseRecordDetail before = beforeList.get(x);
                if(after.getProductId()==before.getProductId()){
                    BigDecimal num = after.getNum().subtract(before.getNum());
                    if(resultMap.get(before.getProductId()+"")!=null){
                        num = resultMap.get(before.getProductId()+"").add(num);
                    }
                    resultMap.put(before.getProductId()+"",num);
                    is = true;
                    break;
                }
            }
            if(!is){
                BigDecimal num = after.getNum();
                if(resultMap.get(after.getProductId()+"")!=null){
                    num = resultMap.get(after.getProductId()+"").add(num);
                }
                resultMap.put(after.getProductId()+"",num);
            }
        }
        return resultMap;

    }

    @Transactional
    public void delete(String oddId){
        WarehouseRecord warehouseRecordOld = warehouseRecordMapper.findById(oddId);
        if(warehouseRecordOld!=null && warehouseRecordOld.getStatus()==WarehouseRecord.STATUS_ON){
            warehouseRecordOld.setStatus(WarehouseRecord.STATUS_OFF);
            warehouseRecordMapper.update(warehouseRecordOld);
            //单据删除无效后,库存回滚
            //保存详细列表
            List<WarehouseRecordDetail> details = warehouseRecordOld.getListDetails();
            if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_MAKE){//生产，半成品入库回退
                warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecordOld.getStoreId(),warehouseRecordOld.getMakeProductId(),warehouseRecordOld.getMakeNum(),warehouseRecordOld.getDateTime(),warehouseRecordOld.getCreatTime());//半成品入库
            }
            for(int i = 0;i<details.size();i++){
                WarehouseRecordDetail detail = details.get(i);
                if(detail.getNum().compareTo(BigDecimal.ZERO)!=0){
                    //库存修改
                    if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_BUY){//进货回退
                        warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecordOld.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecordOld.getDateTime(),warehouseRecordOld.getCreatTime());
                    }else if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_CONSUME
                            || warehouseRecordOld.getType()==WarehouseRecord.TYPE_SCRAP){//报废、消耗回退
                        warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecordOld.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecordOld.getDateTime(),warehouseRecordOld.getCreatTime());
                    }else if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_SALE){//销售、实际库存改变转换为原料改变,回退
                        Product product = productService.findById(detail.getProductId());
                        for(int p=0;p<product.getDetails().size();p++){
                            warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecordOld.getStoreId(),product.getDetails().get(p).getDetailId(),detail.getNum().multiply(product.getDetails().get(p).getNum()),warehouseRecordOld.getDateTime(),warehouseRecordOld.getCreatTime());
                        }
                    }else if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_MAKE){//生产 回退
                        warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecordOld.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecordOld.getDateTime(),warehouseRecordOld.getCreatTime());//原料出库
                    }else if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_SEND && warehouseRecordOld.getConfirmFlag()==WarehouseRecord.CONFIRMFLAG_YES){//配送回退
                        //配送单位库存回退增加
                        warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecordOld.getStoreId(),detail.getProductId(),detail.getNum(),warehouseRecordOld.getDateTime(),warehouseRecordOld.getCreatTime());
                        //接收单位库存回退减少
                        warehouseService.updateSave(WarehouseServiceImpl.OP_OUT,warehouseRecordOld.getSendStoreId(),detail.getProductId(),detail.getNum(),warehouseRecordOld.getDateTime(),warehouseRecordOld.getCreatTime());
                    }else if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_CHECK){//盘点,根据盘点后的库存和盘点之前的库存,差值进行回退
                        BigDecimal num = detail.getBeforeSaveNum().subtract(detail.getNum());
                        warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecordOld.getStoreId(),detail.getProductId(),num,warehouseRecordOld.getDateTime(),warehouseRecordOld.getCreatTime());
                    }
                }else{
                    if(warehouseRecordOld.getType()==WarehouseRecord.TYPE_CHECK){//盘点,根据盘点后的库存和盘点之前的库存,差值进行回退
                        BigDecimal num = detail.getBeforeSaveNum().subtract(detail.getNum());
                        warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouseRecordOld.getStoreId(),detail.getProductId(),num,warehouseRecordOld.getDateTime(),warehouseRecordOld.getCreatTime());
                    }
                }
            }
        }
    }

    public WarehouseRecord findById(String oddId){
        return warehouseRecordMapper.findById(oddId);
    }

    public WarehouseRecord findLastCheck(int storeId,Timestamp time){
        PageSearch pageSearch = new PageSearch();
        pageSearch.setPageSize(1);
        List<WarehouseRecord> list = list(pageSearch,WarehouseRecord.TYPE_CHECK,storeId,new Date(time.getTime()),null,"date asc");
        if(list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }
}
