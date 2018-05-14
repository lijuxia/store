package org.ljx.entity;

import java.io.Serializable;

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
}
