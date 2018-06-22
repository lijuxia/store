package org.ljx.entity.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ljx on 2018/6/13.
 */
public class ReportCell implements Serializable {

    private BigDecimal in;
    private BigDecimal out;
    private BigDecimal save;
    private Map<String,BigDecimal> makeMap = new HashMap<>();
    public ReportCell(){
        this.in = new BigDecimal(0);
        this.out = new BigDecimal(0);
        this.save = new BigDecimal(0);
        this.makeMap = new HashMap<>();
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

    public Map<String, BigDecimal> getMakeMap() {
        return makeMap;
    }

    public void setMakeMap(Map<String, BigDecimal> makeMap) {
        this.makeMap = makeMap;
    }

    public void addIn(BigDecimal num){
        this.in = this.in.add(num);
    }

    public void addOut(BigDecimal num){
        this.out = this.out.add(num);
    }
}
