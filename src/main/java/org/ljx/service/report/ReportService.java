package org.ljx.service.report;

import org.ljx.entity.WarehouseRecord;
import org.ljx.entity.report.ReportCell;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ljx on 2018/6/13.
 */
public interface ReportService {

    public List<Map<String,ReportCell>> list(byte type, int storeId, Date beginDate, Date endDate);
}
