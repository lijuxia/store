package org.ljx.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
    private byte type;//销售、报废、配送、采购、消耗、生产
    private byte status;//状态：有效、无效
    private byte inOrOut;  //出库、入库
    private Timestamp creatTime;
    private byte confirmFlag;//确认标志
    private String remark="";//备注
    private int sendStoreId=0;
    private String sendStoreName="";
    private Date date;
    private int makeProductId=0;
    private Product makeProduct;
    private BigDecimal makeNum = new BigDecimal(0);
    private List<WarehouseRecordDetail> listDetails = new ArrayList<>();
    public static final byte TYPE_SALE = 1; //销售
    public static final byte TYPE_SEND = 2; //配送
    public static final byte TYPE_SCRAP = 3;    //报废
    public static final byte TYPE_BUY = 4;  //采购
    public static final byte TYPE_CONSUME = 5;  //消耗
    public static final byte TYPE_CHECK = 6;  //消耗
    public static final byte TYPE_MAKE = 7;  //生产
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
        this.type = 0;//销售、报废、配送、采购、消耗、生产
        this.status = WarehouseRecord.STATUS_ON;//状态：有效、无效
        this.inOrOut = WarehouseRecord.INOROUT_IN;  //出库、入库
        this.confirmFlag = WarehouseRecord.CONFIRMFLAG_NO;//确认标志
        this.remark="";//备注
        this.sendStoreId=0;
        this.sendStoreName="";
        this.makeProductId=0;
        this.makeNum=new BigDecimal(0);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Timestamp getDateTime(){
        return new Timestamp(date.getTime());
    }

    public int getMakeProductId() {
        return makeProductId;
    }

    public void setMakeProductId(int makeProductId) {
        this.makeProductId = makeProductId;
    }

    public Product getMakeProduct() {
        return makeProduct;
    }

    public void setMakeProduct(Product makeProduct) {
        this.makeProduct = makeProduct;
    }

    public BigDecimal getMakeNum() {
        return makeNum;
    }

    public void setMakeNum(BigDecimal makeNum) {
        this.makeNum = makeNum;
    }

    public List<WarehouseRecordDetail> getListDetails() {
        return listDetails;
    }

    public void setListDetails(List<WarehouseRecordDetail> listDetails) {
        this.listDetails = listDetails;
    }

    public String getCreatTimeStr(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(this.creatTime==null){
            return "";
        }
        return dateFormat.format(this.creatTime);
    }

    public String getDateStr(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(this.date==null){
            return "";
        }
        return dateFormat.format(this.date);
    }

    public String getTypeStr(){
        switch (this.type){
            case TYPE_SALE: return "销售";
            case TYPE_SEND:return "配送";
            case TYPE_SCRAP:return "报废";
            case TYPE_BUY:return "采购";
            case TYPE_CONSUME:return "消耗";
            case TYPE_CHECK:return "盘点";
            case TYPE_MAKE:return "生产";
            default:return "错误";
        }
    }

    public String getDetailsStr(){
        StringBuffer str = new StringBuffer("");
        StringBuffer check = new StringBuffer("");
        for(int i=0;i<this.listDetails.size();i++){
            if(!"".equals(str.toString())){
                str.append(" , ");
            }
            str.append(this.listDetails.get(i).getProductName()+" : "+this.listDetails.get(i).getNum().stripTrailingZeros().toPlainString()+this.listDetails.get(i).getUnit());
            if(type==TYPE_CHECK){
                if(this.listDetails.get(i).getNum().subtract(this.listDetails.get(i).getBeforeSaveNum()).compareTo(BigDecimal.ZERO)!=0){//没有误差的不显示
                    if(!"".equals(check.toString())){
                        check.append(" , ");
                    }
                    if(this.listDetails.get(i).getNum().subtract(this.listDetails.get(i).getBeforeSaveNum()).doubleValue()<0){
                        check.append("<font color='red'>"+this.listDetails.get(i).getProductName()+" : "+this.listDetails.get(i).getNum().subtract(this.listDetails.get(i).getBeforeSaveNum()).stripTrailingZeros().toPlainString()+this.listDetails.get(i).getUnit()+"</font>");
                    }else{
                        check.append(this.listDetails.get(i).getProductName()+" : "+this.listDetails.get(i).getNum().subtract(this.listDetails.get(i).getBeforeSaveNum()).stripTrailingZeros().toPlainString()+this.listDetails.get(i).getUnit());
                    }
                }
            }
        }
        StringBuffer make = new StringBuffer("");
        if(type==TYPE_MAKE){
            make.append("[生产] "+makeProduct.getName()+" : "+makeNum.stripTrailingZeros().toPlainString()+makeProduct.getUnit()+"<br><br>");
            make.append("  [原料] "+str);
            str = make;
        }else if(type==TYPE_CHECK){
            if(!"".equals(check.toString())){
                make.append("[问题] "+check+"<br><br>");
            }
            make.append("  [盘点] "+str);
            str = make;
        }
        return str.toString();
    }
}
