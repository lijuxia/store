package org.ljx.entity.report;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by ljx on 2018/6/13.
 */
public class ReportCell implements Serializable {

    private BigDecimal in;
    private BigDecimal out;
    private BigDecimal save;
    public ReportCell(){
        this.in = new BigDecimal(0);
        this.out = new BigDecimal(0);
        this.save = new BigDecimal(0);
    }

    public BigDecimal getIn() {
        return in;
    }

    public void setIn(BigDecimal in) {
        this.in = in;
    }

    public BigDecimal getOut() {
        return out;
    }

    public void setOut(BigDecimal out) {
        this.out = out;
    }

    public BigDecimal getSave() {
        return save;
    }

    public void setSave(BigDecimal save) {
        this.save = save;
    }

    public void addIn(BigDecimal num){
        this.in.add(num);
    }

    public void addOut(BigDecimal num){
        this.out.add(num);
    }
}
