package org.ljx.entity.report;

import java.io.Serializable;

/**
 * Created by ljx on 2018/6/13.
 */
public class ReportCell implements Serializable {

    private double in;
    private double out;
    private double save;
    public ReportCell(){
        this.in = 0;
        this.out = 0;
        this.save = 0;
    }

    public double getIn() {
        return in;
    }

    public void setIn(double in) {
        this.in = in;
    }

    public double getOut() {
        return out;
    }

    public void setOut(double out) {
        this.out = out;
    }

    public double getSave() {
        return save;
    }

    public void setSave(double save) {
        this.save = save;
    }

    public void addIn(double num){
        this.in += num;
    }

    public void addOut(double num){
        this.out += num;
    }
    public void addSave(double num){
        this.save += num;
    }
}
