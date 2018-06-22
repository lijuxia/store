package org.ljx.service.report;

import org.ljx.entity.*;
import org.ljx.entity.report.ReportCell;
import org.ljx.service.product.ProductService;
import org.ljx.service.warehouse.WarehouseService;
import org.ljx.service.warehouseRecord.WarehouseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by ljx on 2018/6/13.
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    WarehouseRecordService warehouseRecordService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    ProductService productService;

    private final byte OP_IN = 1;
    private final byte OP_OUT = 2;
    private final byte OP_SAVE = 3;

    public List<Map<String,ReportCell>> list(byte type, int storeId, Date beginDate, Date endDate){
        List<Warehouse> warehouseList = warehouseService.list(storeId,(byte)0,beginDate,endDate);
        List<WarehouseRecord> list = warehouseRecordService.list(null,type,storeId,beginDate,endDate);
        //初始化数据
        List<Map<String,ReportCell>> dataList = initList(getDayFromDate(endDate));
        //遍历流水记录，统计结果写入数据中
        for(int i=0;i<list.size();i++){
            addRecord(dataList,list.get(i),storeId);//统计记录
        }
        //遍历库存记录，将库存写入数据中
        for(int i=0;i<warehouseList.size();i++){
            Warehouse warehouse = warehouseList.get(i);
            int index = getDayFromDate(warehouse.getTime())-1;//记录在数据的位置
            addCell(dataList,index,warehouse.getProductId(),OP_SAVE,warehouse.getBalance());
        }
        return dataList;
    }

    public Map<String,Set<Product>> list(){
        //获取所有半成品
        List<Product> list = productService.list(new byte[]{Product.TYPE_HALF});
        Map<String,Set<Product>> map = new HashMap<>();
        for(int i=0;i<list.size();i++){
            Product product = list.get(i);
            for(int x=0;x<product.getDetails().size();x++){
                ProductDetail detail = product.getDetails().get(x);
                Set<Product> set = new TreeSet<Product>();
                if(map.get(detail.getDetailId()+"")!=null){
                    set = map.get(detail.getDetailId()+"");
                }
                set.add(product);
                map.put(detail.getDetailId()+"",set);
            }
        }
        return map;
    }

    private List<Map<String,ReportCell>> addRecord(List<Map<String,ReportCell>> dataList,WarehouseRecord record,int storeId){
        int index = getDayFromDate(record.getDate())-1;//记录在数据的位置
        if(record.getType() == WarehouseRecord.TYPE_MAKE){//生产
            //生产半成品，表示进货
            addProduct(dataList,index,record,record.getMakeProduct(),OP_IN,record.getMakeNum());
        }
        //遍历记录中包含的产品
        for(int p=0;p<record.getListDetails().size();p++){
            WarehouseRecordDetail detail = record.getListDetails().get(p);//产品详细记录
            //将流水详细处理到数据中
            addDetail(storeId,dataList,index,record,detail);
        }
        return dataList;
    }

    private List<Map<String,ReportCell>> addDetail(int storeId,List<Map<String,ReportCell>> dataList,int index,WarehouseRecord record, WarehouseRecordDetail detail){
        if(record.getType() == WarehouseRecord.TYPE_SEND){//配送
            if(record.getStoreId()==storeId){//为送货方
                addProduct(dataList,index,record,detail.getProduct(),OP_OUT,detail.getNum());
            }else if(record.getSendStoreId()==storeId){//进货方
                addProduct(dataList,index,record,detail.getProduct(),OP_IN,detail.getNum());
            }
        }else{
            if(record.getInOrOut()==WarehouseRecord.INOROUT_IN){
                addProduct(dataList,index,record,detail.getProduct(),OP_IN,detail.getNum());
            }else if(record.getInOrOut()==WarehouseRecord.INOROUT_OUT){
                addProduct(dataList,index,record,detail.getProduct(),OP_OUT,detail.getNum());
            }
        }
        return dataList;
    }

    private List<Map<String,ReportCell>> addProduct(List<Map<String,ReportCell>> dataList,int index,WarehouseRecord record,Product product,byte op,BigDecimal num){
        if(product.getType() == Product.TYPE_GOODS){
            addCell(dataList,index,product.getId(),op,num);
            for(int i=0;i<product.getDetails().size();i++){
                ProductDetail de = product.getDetails().get(i);
                addCell(dataList,index,de.getDetailId(),op,num.multiply(de.getNum()));
            }
        }else if(product.getType() == Product.TYPE_MATERIAL){
            addCell(dataList,index,product.getId(),op,num);
        }else if(product.getType() == Product.TYPE_HALF){
            addCell(dataList,index,product.getId(),op,num);
        }
        if(record.getType()==WarehouseRecord.TYPE_MAKE){//生产单，需要额外添加半成品消耗原料详细
            addCellMap(dataList,index,product.getId(),record.getMakeProductId(),num);
        }
        return dataList;
    }

    private List<Map<String,ReportCell>> addCell(List<Map<String,ReportCell>> dataList,int index,int productId,byte op,BigDecimal num){
        String key = productId+"";//产品ID
        ReportCell cell = dataList.get(index).get(key);//产品对应统计内容
        if(cell==null){
            cell = new ReportCell();
        }
        switch (op){//判断统计的位置
            case OP_IN:
                cell.addIn(num);
                break;
            case OP_OUT:
                cell.addOut(num);
                break;
            case OP_SAVE:
                cell.setSave(num);
            default:
                break;
        }
        dataList.get(index).put(key,cell);
        return dataList;
    }

    private List<Map<String,ReportCell>> addCellMap(List<Map<String,ReportCell>> dataList,int index,int productId,int makeProductId,BigDecimal makeNum){
        String key = productId+"";//产品ID
        ReportCell cell = dataList.get(index).get(key);//产品对应统计内容
        if(cell==null){
            cell = new ReportCell();
        }
        if(cell.getMakeMap().get(makeProductId+"")!=null){
            makeNum = cell.getMakeMap().get(makeProductId+"").add(makeNum);
            cell.getMakeMap().put(makeProductId+"",makeNum);
        }else{
            cell.getMakeMap().put(makeProductId+"",makeNum);
        }
        dataList.get(index).put(key,cell);
        return dataList;
    }


    private List initList(int day){
        List list = new ArrayList();
        for(int i=0;i<day;i++){
            list.add(new HashMap<String,ReportCell>());
        }
        return list;
    }

    private int getDayFromDate(Date date){
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(date.getTime());
        int i = ca.get(Calendar.DAY_OF_MONTH);
        return i;
    }

    private int getDayFromDate(Timestamp date){
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(date.getTime());
        int i = ca.get(Calendar.DAY_OF_MONTH);
        return i;
    }
}
