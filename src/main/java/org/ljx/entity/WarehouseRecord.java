package org.ljx.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 仓库记录(出库单，入库单、报废单）
 * Created by ljx on 2018/5/14.
 */
public class WarehouseRecord implements Serializable {
    private String oddId;
    private int storeId;
    private byte type;//销售、报废、配送
    private byte status;//状态：有效、无效
    private byte inOrOut;  //出库、入库
    private Timestamp creatTime;
    private byte confirmFlag;//确认标志
}
