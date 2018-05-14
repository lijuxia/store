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
}
