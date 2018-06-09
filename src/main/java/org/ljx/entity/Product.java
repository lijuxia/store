package org.ljx.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品
 * Created by ljx on 2018/5/14.
 */
public class Product implements Serializable {
    private int id;
    private String code;
    private String name;
    private String unit;//单位
    private byte status;
    private byte type;//原料、商品
    public static final byte STATUS_ON = 1;
    public static final byte STATUS_OFF = 2;
    public static final byte TYPE_MATERIAL = 1;
    public static final byte TYPE_GOODS = 2;
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

    public String getTypeName(){
        switch (this.type){
            case Product.TYPE_MATERIAL:return "原料";
            case Product.TYPE_GOODS:return "商品";
            default:return "错误";
        }
    }
}
