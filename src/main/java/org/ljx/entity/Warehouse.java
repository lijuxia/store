package org.ljx.entity;

import java.io.Serializable;

/**
 * 仓库（库存）
 * Created by ljx on 2018/5/14.
 */
public class Warehouse implements Serializable {
    private int id;
    private double balance;
    private int storeId;
    private int productId;
    private byte status;
    public static final byte STATUS_ON = 1;
    public static final byte STATUS_OFF = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
}
