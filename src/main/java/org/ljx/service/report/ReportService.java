package org.ljx.service.report;

import org.ljx.entity.Product;
import org.ljx.entity.WarehouseRecord;
import org.ljx.entity.report.CheckReportCell;
import org.ljx.entity.report.ReportCell;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ljx on 2018/6/13.
 */
public interface ReportService {

    public List<Map<String,CheckReportCell>> listCheck(int storeId, Date beginDate, Date endDate);

    public List<Map<String,ReportCell>> list(byte type, int storeId, Date beginDate, Date endDate);

    public Map<String,Set<Product>> list();
}
