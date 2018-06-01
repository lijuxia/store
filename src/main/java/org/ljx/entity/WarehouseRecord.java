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
    private int storeName;
    private byte type;//销售、报废、配送、采购
    private byte status;//状态：有效、无效
    private byte inOrOut;  //出库、入库
    private Timestamp creatTime;
    private byte confirmFlag;//确认标志
    private String remark;//备注
    public static final byte TYPE_SELL_OUT = 1;
    public static final byte TYPE_SEND = 2;
    public static final byte TYPE_SCRAP = 3;
    public static final byte TYPE_BUY = 4;
    public static final byte STATUS_ON = 1;
    public static final byte STATUS_YES = 2;
    public static final byte INOROUT_IN = 1;
    public static final byte INOROUT_OUT = 2;
    public static final byte CONFIRMFLAG_YES = 1;
    public static final byte CONFIRMFLAG_NO = 2;

    public String getOddId() {
        return oddId;
    }

    public void setOddId(String oddId) {
        this.oddId = oddId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getStoreName() {
        return storeName;
    }

    public void setStoreName(int storeName) {
        this.storeName = storeName;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(byte inOrOut) {
        this.inOrOut = inOrOut;
    }

    public Timestamp getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Timestamp creatTime) {
        this.creatTime = creatTime;
    }

    public byte getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(byte confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
