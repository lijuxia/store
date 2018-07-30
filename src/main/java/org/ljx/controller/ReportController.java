package org.ljx.controller;

import com.sun.deploy.net.URLEncoder;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.ljx.entity.Product;
import org.ljx.entity.Store;
import org.ljx.entity.WarehouseRecord;
import org.ljx.entity.report.CheckReportCell;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
        Map<String,Object> map = new HashMap();
        Date dateSql = StringToDate(date);
        List<Map<String,ReportCell>> list = reportService.list((byte)0,getCurrentStore().getId(),getMonthBegin(dateSql),getMonthEnd(dateSql));
        byte[] types = new byte[]{Product.TYPE_MATERIAL,Product.TYPE_HALF};
        if(getCurrentStore().getType()== Store.TYPE_DISTRIBUTION_CENTRE){
            map.put("makeList",reportService.list());
        }
        List<Product> productList = productService.list(types);
        map.put("productList",productList);
        map.put("valueList",list);
        return success(map);
    }

    @RequestMapping(value = "/checkList" ,method = RequestMethod.GET)
    public ResponseMessage checkList(String date){
        Map<String,Object> map = new HashMap();
        Date dateSql = StringToDate(date);
        List<Map<String,CheckReportCell>> list = reportService.listCheck(getCurrentStore().getId(),getMonthBegin(dateSql),getMonthEnd(dateSql));
        byte[] types = new byte[]{Product.TYPE_MATERIAL,Product.TYPE_HALF};
        List<Product> productList = productService.list(types);
        map.put("productList",productList);
        map.put("valueList",list);
        return success(map);
    }

    @RequestMapping(value = "/updateCenterFile")
    public void updateCenterFile(String date) throws Exception{
        //获取数据
        Map<String,Object> map = new HashMap();
        Date dateSql = StringToDate(date);
        List<Map<String,ReportCell>> list = reportService.list((byte)0,getCurrentStore().getId(),getMonthBegin(dateSql),getMonthEnd(dateSql));
        byte[] types = new byte[]{Product.TYPE_MATERIAL,Product.TYPE_HALF};
        Map<String,Set<Product>> makeList = new HashMap<>();
        if(getCurrentStore().getType()== Store.TYPE_DISTRIBUTION_CENTRE){
            makeList = reportService.list();
        }
        List<Product> productList = productService.list(types);

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");
        //单元格样式
        CellStyle cellStyle = getCellStyle(wb,false);
        CellStyle thCellStyle = getCellStyle(wb,true);
        int index = 1;
        sheet.addMergedRegion(new CellRangeAddress(0,2,0,0));
        for(int i=0;i<productList.size();i++){
            Set<Product> lis = makeList.get(productList.get(i).getId()+"");
            int size = 0;
            int endIndex = index+2;
            if(lis!=null){
                size = lis.size();
                endIndex += size;
            }
            sheet.addMergedRegion(new CellRangeAddress(0,0,index,endIndex));
            sheet.addMergedRegion(new CellRangeAddress(1,1,index,endIndex));
            index +=size;
            index+=3;
        }

        // 创建第一行，设置内容
        Row row = sheet.createRow((short) 0);
        //设置第一栏
        setCell(row,0,"日期",thCellStyle);
        index = 1;
        for(int i=0;i<productList.size();i++){
            Set<Product> lis = makeList.get(productList.get(i).getId()+"");
            setCell(row,index,productList.get(i).getName(),thCellStyle);
            int cols = 3;
            if(lis!=null){
                cols+=lis.size();
            }
            index +=cols;
        }
        //设置第二行，设置内容
        row = sheet.createRow((short) 1);
        index = 1;
        for(int i=0;i<productList.size();i++){
            Set<Product> lis = makeList.get(productList.get(i).getId()+"");
            setCell(row,index,productList.get(i).getUnit(),thCellStyle);
            int cols = 3;
            if(lis!=null){
                cols+=lis.size();
            }
            index +=cols;
        }
        //设置第三行，设置内容
        row = sheet.createRow((short) 2);
        index = 0;
        for(int i=0;i<productList.size();i++){
            setCell(row,index+1,"进",thCellStyle);
            setCell(row,index+2,"出",thCellStyle);
            Set<Product> lis = makeList.get(productList.get(i).getId()+"");
            int size = 0;
            if(lis!=null){
                List<Product> lisArray = new ArrayList(lis);
                size = lis.size();
                for(int x=0;x<lis.size();x++){
                    setCell(row,index+3+x,lisArray.get(x).getName(),thCellStyle);
                }
            }
            setCell(row,index+3+size,"存",thCellStyle);
            index+=3+size;
        }
        //遍历统计内容，设置单元格
        for(int i=0;i<list.size();i++){
            row = sheet.createRow((short) 3+i);
            String day = i+1+"";
            if(i==(list.size()-1)){
                day = "合计";
            }
            setCell(row,0,day,cellStyle);
            index = 0;
            for(int p=0;p<productList.size();p++){
                Map<String,ReportCell> valueMap = list.get(i);
                ReportCell reportCell = valueMap.get(productList.get(p).getId()+"");
                if(reportCell!=null){
                    setCell(row,index+1,reportCell.getIn()+"",cellStyle);
                    setCell(row,index+2,reportCell.getOut()+"",cellStyle);
                    Set<Product> lis = makeList.get(productList.get(p).getId()+"");
                    int size = 0;
                    if(lis!=null){
                        size = lis.size();
                        List<Product> lisArray = new ArrayList(lis);
                        for(int x=0;x<lisArray.size();x++){
                            String value = "";
                            if(reportCell.getMakeMap().get(productList.get(p).getId()+"")!=null){
                                value =reportCell.getMakeMap().get(productList.get(p).getId()+"")+"";
                            }
                            setCell(row,index+3+x,value,cellStyle);
                        }
                    }
                    setCell(row,index+3+size,reportCell.getSave()+"",cellStyle);
                }
            }
        }
        //同理可以设置数据行
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        String fileName = getCurrentStore().getName()+"_报表_"+date+".xls";
        downFile(content,fileName);
    }

    @RequestMapping(value = "/updateFile")
    public void updateFile(String date) throws Exception{
        //获取数据
        Map<String,Object> map = new HashMap();
        Date dateSql = StringToDate(date);
        List<Map<String,ReportCell>> list = reportService.list((byte)0,getCurrentStore().getId(),getMonthBegin(dateSql),getMonthEnd(dateSql));
        byte[] types = new byte[]{Product.TYPE_MATERIAL,Product.TYPE_HALF};
        if(getCurrentStore().getType()== Store.TYPE_DISTRIBUTION_CENTRE){
            map.put("makeList",reportService.list());
        }
        List<Product> productList = productService.list(types);
        map.put("productList",productList);
        map.put("valueList",list);

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");
        //单元格样式
        CellStyle cellStyle = getCellStyle(wb,false);
        CellStyle thCellStyle = getCellStyle(wb,true);
        sheet.addMergedRegion(new CellRangeAddress(0,2,0,0));
        for(int i=0;i<productList.size();i++){
            sheet.addMergedRegion(new CellRangeAddress(0,0,i*3+1,i*3+3));
            sheet.addMergedRegion(new CellRangeAddress(1,1,i*3+1,i*3+3));
        }
//        sheet.addMergedRegion(new CellRangeAddress(0,3,3,3));
//        sheet.addMergedRegion(new CellRangeAddress(0,3,3,3));
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
//        for(int i=0;i<columnNames.length;i++){
//            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
//        }

        // 创建第一行，设置内容
        Row row = sheet.createRow((short) 0);
        //设置第一栏
        setCell(row,0,"日期",thCellStyle);
        for(int i=0;i<productList.size();i++){
            setCell(row,i*3+1,productList.get(i).getName(),thCellStyle);
        }
        //设置第二行，设置内容
        row = sheet.createRow((short) 1);
        for(int i=0;i<productList.size();i++){
            setCell(row,i*3+1,productList.get(i).getUnit(),thCellStyle);
        }
        //设置第三行，设置内容
        row = sheet.createRow((short) 2);
        for(int i=0;i<productList.size();i++){
            setCell(row,i*3+1,"进",thCellStyle);
            setCell(row,i*3+2,"出",thCellStyle);
            setCell(row,i*3+3,"存",thCellStyle);
        }
        //遍历统计内容，设置单元格
        for(int i=0;i<list.size();i++){
            row = sheet.createRow((short) 3+i);
            String day = i+1+"";
            if(i==(list.size()-1)){
                day = "合计";
            }
            setCell(row,0,day,cellStyle);
            for(int p=0;p<productList.size();p++){
                Map<String,ReportCell> valueMap = list.get(i);
                ReportCell reportCell = valueMap.get(productList.get(p).getId()+"");
                if(reportCell!=null){
                    setCell(row,p*3+1,reportCell.getIn()+"",cellStyle);
                    setCell(row,p*3+2,reportCell.getOut()+"",cellStyle);
                    setCell(row,p*3+3,reportCell.getSave()+"",cellStyle);
                }
            }
        }
        //同理可以设置数据行
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        String fileName = getCurrentStore().getName()+"_报表_"+date+".xls";
        downFile(content,fileName);
    }


    @RequestMapping(value = "/updateCheckFile")
    public void updateCheckFile(String date) throws Exception{
        //获取数据
        Map<String,Object> map = new HashMap();
        Date dateSql = StringToDate(date);
        List<Map<String,CheckReportCell>> list = reportService.listCheck(getCurrentStore().getId(),getMonthBegin(dateSql),getMonthEnd(dateSql));
        byte[] types = new byte[]{Product.TYPE_MATERIAL,Product.TYPE_HALF};
        List<Product> productList = productService.list(types);
        map.put("productList",productList);
        map.put("valueList",list);

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");
        //单元格样式
        CellStyle cellStyle = getCellStyle(wb,false);
        CellStyle thCellStyle = getCellStyle(wb,true);
        sheet.addMergedRegion(new CellRangeAddress(0,2,0,0));
        for(int i=0;i<productList.size();i++){
            sheet.addMergedRegion(new CellRangeAddress(0,0,i*3+1,i*3+3));
            sheet.addMergedRegion(new CellRangeAddress(1,1,i*3+1,i*3+3));
        }

        // 创建第一行，设置内容
        Row row = sheet.createRow((short) 0);
        //设置第一栏
        setCell(row,0,"日期",thCellStyle);
        for(int i=0;i<productList.size();i++){
            setCell(row,i*3+1,productList.get(i).getName(),thCellStyle);
        }
        //设置第二行，设置内容
        row = sheet.createRow((short) 1);
        for(int i=0;i<productList.size();i++){
            setCell(row,i*3+1,productList.get(i).getUnit(),thCellStyle);
        }
        //设置第三行，设置内容
        row = sheet.createRow((short) 2);
        for(int i=0;i<productList.size();i++){
            setCell(row,i*3+1,"前",thCellStyle);
            setCell(row,i*3+2,"盘",thCellStyle);
            setCell(row,i*3+3,"错",thCellStyle);
        }
        //遍历统计内容，设置单元格
        for(int i=0;i<list.size();i++){
            row = sheet.createRow((short) 3+i);
            String day = i+1+"";
            if(i==(list.size()-1)){
                day = "合计";
            }
            setCell(row,0,day,cellStyle);
            for(int p=0;p<productList.size();p++){
                Map<String,CheckReportCell> valueMap = list.get(i);
                CheckReportCell reportCell = valueMap.get(productList.get(p).getId()+"");
                if(reportCell!=null){
                    setCell(row,p*3+1,reportCell.getBefore()+"",cellStyle);
                    setCell(row,p*3+2,reportCell.getCheck()+"",cellStyle);
                    setCell(row,p*3+3,reportCell.getError()+"",cellStyle);
                }
            }
        }
        //同理可以设置数据行
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        String fileName = getCurrentStore().getName()+"_盘点统计_"+date+".xls";
        downFile(content,fileName);
    }

    private CellStyle getCellStyle(Workbook wb,boolean isBold){
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)16);
        if(isBold){
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }else{
            font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        }
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private void setCell(Row row,int index,String value,CellStyle style){
        Cell cell = row.createCell(index);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private String getNowStr(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        return sdf.format(new Date(System.currentTimeMillis()));
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
