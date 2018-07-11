package org.ljx.quartz;

import org.ljx.entity.Warehouse;
import org.ljx.service.warehouse.WarehouseService;
import org.ljx.service.warehouse.WarehouseServiceImpl;
import org.ljx.util.SpringTools;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 结算库存
 * Created by ljx on 2018/7/6.
 */
public class Settlement implements Job {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private WarehouseService warehouseService = (WarehouseService) SpringTools.getBean("warehouseServiceImpl");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Warehouse> list = warehouseService.list(0,Warehouse.STATUS_ON);
        for(int i=0;i<list.size();i++){
            Warehouse warehouse = list.get(i);
            warehouseService.updateSave(WarehouseServiceImpl.OP_INTO,warehouse.getStoreId(),warehouse.getProductId(),new BigDecimal(0),new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()));
        }
    }
}
