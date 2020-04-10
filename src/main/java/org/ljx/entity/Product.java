package org.ljx.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品
 * Created by ljx on 2018/5/14.
 */
public class Product implements Serializable,Comparable {
    private int id;
    private String code;
    private String name;
    private String unit;//单位
    private byte status;
    private byte type;//原料、商品、半成品
    private int indexFlag; // 排序
    public static final byte STATUS_ON = 1;
    public static final byte STATUS_OFF = 2;
    public static final byte TYPE_MATERIAL = 1;
    public static final byte TYPE_GOODS = 2;
    public static final byte TYPE_HALF = 3;
    public List<ProductDetail> details = new ArrayList<ProductDetail>();
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public List<ProductDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ProductDetail> details) {
        this.details = details;
    }

    public int getIndexFlag() {
        return indexFlag;
    }

    public void setIndexFlag(int indexFlag) {
        this.indexFlag = indexFlag;
    }

    public String getTypeName(){
        switch (this.type){
            case Product.TYPE_MATERIAL:return "原料";
            case Product.TYPE_GOODS:return "商品";
            case Product.TYPE_HALF:return "半成品";
            default:return "错误";
        }
    }

    @Override
    public int compareTo(Object o) {
        Product p = (Product) o;
        if (this.id > p.getId()) {
            return 1;
        }
        if (this.id < p.getId()) {
            return -1;
        }
        return 0;
    }
}
