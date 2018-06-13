package org.ljx.entity.web;

import java.io.Serializable;

/**
 * 返回封装
 * Created by WangHao on 2015/1/23.
 */
public class ResponseMessage implements Serializable {
    private boolean succeed;
    private Integer code;
    private String msg;
    private Object value;

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
