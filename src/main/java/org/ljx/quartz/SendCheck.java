package org.ljx.quartz;

import org.ljx.entity.WarehouseRecord;
import org.ljx.service.warehouseRecord.WarehouseRecordService;
import org.ljx.util.SpringTools;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 *
 * 日志统计定时器
 * Created by ztw on 2017/5/8.
 */
public class SendCheck implements Job {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private WarehouseRecordService warehouseRecordService = (WarehouseRecordService) SpringTools.getBean("warehouseRecordServiceImpl");

    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date lastDay = getLastDay();
        List<WarehouseRecord> list = warehouseRecordService.list(null,WarehouseRecord.TYPE_SEND,0,lastDay,lastDay);
        for(int i=0;i<list.size();i++){
            WarehouseRecord record = list.get(i);
            if(record.getConfirmFlag()==WarehouseRecord.CONFIRMFLAG_NO){
                warehouseRecordService.confirmeRecord(record.getOddId());
            }
        }
    }


    private Date getLastDay(){
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,-1);
        Date date = new Date(c.getTime().getTime());
        return date;
    }

}