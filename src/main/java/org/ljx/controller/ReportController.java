package org.ljx.controller;

import org.ljx.entity.Product;
import org.ljx.entity.WarehouseRecord;
import org.ljx.entity.report.ReportCell;
import org.ljx.entity.web.PageSearch;
import org.ljx.entity.web.ResponseMessage;
import org.ljx.service.product.ProductService;
import org.ljx.service.report.ReportService;
import org.ljx.service.warehouseRecord.WarehouseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

/**
 * Created by ljx on 2018/6/13.
 */
@RestController
@RequestMapping("report")
public class ReportController extends BaseController {
    @Autowired
    ReportService reportService;
    @Autowired
    ProductService productService;

    @RequestMapping(value = "/list" ,method = RequestMethod.GET)
    public ResponseMessage list(String date){
        Date dateSql = StringToDate(date);
        List<Map<String,ReportCell>> list = reportService.list((byte)0,getCurrentStore().getId(),getMonthBegin(dateSql),getMonthEnd(dateSql));
        List<Product> productList = productService.list(new byte[]{Product.TYPE_MATERIAL});
        Map<String,Object> map = new HashMap();
        map.put("productList",productList);
        map.put("valueList",list);
        return success(map);
    }

    private Date StringToDate(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = new Date(System.currentTimeMillis());
        try {
            date = new Date(sdf.parse(dateStr).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        date = new Date(c.getTime().getTime());
        return date;
    }

    private Date getMonthBegin(Date date){
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        date = new Date(c.getTime().getTime());
        return date;
    }

    private Date getMonthEnd(Date date){
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(date.getTime());
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        date = new Date(ca.getTime().getTime());
        return date;
    }
}
