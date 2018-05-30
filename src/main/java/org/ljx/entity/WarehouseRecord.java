package org.ljx.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 仓库记录(出库单，入库单、报废单）
 * Created by ljx on 2018/5/14.
 */
public class WarehouseRecord implements Serializable {
    private String oddId;
    private int storeId=0;
    private String storeName="";
    private byte type;//销售、报废、配送、采购、消耗
    private byte status;//状态：有效、无效
    private byte inOrOut;  //出库、入库
    private Timestamp creatTime;
    private byte confirmFlag;//确认标志
    private String remark="";//备注
    private int sendStoreId=0;
    private String sendStoreName="";
    private List<WarehouseRecordDetail> listDetails = new ArrayList<>();
    public static final byte TYPE_SELL_OUT = 1;
    public static final byte TYPE_SEND = 2;
    public static final byte TYPE_SCRAP = 3;
    public static final byte TYPE_BUY = 4;
    public static final byte TYPE_CONSUME = 5;
    public static final byte STATUS_ON = 1;
    public static final byte STATUS_OFF = 2;
    public static final byte INOROUT_IN = 1;
    public static final byte INOROUT_OUT = 2;
    public static final byte CONFIRMFLAG_YES = 1;
    public static final byte CONFIRMFLAG_NO = 2;

    public WarehouseRecord(){
        this.oddId = "";
        this.storeId=0;
        this.storeName="";
        this.type = 0;//销售、报废、配送、采购、消耗
        this.status = WarehouseRecord.STATUS_ON;//状态：有效、无效
        this.inOrOut = WarehouseRecord.INOROUT_IN;  //出库、入库
        this.confirmFlag = WarehouseRecord.CONFIRMFLAG_NO;//确认标志
        this.remark="";//备注
        this.sendStoreId=0;
        this.sendStoreName="";
    }

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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
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

    public int getSendStoreId() {
        return sendStoreId;
    }

    public void setSendStoreId(int sendStoreId) {
        this.sendStoreId = sendStoreId;
    }

    public String getSendStoreName() {
        return sendStoreName;
    }

    public void setSendStoreName(String sendStoreName) {
        this.sendStoreName = sendStoreName;
    }

    public List<WarehouseRecordDetail> getListDetails() {
        return listDetails;
    }

    public void setListDetails(List<WarehouseRecordDetail> listDetails) {
        this.listDetails = listDetails;
    }
}
