package org.ljx.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库单详细
 * Created by ljx on 2018/5/14.
 */
public class WarehouseRecordDetail implements Serializable {
    private String uuid;
    private String oddId;
    private int productId;
    private BigDecimal num;
    private String productName;
    private String unit;
    private BigDecimal beforeSaveNum;
    private Product product;

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

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
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

    public BigDecimal getBeforeSaveNum() {
        return beforeSaveNum;
    }

    public void setBeforeSaveNum(BigDecimal beforeSaveNum) {
        this.beforeSaveNum = beforeSaveNum;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
