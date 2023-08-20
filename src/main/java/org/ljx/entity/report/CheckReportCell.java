package org.ljx.entity.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 盘点统计单元格
 * Created by ljx on 2018/7/30.
 */
public class CheckReportCell implements Serializable {

    private BigDecimal before;
    private BigDecimal check;
    private BigDecimal error;
    public CheckReportCell(){
        this.before = new BigDecimal(0);
        this.check = new BigDecimal(0);
        this.error = new BigDecimal(0);
    }

    public BigDecimal getBefore() {
        return before;
    }

    public void setBefore(BigDecimal before) {
        this.before = before;
    }

    public BigDecimal getCheck() {
        return check;
    }

    public void setCheck(BigDecimal check) {
        this.check = check;
    }

    public BigDecimal getError() {
        return error;
    }

    public void setError(BigDecimal error) {
        this.error = error;
    }

    public void addError(BigDecimal num){
        this.error = this.error.add(num);
    }
}
