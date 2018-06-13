package org.ljx.service.report;

import com.github.pagehelper.PageHelper;
import org.ljx.entity.*;
import org.ljx.entity.report.ReportCell;
import org.ljx.entity.web.PageSearch;
import org.ljx.service.product.ProductService;
import org.ljx.service.warehouse.WarehouseService;
import org.ljx.service.warehouseRecord.WarehouseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final byte OP_IN = 1;
    private final byte OP_OUT = 2;
    private final byte OP_SAVE = 3;

    public List<Map<String,ReportCell>> list(byte type, int storeId, Date beginDate, Date endDate){
        List<Warehouse> warehouseList = warehouseService.list(storeId,(byte)0);
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

    private List<Map<String,ReportCell>> addRecord(List<Map<String,ReportCell>> dataList,WarehouseRecord record,int storeId){
        int index = getDayFromDate(record.getDate())-1;//记录在数据的位置
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
                addProduct(dataList,index,detail.getProduct(),OP_OUT,detail.getNum());
            }else if(record.getSendStoreId()==storeId){//进货方
                addProduct(dataList,index,detail.getProduct(),OP_IN,detail.getNum());
            }
        }else{
            if(record.getInOrOut()==WarehouseRecord.INOROUT_IN){
                addProduct(dataList,index,detail.getProduct(),OP_IN,detail.getNum());

            }else if(record.getInOrOut()==WarehouseRecord.INOROUT_OUT){
                addProduct(dataList,index,detail.getProduct(),OP_OUT,detail.getNum());
            }
        }
        return dataList;
    }

    private List<Map<String,ReportCell>> addProduct(List<Map<String,ReportCell>> dataList,int index,Product product,byte op,double num){
        if(product.getType() == Product.TYPE_GOODS){
            for(int i=0;i<product.getDetails().size();i++){
                ProductDetail de = product.getDetails().get(i);
                addCell(dataList,index,de.getDetailId(),op,num*de.getNum());
            }
        }else if(product.getType() == Product.TYPE_MATERIAL){
            addCell(dataList,index,product.getId(),op,num);
        }
        return dataList;
    }

    private List<Map<String,ReportCell>> addCell(List<Map<String,ReportCell>> dataList,int index,int productId,byte op,double num){
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
