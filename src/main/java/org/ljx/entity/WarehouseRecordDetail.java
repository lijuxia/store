package org.ljx.entity;

import java.io.Serializable;

/**
 * 库单详细
 * Created by ljx on 2018/5/14.
 */
public class WarehouseRecordDetail implements Serializable {
    private String uuid;
    private String oddId;
    private int productId;
    private double num;
    private String productName;
    private String unit;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOddId() {
        return oddId;
    }

    public void setOddId(String oddId) {
        this.oddId = oddId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
