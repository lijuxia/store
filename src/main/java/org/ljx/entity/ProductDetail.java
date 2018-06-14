package org.ljx.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by ljx on 2018/6/9.
 */
public class ProductDetail implements Serializable {
    private int id;
    private int productId;
    private int detailId;
    private BigDecimal num;
    private byte status;
    private Product detail;
    public static final byte STATUS_ON = 1;
    public static final byte STATUS_OFF = 2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Product getDetail() {
        return detail;
    }

    public void setDetail(Product detail) {
        this.detail = detail;
    }
}
