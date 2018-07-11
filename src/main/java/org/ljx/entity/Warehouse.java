package org.ljx.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 仓库（库存）
 * 商品每日记录一条库存记录，没有有且只有一条记录
 * Created by ljx on 2018/5/14.
 */
public class Warehouse implements Serializable {
    private int id;
    private BigDecimal balance = new BigDecimal(0);
    private int storeId;
    private int productId;
    private byte status;
    public static final byte STATUS_ON = 1;
    public static final byte STATUS_OFF = 2;
    private Store store;
    private Product product;
    private Timestamp time;

    public Warehouse(){

    }

    public Warehouse(BigDecimal balance,int storeId,int productId,byte status,Timestamp time){
        if(balance==null){
            this.balance = new BigDecimal(0);
        }else{
            this.balance = balance;
        }
        this.storeId = storeId;
        this.productId = productId;
        this.status = status;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public boolean isToday(){
        return isToday(new Timestamp(System.currentTimeMillis()));
    }

    public boolean isToday(Timestamp newTime){
        return isToday(newTime,this.time);
    }

    public boolean isToday(Timestamp start,Timestamp end){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = dateFormat.format(end);
        String recordTime = dateFormat.format(start);
        if(now.equals(recordTime)){
            return true;
        }else{
            return false;
        }
    }

}
